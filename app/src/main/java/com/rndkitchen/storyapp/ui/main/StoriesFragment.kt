package com.rndkitchen.storyapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rndkitchen.storyapp.data.remote.Result2
import com.rndkitchen.storyapp.databinding.FragmentStoriesBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory

class StoriesFragment : Fragment() {
    private var fragmentStoriesBinding: FragmentStoriesBinding? = null
    private val binding get() = fragmentStoriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStoriesBinding = FragmentStoriesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: StoriesViewModel by viewModels {
            factory
        }

        val storiesAdapter = FragmentStoriesAdapter()

//        if (tabName == TAB_STORIES) {
            val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXd0ckllejZlSWZWb1dvV3EiLCJpYXQiOjE2NjU4MDU2NzR9.K5ge2WSHJlT4oZ1AoeYudKlXJxBBygvkA2nPQKrJ9dA"

            viewModel.getStories(token).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result2.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result2.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            val storiesData = result.data
                            storiesAdapter.submitList(storiesData)
                            Toast.makeText(context, "Data berhasil diambil ", Toast.LENGTH_SHORT).show()
                        }
                        is Result2.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(context, "Terjadi kesalahan " + result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
//        }

        binding?.rvStories?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storiesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentStoriesBinding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_STORIES = "stories"
        const val TAB_ADD_STORY = "add_story"
    }
}