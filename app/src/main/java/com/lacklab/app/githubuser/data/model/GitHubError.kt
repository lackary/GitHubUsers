package com.lacklab.app.githubuser.data.model

import com.google.gson.annotations.SerializedName

data class GitHubError(
    val message: String,
    @SerializedName("document_url")
    val documentUrl: String?
)
