package com.luthfirr.sub1intermediate.di

import android.content.Context
import com.luthfirr.sub1intermediate.api.ApiConfig
import com.luthfirr.sub1intermediate.data.StoryRepository
import com.luthfirr.sub1intermediate.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val dataBase = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getRetrofitClientInstance()
        return StoryRepository(dataBase, apiService)
    }
}