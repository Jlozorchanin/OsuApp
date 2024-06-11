package com.example.osuapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProvider(private val context : Context){
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val first_launch = booleanPreferencesKey("isFirstLaunch")
        private val tokenDateOfGet = intPreferencesKey("tokenDate")
        private val token = stringPreferencesKey("token")
    }

    val getInfo : Flow<Boolean> = context.dataStore.data.map {
        it[first_launch] ?: true
    }
    val getTokenDate : Flow<Int> = context.dataStore.data.map {
        it[tokenDateOfGet] ?: 1718000000 // ToDO Change it
    }
    val getToken : Flow<String> = context.dataStore.data.map{
        it[token] ?: "aaa"// ToDO Change it
    }

    suspend fun saveInfo(value : Boolean){
        context.dataStore.edit {
            it[first_launch] = value
        }
    }

    suspend fun saveTokenAndTime(time : Int, tokenValue: String){
        context.dataStore.edit {
            it[tokenDateOfGet] = time
            it[token] = tokenValue
        }
    }
}
