package com.rndkitchen.storyapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.databinding.StoryItemBinding
import com.rndkitchen.storyapp.ui.main.FragmentStoriesAdapter.StoriesViewHolder

class FragmentStoriesAdapter(): ListAdapter<StoriesEntity, StoriesViewHolder>(DIFF_CALLBACK) {
    inner class StoriesViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoriesEntity) {
            with(binding) {
                tvName.text = story.name
                tvCreatedAt.text = story.createdAt

                Glide.with(imgStory.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(imgStory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentStoriesAdapter.StoriesViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

//    override fun getItemCount(): Int = 0

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<StoriesEntity> =
            object : DiffUtil.ItemCallback<StoriesEntity>() {
                override fun areItemsTheSame(oldUser: StoriesEntity, newUser: StoriesEntity): Boolean {
                    return oldUser.name == newUser.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: StoriesEntity, newUser: StoriesEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}