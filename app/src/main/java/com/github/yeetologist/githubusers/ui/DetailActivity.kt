package com.github.yeetologist.githubusers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.data.response.DetailUserResponse
import com.github.yeetologist.githubusers.data.response.ItemsItem
import com.github.yeetologist.githubusers.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]

        intent.getStringExtra(EXTRA_LOGIN)?.let { detailViewModel.findDetailUser(it) }

        detailViewModel.detail.observe(this){
            if (it != null) {
                setDetailUser(it)
            }
        }
    }

    private fun setDetailUser(userData: DetailUserResponse) {
        binding.tvLogin.text = userData.login
        Glide.with(binding.root)
            .load(userData.avatarUrl)
            .into(binding.ivProfile)
        binding.tvFollowers.text = getString(R.string.followers_label,userData.followers)
        binding.tvFollowing.text = getString(R.string.following_label,userData.following)
        binding.tvName.text = userData.name
        binding.topAppBar.title = userData.login
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }
}