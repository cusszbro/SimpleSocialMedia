package com.luthfirr.sub1intermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luthfirr.sub1intermediate.addstory.UploadViewModel
import com.luthfirr.sub1intermediate.di.Injection
import com.luthfirr.sub1intermediate.main.liststory.ListStoryViewModel
import com.luthfirr.sub1intermediate.login.LoginViewModel
import com.luthfirr.sub1intermediate.main.mapstory.MapsViewModel

class ViewModelFactory(private val pref: UserPreference,private val context: Context? = null) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(pref, Injection.provideRepository(context!!)) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(pref) as T
            }modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}