package com.github.yeetologist.githubusers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yeetologist.githubusers.data.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.databinding.FragmentFollowingBinding
import com.github.yeetologist.githubusers.ui.adapter.FollowAdapter
import com.github.yeetologist.githubusers.ui.viewmodel.FollowViewModel

private const val ARG_LOGIN = "arg_login"
private const val ARG_INDEX = "arg_index"

class FollowFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LOGIN, param1)
                    putInt(ARG_INDEX, param2)
                }
            }
    }

    private var argLogin: String? = null
    private var argIndex: Int? = null
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()

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

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        followViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        if (argLogin != null) {
            if (argIndex == 0){ followViewModel.findFollowing(argLogin!!) }
            else followViewModel.findFollowers(argLogin!!)
        }

        followViewModel.listUsers.observe(viewLifecycleOwner){
            setListUsers(it)
        }
    }

    private fun setListUsers(items: List<FollowUserResponseItem>?) {
        val adapter = FollowAdapter()
        adapter.submitList(items)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}