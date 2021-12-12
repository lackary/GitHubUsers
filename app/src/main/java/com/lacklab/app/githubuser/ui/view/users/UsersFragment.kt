package com.lacklab.app.githubuser.ui.view.users

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lacklab.app.githubuser.R
import com.lacklab.app.githubuser.base.BaseFragment
import com.lacklab.app.githubuser.databinding.FragmentUsersBinding
import com.lacklab.app.githubuser.ui.adapter.PagingLoadStateAdapter
import com.lacklab.app.githubuser.ui.adapter.UserPagingAdapter
import com.lacklab.app.githubuser.utils.LAYOUT_GRID
import com.lacklab.app.githubuser.utils.LAYOUT_LINEAR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding, UsersViewModel>() {

    private val usersViewModel: UsersViewModel by viewModels()

    @Inject
    lateinit var userPagingAdapter: UserPagingAdapter

    companion object {
        var isGridLayout = false
    }

    override val layoutId: Int
        get() = R.layout.fragment_users

    override fun getVM() = usersViewModel

    override fun bindVM(binding: FragmentUsersBinding, viewModel: UsersViewModel) {
        Timber.d("bindVM")
        with(binding) {
            with(userPagingAdapter) {
                recycleViewUser.adapter = withLoadStateFooter(
                    footer = PagingLoadStateAdapter(this)
                )
                swipeRefresh.setOnRefreshListener { refresh() }
                with(viewModel) {
                    launchOnLifecycleScope {
                        userFlow.collectLatest { submitData(it) }
                    }

                    launchOnLifecycleScope {
                        loadStateFlow.collectLatest { it ->
                            swipeRefresh.isRefreshing = it.refresh is LoadState.Loading

                            val isItemEmpty =
                                it.refresh !is LoadState.Loading
                                        && userPagingAdapter.itemCount == 0
                            textViewNoResult.isVisible = isItemEmpty
                            recycleViewUser.isVisible = !isItemEmpty

                            // If we have an error, show a toast
                            val errorState = when {
                                it.append is LoadState.Error -> it.append as LoadState.Error
                                it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                                it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                                else -> null
                            }
                            errorState?.let {
                                showToastMessage(it.error.message.toString())
                            }
                        }
                    }
                }
            }

            fabLayout.setOnClickListener {
                isGridLayout = if (!isGridLayout) {
                    changeRecycleViewLayout(this, LAYOUT_GRID)
                    true
                } else {
                    changeRecycleViewLayout(this, LAYOUT_LINEAR)
                    false
                }

            }
        }
    }

    /**
     * change RecycleView layout by linear, Grid, and StaggeredGrid
     * */
    private fun changeRecycleViewLayout(binding: FragmentUsersBinding, layoutType: Int) {
        with(binding) {
            var position = 0
            when(layoutType) {
                LAYOUT_LINEAR -> {
                    position = (recycleViewUser.layoutManager as GridLayoutManager)
                        .findFirstCompletelyVisibleItemPosition()
                    recycleViewUser.layoutManager = LinearLayoutManager(context)
                    fabLayout.setImageResource(R.drawable.ic_round_grid_view_black_24)
                }
                LAYOUT_GRID -> {
                    position = (recycleViewUser.layoutManager as LinearLayoutManager)
                        .findFirstCompletelyVisibleItemPosition()
                    recycleViewUser.layoutManager = GridLayoutManager(context,2)
                    fabLayout.setImageResource(R.drawable.ic_round_view_list_black_24)
                }
            }
            with(userPagingAdapter) {
                recycleViewUser.adapter = withLoadStateFooter(
                    footer = PagingLoadStateAdapter(this)
                )
            }
            recycleViewUser.scrollToPosition(position)
        }

    }
}