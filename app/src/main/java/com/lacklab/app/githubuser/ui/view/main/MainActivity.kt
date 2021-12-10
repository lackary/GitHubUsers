package com.lacklab.app.githubuser.ui.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lacklab.app.githubuser.R
import com.lacklab.app.githubuser.base.BaseActivity
import com.lacklab.app.githubuser.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private val mainViewModel: MainViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun getVM() = mainViewModel

    override fun bindVM(binding: ActivityMainBinding, viewModel: MainViewModel) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}