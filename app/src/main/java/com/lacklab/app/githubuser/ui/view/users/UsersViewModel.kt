package com.lacklab.app.githubuser.ui.view.users

import androidx.paging.PagingData
import com.lacklab.app.githubuser.base.BaseViewModel
import com.lacklab.app.githubuser.data.model.GitHubUser
import com.lacklab.app.githubuser.data.repo.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: GitHubRepository
) : BaseViewModel() {
    private lateinit var _usersFlow: Flow<PagingData<GitHubUser>>
    val userFlow: Flow<PagingData<GitHubUser>>
        get() = _usersFlow

    init {
        getUsers()
    }

    private fun getUsers() {
        _usersFlow = repository.getUsers()
    }
}