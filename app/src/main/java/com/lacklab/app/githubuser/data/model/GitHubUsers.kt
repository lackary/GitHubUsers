package com.lacklab.app.githubuser.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUsers(
    @SerializedName("total_count")
    val totalCount: Int? = 0,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = false,
    var totalPages: Int? = 0,
    val items: List<GitHubUser>
)
