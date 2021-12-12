package com.lacklab.app.githubuser.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lacklab.app.githubuser.data.model.GitHubUser
import com.lacklab.app.githubuser.data.remote.api.GitHubApi
import com.lacklab.app.githubuser.data.repo.source.GitHubUsersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GitHubRepository @Inject constructor(
    private val api: GitHubApi
) {


    fun getUsers(): Flow<PagingData<GitHubUser>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GitHubUsersPagingSource(api = api)
            },
        ).flow
    }
}