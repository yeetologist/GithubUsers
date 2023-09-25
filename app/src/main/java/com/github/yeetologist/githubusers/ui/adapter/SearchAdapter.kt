package com.github.yeetologist.githubusers.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.yeetologist.githubusers.data.remote.response.ItemsItem
import com.github.yeetologist.githubusers.databinding.ItemSearchBinding

class SearchAdapter : ListAdapter<ItemsItem, SearchAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(searchResult: ItemsItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MyViewHolder (private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: ItemsItem){
            binding.tvUsername.text = searchResult.login
            Glide.with(binding.root)
                .load(searchResult.avatarUrl)
                .into(binding.ivProfile)
            binding.tvUrl.text = searchResult.htmlUrl
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
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
