package com.hye.mylibrary.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException


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

    override suspend fun addPreference(key: Preferences.Key<Int>, value: Int) {
        dataSource.edit { preferences ->
            val currentPoint = preferences[key] ?: 0
            preferences[key] = currentPoint + value
        }
    }


    /*
    * 단어 다운로드 관련
    * */
    val targetCode: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.TARGET_CODE, 0
    )

    suspend fun saveTargetCode(code: Int) {
        createPreference(PreferenceDataStoreConstants.TARGET_CODE, code)
    }


    suspend fun saveDocumentId(id: String) {
        createPreference(PreferenceDataStoreConstants.DOCUMENT_ID, id)
    }

    val documentId: Flow<String> = readPreference(
        PreferenceDataStoreConstants.DOCUMENT_ID, ""
    )


    /*
    * 오늘의 학습 관련
    *  targetWordCount : 학습할 단어 갯수
    *  currentWordCount: 현재 까지 학습한 단어 갯수
    * */
    val targetWordCount: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.TARGET_WORD_COUNT, 10
    )

    val currentWordCount: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.CURRENT_WORD_COUNT, 1
    )

    suspend fun saveTargetWordCount(count: Int) {
        createPreference(PreferenceDataStoreConstants.TARGET_WORD_COUNT, count)
    }

    suspend fun incrementCurrentWordCount() {
        val currentCount = currentWordCount.first()
        println("currentCount: $currentCount")
        println("targetWordCount: ${targetWordCount.first()}")
        if(currentCount >= 0 && currentCount < targetWordCount.first()) {
            addPreference(PreferenceDataStoreConstants.CURRENT_WORD_COUNT, 1)
        }
    }

    suspend fun resetDailyWordCount() {
        dataSource.edit {
            it[PreferenceDataStoreConstants.CURRENT_WORD_COUNT] = 0
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

    /*
    * pawPoint: 누적 학습 단어 갯수 1씩 증가
    * targetPoint : 누적 포인트 적립 2씩 증가
    *
    * */
    val pawPoint: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.PAW_POINT, 0
    )

    val targetPoint: Flow<Int> = readPreference(
        PreferenceDataStoreConstants.TARGET_POINT, 0
    )

    suspend fun addPawPoint() {
        addPreference(PreferenceDataStoreConstants.PAW_POINT, 1)
    }

    suspend fun addTargetPoint() {
        addPreference(PreferenceDataStoreConstants.TARGET_POINT, 2)
    }
}



