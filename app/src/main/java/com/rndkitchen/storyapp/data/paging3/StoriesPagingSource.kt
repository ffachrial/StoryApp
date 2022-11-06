package com.rndkitchen.storyapp.data.paging3

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rndkitchen.storyapp.data.remote.response.StoryResponse
import com.rndkitchen.storyapp.data.remote.retrofit.ApiService
import java.lang.Exception

class StoriesPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, StoryResponse>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesPaging(token, position, params.loadSize)


            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}