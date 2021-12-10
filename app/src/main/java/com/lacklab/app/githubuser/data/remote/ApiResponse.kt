package com.lacklab.app.githubuser.data.remote

import com.google.gson.Gson
import com.lacklab.app.githubuser.data.model.GitHubError
import retrofit2.Response
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object</T>
 * */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body = body,
                        linkHeader = response.headers()["link"]
                    )
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    val gitHubError = Gson().fromJson(msg, GitHubError::class.java)
                    Timber.d("error message: ${gitHubError.message}")
                    gitHubError.message
                }
                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses
 * so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ApiResponse<T>() {
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )
    val nextPages: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { value ->
            val matcher = SINCE_PATTERN.matcher(value)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1)!!)
                } catch (ex: NumberFormatException) {
                    Timber.w("cannot parse next page from $value")
                    null
                }
            }
        }
    }
    val lastPages: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[LAST_LINK]?.let { value->
            val matcher = SINCE_PATTERN.matcher(value)
            if (!matcher.find() || matcher.groupCount() != 1) {
                0
            } else {
                try {
                    Integer.parseInt(matcher.group(1)!!)
                } catch (ex: NumberFormatException) {
                    Timber.w("cannot parse next page from $value")
                    0
                }
            }
        }?: 0
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private val SINCE_PATTERN  = Pattern.compile("\\bsince=(\\d+)")
        private const val NEXT_LINK = "next"
        private const val LAST_LINK = "last"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)
            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)!!] = matcher.group(1)!!
                }
            }
            return links
        }

    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
