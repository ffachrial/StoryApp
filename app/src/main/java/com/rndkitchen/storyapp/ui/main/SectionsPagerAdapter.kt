package com.rndkitchen.storyapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter internal constructor(activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = StoriesFragment()
        val bundle = Bundle()

        if (position == 0) {
            bundle.putString(StoriesFragment.ARG_TAB, StoriesFragment.TAB_STORIES)
        } else {
            bundle.putString(StoriesFragment.ARG_TAB, StoriesFragment.TAB_ADD_STORY)
        }

        fragment.arguments = bundle
        return fragment
    }
}