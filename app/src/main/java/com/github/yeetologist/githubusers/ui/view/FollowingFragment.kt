package com.github.yeetologist.githubusers.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.yeetologist.githubusers.data.remote.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.databinding.FragmentFollowingBinding
import com.github.yeetologist.githubusers.ui.adapter.FollowAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.FollowViewModel

private const val ARG_LOGIN = "arg_login"
private const val ARG_INDEX = "arg_index"


class FollowingFragment : Fragment() {

    private var argLogin: String? = null
    private var argIndex: Int? = null
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding
    private val followingViewModel by viewModels<FollowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argLogin = it.getString(ARG_LOGIN)
            argIndex = it.getInt(ARG_INDEX,0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        if (!followingViewModel.isFunctionExecuted) {
            argLogin?.let { followingViewModel.findFollowing(it) }
            followingViewModel.isFunctionExecuted = true
        }

        followingViewModel.listFollowing.observe(viewLifecycleOwner){
            setListUsers(it.peekContent())
        }
        followingViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvUsers?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvUsers?.addItemDecoration(itemDecoration)

        binding?.rvUsers?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var nextPage = 2
            var isLoading = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoading = true
                    if (DetailActivity.following > 30) {
                        argLogin?.let { followingViewModel.findFollowing(it, nextPage) }
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

    private fun setListUsers(items: List<FollowUserResponseItem>?) {
        val adapter = FollowAdapter()
        adapter.submitList(items)
        binding?.rvUsers?.adapter = adapter
        adapter.setOnItemClickListener(object : FollowAdapter.OnItemClickListener{
            override fun onItemClick(searchResult: FollowUserResponseItem) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LOGIN,searchResult.login)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGIN, param1)
                    putInt(ARG_INDEX, param2)
                }
            }
    }

}