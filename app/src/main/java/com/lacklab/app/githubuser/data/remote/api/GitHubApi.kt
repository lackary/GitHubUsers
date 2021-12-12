package com.lacklab.app.githubuser.data.remote.api

import com.lacklab.app.githubuser.data.model.GitHubUser
import com.lacklab.app.githubuser.data.model.GitHubUsers
import com.lacklab.app.githubuser.data.remote.ApiResponse
import com.lacklab.app.githubuser.data.remote.DataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int? = 0,
        @Query("per_page") perPage: Int,
    ): ApiResponse<List<GitHubUser>>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create() : GitHubApi {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(DataCallAdapterFactory())
                .build()
                .create(GitHubApi::class.java)
        }
    }
}