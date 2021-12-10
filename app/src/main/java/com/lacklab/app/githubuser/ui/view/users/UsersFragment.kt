package com.lacklab.app.githubuser.ui.view.users

import androidx.fragment.app.viewModels
import com.lacklab.app.githubuser.R
import com.lacklab.app.githubuser.base.BaseFragment
import com.lacklab.app.githubuser.databinding.FragmentUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding, UsersViewModel>() {

    private val usersViewModel: UsersViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_users

    override fun getVM() = usersViewModel

    override fun bindVM(binding: FragmentUsersBinding, viewModel: UsersViewModel) {
        Timber.d("bindVM")
    }

}