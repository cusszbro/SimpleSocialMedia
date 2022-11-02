package com.luthfirr.sub1intermediate.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luthfirr.sub1intermediate.api.ApiService
import com.luthfirr.sub1intermediate.api.response.ListStoryItem

class StoryPagingSource(private val token: String, private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.getStoriesPaging3(
                "Bearer $token",
                position,
                params.loadSize
                ).listStory as List<ListStoryItem>

            Log.e("paging source", "${responseData}")
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}