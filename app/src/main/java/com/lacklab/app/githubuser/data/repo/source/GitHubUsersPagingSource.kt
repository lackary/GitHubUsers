package com.lacklab.app.githubuser.data.repo.source

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lacklab.app.githubuser.data.model.GitHubUser
import com.lacklab.app.githubuser.data.model.GitHubUsers
import com.lacklab.app.githubuser.data.remote.ApiEmptyResponse
import com.lacklab.app.githubuser.data.remote.ApiErrorResponse
import com.lacklab.app.githubuser.data.remote.ApiSuccessResponse
import com.lacklab.app.githubuser.data.remote.api.GitHubApi
import com.lacklab.app.githubuser.utils.GITHUB_STARTING_SINCE_INDEX
import com.lacklab.app.githubuser.utils.NETWORK_PAGE_SIZE

class GitHubUsersPagingSource(
    private val api: GitHubApi,
) : PagingSource<Int, GitHubUser>() {
    override fun getRefreshKey(state: PagingState<Int, GitHubUser>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubUser> {
        val since = params.key?: GITHUB_STARTING_SINCE_INDEX
        var nextPage: Int? = null
        return try {
            val response = api.users(
                since = since,
                perPage = params.loadSize)
            var data: GitHubUsers? = null
            when(response) {
                is ApiSuccessResponse -> {
                    data = GitHubUsers(items = response.body)
                    nextPage = response.nextPages

                }
                is ApiErrorResponse -> {
                    throw Exception(response.errorMessage)
                }
                is ApiEmptyResponse -> {
                    data = GitHubUsers(
                        totalCount = 0,
                        incompleteResults = false,
                        items = emptyList(),
                        totalPages = 0
                    )
                }
            }
            LoadResult.Page(
                data = data.items,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}