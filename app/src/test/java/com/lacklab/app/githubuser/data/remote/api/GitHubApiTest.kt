package com.lacklab.app.githubuser.data.remote.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.lacklab.app.githubuser.data.remote.ApiEmptyResponse
import com.lacklab.app.githubuser.data.remote.ApiErrorResponse
import com.lacklab.app.githubuser.data.remote.ApiSuccessResponse
import com.lacklab.app.githubuser.data.remote.DataCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

const val BASE_URL = "https://api.github.com/"

@RunWith(JUnit4::class)
class GitHubApiTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: GitHubApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(DataCallAdapterFactory())
            .build()
            .create(GitHubApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUsers() {
        runBlocking {
            try {
                when(val response = service?.getUsers(0, 10)){
                    is ApiSuccessResponse -> {
                        assertThat(response).isNotNull()
                        Assert.assertEquals(10, response.body.size)
                        Timber.d("body: ${response.body}")
                    }
                    is ApiErrorResponse -> {
                        throw Exception(response.errorMessage)
                    }
                    is ApiEmptyResponse -> {
                        Timber.d("ApiEmptyResponse")
                    }
                }
            } catch (ex: Exception) {
                Timber.d("ex: ${ex.message}")
            }
        }
    }
}