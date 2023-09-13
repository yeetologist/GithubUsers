package com.github.yeetologist.githubusers.ui

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.data.response.DetailUserResponse
import com.github.yeetologist.githubusers.databinding.ActivityDetailBinding
import com.github.yeetologist.githubusers.ui.adapter.SectionsPagerAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]

        intent.getStringExtra(EXTRA_LOGIN)?.let { detailViewModel.findDetailUser(it) }

        detailViewModel.detail.observe(this){
            if (it != null) {
                setDetailUser(it)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this,intent.getStringExtra(EXTRA_LOGIN))

        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
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
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}