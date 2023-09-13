package com.github.yeetologist.githubusers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.yeetologist.githubusers.data.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.databinding.ItemSearchBinding

class FollowAdapter : ListAdapter<FollowUserResponseItem,FollowAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(searchResult: FollowUserResponseItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MyViewHolder (private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: FollowUserResponseItem){
            binding.tvUsername.text = searchResult.login
            Glide.with(binding.root)
                .load(searchResult.avatarUrl)
                .into(binding.ivProfile)
            binding.tvUrl.text = searchResult.htmlUrl
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowUserResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowUserResponseItem, newItem: FollowUserResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FollowUserResponseItem, newItem: FollowUserResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(result)
        }
    }

}