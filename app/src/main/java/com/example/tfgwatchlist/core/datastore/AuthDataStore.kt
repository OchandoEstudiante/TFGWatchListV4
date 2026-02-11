package com.example.tfgwatchlist.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

class AuthDataStore(private val context: Context){

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[AuthPreferences.IS_LOGGED_IN] ?: false
        }

    suspend fun saveLoginState(isLogged: Boolean){
        context.dataStore.edit { preferences ->
            preferences[AuthPreferences.IS_LOGGED_IN] = isLogged
        }
    }

    suspend fun saveToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[AuthPreferences.USER_TOKEN] = token
        }
    }

    //Obtener token
    val userToken: Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[AuthPreferences.USER_TOKEN]
        }

    //Cerrar sesión
    suspend fun clearSession(){
        context.dataStore.edit{ preferences ->
            preferences.clear()
        }
    }
}