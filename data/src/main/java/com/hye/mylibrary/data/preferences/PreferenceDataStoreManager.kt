package com.hye.sesac.klangpj.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.hye.mylibrary.data.preferences.PreferenceDataStoreConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar


val Context.jetpackDataStore by preferencesDataStore(
    name = "jetpack_preferences"
)

class PreferenceDataStoreManager(context: Context) : ICRUDPreferenceDataStore {

    private val dataSource = context.jetpackDataStore

    override fun <T> readPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataSource.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())

            } else {
                throw exception

            }

        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result

        }

    override suspend fun <T> createPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> deletePreference(key: Preferences.Key<T>) {
        dataSource.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun <T> clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }

    //단어 다운로드 관련
    suspend fun saveTargetCode(code: Int) {
        createPreference(PreferenceDataStoreConstants.TARGET_CODE, code)
    }
    val targetCode: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.TARGET_CODE, 0
    )
    suspend fun saveDocumentId(id: String) {
        createPreference(PreferenceDataStoreConstants.DOCUMENT_ID, id)
    }
    val documentId: Flow<String> = readPreference(
        PreferenceDataStoreConstants.DOCUMENT_ID, ""
    )

    //단어 학습 관련
    suspend fun saveTargetWordCount(count: Int) {
        createPreference(PreferenceDataStoreConstants.TARGET_WORD_COUNT, count)
    }

    val targetWordCount: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.TARGET_WORD_COUNT, 10
    )


    suspend fun saveCurrentWordCount(count: Int) {
        createPreference(PreferenceDataStoreConstants.CURRENT_WORD_COUNT, count)
    }

    suspend fun incrementCurrentWordCount() {
        dataSource.edit { preferences ->
            val currentCount = preferences[PreferenceDataStoreConstants.CURRENT_WORD_COUNT] ?: 0
            preferences[PreferenceDataStoreConstants.CURRENT_WORD_COUNT] = currentCount + 1
        }
    }

    suspend fun decrementCurrentWordCount() {
        dataSource.edit { preferences ->
            val currentCount = preferences[PreferenceDataStoreConstants.CURRENT_WORD_COUNT] ?: 0
            if (currentCount > 0) {
                preferences[PreferenceDataStoreConstants.CURRENT_WORD_COUNT] = currentCount - 1
            }


        }
    }

    val currentWordCount: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.CURRENT_WORD_COUNT, 0
    )


    //포인트
    suspend fun savePawPoint(point: Int) {
        createPreference(PreferenceDataStoreConstants.PAW_POINT, point)
    }

    suspend fun addPawPoint(point: Int = 5) {
        dataSource.edit { preferences ->
            val currentPoint = preferences[PreferenceDataStoreConstants.PAW_POINT] ?: 0
            preferences[PreferenceDataStoreConstants.PAW_POINT] = currentPoint + point
        }
    }

    suspend fun saveTargetPoint(point: Int) {
        createPreference(PreferenceDataStoreConstants.TARGET_POINT, point)
    }

    suspend fun addTargetPoint(point: Int = 10) {
        dataSource.edit { preferences ->
            val currentPoint = preferences[PreferenceDataStoreConstants.TARGET_POINT] ?: 0
            preferences[PreferenceDataStoreConstants.TARGET_POINT] = currentPoint + point
        }

    }
}



