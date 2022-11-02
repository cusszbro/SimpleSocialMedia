package com.luthfirr.sub1intermediate.login

import android.util.Log
import androidx.lifecycle.*
import com.luthfirr.sub1intermediate.Event
import com.luthfirr.sub1intermediate.ModelPreference
import com.luthfirr.sub1intermediate.UserPreference
import com.luthfirr.sub1intermediate.api.*
import com.luthfirr.sub1intermediate.api.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    val userLogin = MutableLiveData<LoginResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun setLogin(email: String, password: String) {
        _isLoading.value = true
        val retro = ApiConfig.getRetrofitClientInstance()
        retro.postLogin(email, password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error != true) {
                        userLogin.postValue(response.body())
                        _snackBarText.value = Event(response.body()?.message.toString())
                        Log.e(TAG, "Connection success data is valid")
                    }
                    else {
                        Log.e(TAG, response.message())
                    }
                }
                else {
                    _snackBarText.value = Event("Sorry your Email or Password maybe wrong")
                    Log.e(TAG, response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _snackBarText.value = Event("Sorry, Please connect your internet")
                Log.e(TAG, "error in failure ${t.message.toString()}")
            }

        })
    }

    fun getLogin(): LiveData<LoginResponse> {
        return userLogin
    }

    fun saveToken(user: ModelPreference) {
        viewModelScope.launch {
            pref.saveToken(user)
        }
    }

    fun getToken() : LiveData<ModelPreference>{
        return pref.readDataStore.asLiveData()
    }


    companion object {
        private const val TAG = "LoginViewModel"
    }

}
