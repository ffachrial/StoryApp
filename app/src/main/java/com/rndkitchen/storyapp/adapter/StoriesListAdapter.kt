package com.rndkitchen.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rndkitchen.storyapp.data.remote.response.StoryResponse
import com.rndkitchen.storyapp.databinding.StoryItemBinding
import com.rndkitchen.storyapp.ui.main.StoryDetailActivity

class StoriesListAdapter : PagingDataAdapter<StoryResponse, StoriesListAdapter.StoriesViewHolder>(
    DIFF_CALLBACK
) {
    class StoriesViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryResponse) {
            binding.tvName.text = data.name
            binding.tvDetailDescription.text = data.description

            Glide.with(binding.ivItemPhoto.context)
                .load(data.photoUrl)
                .centerCrop()
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(it.context, StoryDetailActivity::class.java)
                intent.putExtra("extra_detail", data)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(binding.ivItemPhoto, "profile"),
                        androidx.core.util.Pair(binding.tvName, "name"),
                        androidx.core.util.Pair(binding.tvDetailDescription, "description")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
            override fun areItemsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}