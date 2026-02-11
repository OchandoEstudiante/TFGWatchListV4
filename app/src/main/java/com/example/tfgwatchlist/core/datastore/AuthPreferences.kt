package com.example.tfgwatchlist.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AuthPreferences {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val USER_TOKEN = stringPreferencesKey("user_token")
}