package com.luthfirr.sub1intermediate

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveToken(user: ModelPreference) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[STATE_KEY] = user.state
            preferences[TOKEN_KEY] = user.token
            preferences[ID_KEY] = user.id
        }
    }

    val readDataStore : Flow<ModelPreference> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("error", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {
            val name = it[NAME_KEY] ?: ""
            val token = it[TOKEN_KEY] ?: ""
            val userid = it[ID_KEY] ?: ""
            val state = it[STATE_KEY] ?: false
            ModelPreference(name = name, id = userid,token = token, state = state)
        }

    suspend fun clearToken() {
        dataStore.edit {
            it.clear()
        }
    }


    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_KEY = stringPreferencesKey("id")
        private val STATE_KEY = booleanPreferencesKey("error")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}