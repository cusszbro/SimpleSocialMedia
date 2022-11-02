package com.luthfirr.sub1intermediate.main.mapstory

import android.util.Log
import androidx.lifecycle.*
import com.luthfirr.sub1intermediate.ModelPreference
import com.luthfirr.sub1intermediate.UserPreference
import com.luthfirr.sub1intermediate.api.ApiConfig
import com.luthfirr.sub1intermediate.api.response.ListStoryItem
import com.luthfirr.sub1intermediate.api.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference) : ViewModel() {

    val mapStories = MutableLiveData<ArrayList<ListStoryItem>>()

    fun setMapStories(token: String) {
        val retro = ApiConfig.getRetrofitClientInstance()
        retro.getStories(token = "Bearer $token").enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    mapStories.postValue(response.body()?.listStory!!)
                    Log.e(TAG, "Connection success data Maps is valid")
                }
                else {
                    Log.e(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(TAG, "error : ${t.message}")
            }
        })
    }

    fun getMapStories(): LiveData<ArrayList<ListStoryItem>> {
        return mapStories
    }


    fun getToken(): LiveData<ModelPreference>{
        return pref.readDataStore.asLiveData()
    }

    companion object {
        const val TAG = "MapsViewModel"
    }
}