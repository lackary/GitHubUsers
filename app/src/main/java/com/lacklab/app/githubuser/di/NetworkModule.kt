package com.lacklab.app.githubuser.di

import com.lacklab.app.githubuser.data.remote.api.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideGitHubService(): GitHubApi {
        return GitHubApi.create()
    }
}