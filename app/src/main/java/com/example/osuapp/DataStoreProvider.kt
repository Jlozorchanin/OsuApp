package com.example.osuapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProvider(private val context : Context){
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val tokenMissing = booleanPreferencesKey("isMissing")
        private val tokenDateOfGet = intPreferencesKey("tokenDate")
        private val token = stringPreferencesKey("token")
        private val refreshToken = stringPreferencesKey("refreshToken")
    }

    val getInfo : Flow<Boolean> = context.dataStore.data.map {
        it[tokenMissing] ?: true
    }

    val getToken : Flow<String> = context.dataStore.data.map {
        it[token] ?: ""
    }
    val getTokenTime : Flow<Int> = context.dataStore.data.map {
        it[tokenDateOfGet] ?: -1
    }
    val getRefreshToken : Flow<String> = context.dataStore.data.map {
        it[refreshToken] ?: ""
    }



    suspend fun saveInfo(value : Boolean){
        context.dataStore.edit {
            it[tokenMissing] = value
        }
    }

    suspend fun saveBaseData(time : Int, tokenValue: String,refreshTokenValue: String){
        context.dataStore.edit {
            it[tokenDateOfGet] = time
            it[token] = tokenValue
            it[refreshToken] = refreshTokenValue
        }
    }
}
