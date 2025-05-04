package com.hye.mylibrary.data.preferences

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow


interface ICRUDPreferenceDataStore{
    fun <T> readPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> createPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> deletePreference(key: Preferences.Key<T>)
    suspend fun <T> clearAllPreference()
    suspend fun  addPreference(key: Preferences.Key<Int>, value: Int)
}
