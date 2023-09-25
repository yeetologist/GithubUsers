package com.github.yeetologist.githubusers.ui.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.data.response.ItemsItem
import com.github.yeetologist.githubusers.databinding.ActivityMainBinding
import com.github.yeetologist.githubusers.ui.adapter.SearchAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var query: String = ""
    private var userCount: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query", "")
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        mainViewModel.listUsers.observe(this){
            setListUsers(it.peekContent())
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.resultCount.observe(this) {
            it.getContentIfNotHandled().let {sum ->
                if (sum == 0) Snackbar.make(binding.root, "User not found", Snackbar.LENGTH_LONG)
                    .show()
            }
            userCount = it.peekContent()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings_menu -> {
                    startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
                    true
                }
                R.id.favorite_menu -> {
                    startActivity(Intent(this@MainActivity,FavoriteActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("query", query)
    }

    private fun setupRecyclerView() {
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

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.findUser(searchBar.text.toString())
                    query = textView.text.toString()
                    false
                }
        }

        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var nextPage = 2
            var isLoading = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true
                    if (userCount?.let { it > 30 } == true) {
                        mainViewModel.findUser(query, nextPage)
                    }
                    nextPage++
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isLoading = false
                }
            }
        })
    }

    private fun setListUsers(items: List<ItemsItem>?) {
        val adapter = SearchAdapter()
        adapter.submitList(items)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(searchResult: ItemsItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LOGIN, searchResult.login)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}