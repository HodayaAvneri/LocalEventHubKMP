package com.localeventhub.app.featurebase.common

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

object PreferenceStorage {

    suspend fun saveData(dataStore: PrefsDataStore, key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getData(dataStore: PrefsDataStore, key: String): String? {
        return dataStore.data.map { it[stringPreferencesKey(key)] }.firstOrNull()
    }

    suspend fun clearData(dataStore: PrefsDataStore){
        dataStore.edit { it.clear() }
    }
}