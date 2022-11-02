package com.luthfirr.sub1intermediate.main.liststory

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.luthfirr.sub1intermediate.ModelPreference
import com.luthfirr.sub1intermediate.UserPreference
import com.luthfirr.sub1intermediate.api.response.ListStoryItem
import com.luthfirr.sub1intermediate.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListStoryViewModel(private val pref: UserPreference,private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>>{
        return storyRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun getToken(): LiveData<ModelPreference>{
        return pref.readDataStore.asLiveData()
    }

    fun clear(){
        viewModelScope.launch(Dispatchers.IO) {
            pref.clearToken()
        }
    }

}