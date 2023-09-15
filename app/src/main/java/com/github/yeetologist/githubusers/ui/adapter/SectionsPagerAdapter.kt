package com.github.yeetologist.githubusers.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.yeetologist.githubusers.ui.DetailActivity
import com.github.yeetologist.githubusers.ui.FollowersFragment
import com.github.yeetologist.githubusers.ui.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private var stringExtra: String?) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> stringExtra?.let { FollowingFragment.newInstance(it,DetailActivity.following) }
            else -> stringExtra?.let { FollowersFragment.newInstance(it,DetailActivity.followers) }
        }
        return fragment as Fragment
    }
}