package com.github.yeetologist.githubusers.ui.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity
import com.github.yeetologist.githubusers.databinding.ActivityFavoriteBinding
import com.github.yeetologist.githubusers.ui.adapter.FavoriteAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.FavoriteViewModel
import com.github.yeetologist.githubusers.ui.viewmodel.FavoriteViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = FavoriteViewModelFactory.getInstance(application)
        favoriteViewModel = ViewModelProvider(this@FavoriteActivity,factory)[FavoriteViewModel::class.java]

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val layoutManager =
            if (applicationContext.resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        favoriteViewModel.getAllFavorites().observe(this){
            if (it != null){
                setListUsers(it)
            }
            if (it.isEmpty()){
                Snackbar.make(binding.root, "User not found", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setListUsers(items: List<FavoriteEntity>) {
        val adapter = FavoriteAdapter()
        adapter.submitList(items)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(searchResult: FavoriteEntity) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LOGIN, searchResult.login)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_option_menu -> {
                startActivity(Intent(this@FavoriteActivity,SettingsActivity::class.java))
                true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}