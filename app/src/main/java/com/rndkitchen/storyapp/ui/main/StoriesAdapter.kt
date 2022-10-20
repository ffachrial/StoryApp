package com.rndkitchen.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rndkitchen.storyapp.data.remote.response.DataStories
import com.rndkitchen.storyapp.databinding.StoryItemBinding

class StoriesAdapter(private val storyList: List<DataStories>): RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {
    inner class StoriesViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: DataStories) {
            with(binding) {
                tvName.text = story.name
                tvCreatedAt.text = story.createdAt

                Glide.with(ivItemPhoto.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(ivItemPhoto)
            }

            itemView.setOnClickListener {
                val intent = Intent(it.context, StoryDetailActivity::class.java)
                intent.putExtra("extra_detail", story)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesAdapter.StoriesViewHolder, position: Int) {
        storyList[position].let { story ->
            holder.bind(story)
        }
    }

    override fun getItemCount(): Int = storyList.size
}