package com.github.yeetologist.githubusers.ui

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
import com.github.yeetologist.githubusers.data.response.ItemsItem
import com.github.yeetologist.githubusers.databinding.ActivityMainBinding
import com.github.yeetologist.githubusers.ui.adapter.SearchAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var query: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query", "")
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.findUser(searchBar.text.toString())
                    query = textView.text.toString()
                    false
                }
        }

        mainViewModel.listUsers.observe(this){
            setListUsers(it)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.resultCount.observe(this){
            if (it == 0) Snackbar.make(binding.root, "User not found", Snackbar.LENGTH_LONG).show()
        }

        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var currentPage = 2
            var isLoading = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true
                    if (mainViewModel.resultCount.value!! > 30) {
                        mainViewModel.findUser(query, currentPage)
                    }
                    currentPage++
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the query to savedInstanceState to handle rotation
        outState.putString("query", query)
    }

    private fun setListUsers(items: List<ItemsItem>?) {
        val adapter = SearchAdapter()
        adapter.submitList(items)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(searchResult: ItemsItem) {
                val intent = Intent(this@MainActivity,DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LOGIN, searchResult.login)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}