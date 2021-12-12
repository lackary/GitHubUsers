package com.lacklab.app.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.lacklab.app.githubuser.R
import com.lacklab.app.githubuser.data.model.GitHubUser
import com.lacklab.app.githubuser.databinding.ItemUserGridBinding
import com.lacklab.app.githubuser.databinding.ItemUserListBinding
import com.lacklab.app.githubuser.ui.view.users.UsersFragment.Companion.isGridLayout
import timber.log.Timber
import javax.inject.Inject

class UserPagingAdapter @Inject constructor()
    : PagingDataAdapter<GitHubUser, UserPagingAdapter.UserViewHolder>(UserDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        if (!isGridLayout) {
            val binding = DataBindingUtil.inflate<ItemUserListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_user_list,
                parent,
                false
            )
            return UserViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemUserGridBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_user_grid,
                parent,
                false
            )
            return UserViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class UserViewHolder(private val binding: ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                // clickListener
            }
        }

        fun bind(item: GitHubUser) {
            if(!isGridLayout) {
                with(binding as ItemUserListBinding) {
                    user = item
                    Glide.with(root)
                        .load(item.avatarUrl)
                        .circleCrop()
                        .override(200, 200)
                        .into(imageViewAvatar)
                }
            } else {
                with(binding as ItemUserGridBinding) {
                    user = item
                    Glide.with(root)
                        .load(item.avatarUrl)
                        .circleCrop()
                        .override(400, 400)
                        .into(imageViewAvatar)
                }
            }

        }

    }
}

private object UserDiffCallback : DiffUtil.ItemCallback<GitHubUser>(){
    override fun areItemsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
        return oldItem == newItem
    }

}