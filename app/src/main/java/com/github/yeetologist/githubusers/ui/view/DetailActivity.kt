package com.github.yeetologist.githubusers.ui.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity
import com.github.yeetologist.githubusers.data.remote.response.DetailUserResponse
import com.github.yeetologist.githubusers.databinding.ActivityDetailBinding
import com.github.yeetologist.githubusers.ui.adapter.SectionsPagerAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.DetailViewModel
import com.github.yeetologist.githubusers.ui.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isFindDetailUserCalled = false
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login: String? = intent.getStringExtra(EXTRA_LOGIN)
        val detailViewModel by viewModels<DetailViewModel> {
            DetailViewModelFactory(application)
        }
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (savedInstanceState == null && !isFindDetailUserCalled) {
            if (login != null) {
                detailViewModel.findDetailUser(login)
            }
            isFindDetailUserCalled = true
        }

        detailViewModel.detail.observe(this){
            setDetailUser(it.peekContent())
            detailViewModel.getFavoriteById(it.peekContent().id).observe(this@DetailActivity){ entity ->
                isFavorite = entity.isNotEmpty()
                if (entity.isNotEmpty()) binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(255, 50, 50))
                else binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this,intent.getStringExtra(EXTRA_LOGIN))
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        binding.fabFavorite.setOnClickListener {
            val user = detailViewModel.detail.value?.peekContent()?.id?.let { id ->
                detailViewModel.detail.value?.peekContent()?.login?.let { login ->
                    detailViewModel.detail.value?.peekContent()?.htmlUrl?.let { htmlUrl ->
                        detailViewModel.detail.value?.peekContent()?.avatarUrl?.let { avatarUrl ->
                            FavoriteEntity(id, login, htmlUrl, avatarUrl)
                        }
                    }
                }
            }
            if (user != null) {
                if (!isFavorite) {
                    detailViewModel.insert(user)
                }
                else {
                    detailViewModel.delete(user)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailUser(userData: DetailUserResponse) {
        following = userData.following
        followers = userData.followers
        binding.apply {
            tvLogin.text = userData.login
            Glide.with(root)
                .load(userData.avatarUrl)
                .into(ivProfile)
            tvFollowers.text = getString(R.string.followers_label,userData.followers)
            tvFollowing.text = getString(R.string.following_label,userData.following)
            tvName.text = userData.name
            topAppBar.title = userData.login
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        var following = 0
        var followers = 0
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}