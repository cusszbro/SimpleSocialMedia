package com.luthfirr.sub1intermediate.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luthfirr.sub1intermediate.ModelPreference
import com.luthfirr.sub1intermediate.UserPreference

class UploadViewModel(private val pref: UserPreference): ViewModel() {

    fun getToken(): LiveData<ModelPreference> {
        return pref.readDataStore.asLiveData()
    }
}