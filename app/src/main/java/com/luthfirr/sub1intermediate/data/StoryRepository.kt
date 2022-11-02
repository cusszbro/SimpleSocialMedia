package com.luthfirr.sub1intermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.luthfirr.sub1intermediate.api.ApiService
import com.luthfirr.sub1intermediate.api.response.ListStoryItem
import com.luthfirr.sub1intermediate.database.StoryDatabase

class StoryRepository (private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStories(token : String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}