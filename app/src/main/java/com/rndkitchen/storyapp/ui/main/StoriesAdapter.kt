package com.rndkitchen.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rndkitchen.storyapp.data.remote.response.DataStories
import com.rndkitchen.storyapp.databinding.StoryItemBinding

class StoriesAdapter(private val storyList: List<DataStories>): RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {
    inner class StoriesViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: DataStories) {
            with(binding) {
                tvName.text = story.name
                tvDetailDescription.text = story.description
                tvCreatedAt.text = story.createdAt

                Glide.with(ivItemPhoto.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(ivItemPhoto)
            }

            itemView.setOnClickListener {
                val intent = Intent(it.context, StoryDetailActivity::class.java)
                intent.putExtra("extra_detail", story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "profile"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDetailDescription, "description")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
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