package com.appadore.machinetest.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.appadore.machinetest.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class DataStoreHelper @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun setCurrentQuestionIndex(text: Int) {
        storeInt(KEY_CURRENT_QUESTION_INDEX, text)
    }

    fun getCurrentQuestionIndex(): Int {
        return getStoredInt(KEY_CURRENT_QUESTION_INDEX, -1)
    }

    suspend fun updateTotalScore() {
        storeInt(KEY_TOTAL_SCORE, getTotalScore() + 1)
    }

    fun getTotalScore(): Int {
        return getStoredInt(KEY_TOTAL_SCORE, 0)
    }

    suspend fun setChallengeStartTime(text: Long) {
        storeLong(KEY_CHALLENGE_START_TIME, text)
    }

    fun getChallengeStartTime(): Long {
        return getStoredLong(KEY_CHALLENGE_START_TIME, 0)
    }

    /*--------------------------------------------------------------------------------------------*/
    private suspend fun storeString(tag: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(tag)] = value
        }
    }

    private suspend fun storeFloat(tag: String, value: Float) {
        context.dataStore.edit { preferences ->
            preferences[floatPreferencesKey(tag)] = value
        }
    }

    private suspend fun storeLong(tag: String, value: Long) {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(tag)] = value
        }
    }


    private suspend fun storeBoolean(tag: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(tag)] = value
        }
    }

    private suspend fun storeInt(tag: String, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(tag)] = value
        }
    }

    fun getBoolean(tag: String): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(tag)]
        }
    }

    private fun getStoredBoolean(tag: String, default: Boolean = false): Boolean {
        var value = false
        runBlocking {
            value = context.dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(tag)]
            }.first() ?: default
        }

        return value
    }

    private fun getStoredString(tag: String, default: String = ""): String {
        var value = ""
        runBlocking {
            value = context.dataStore.data.map { preferences ->
                preferences[stringPreferencesKey(tag)]
            }.first() ?: default
        }

        return value
    }


    private fun getStoredFloat(tag: String, default: Float = 0f): Float {
        var value = 0f
        runBlocking {
            value = context.dataStore.data.map { preferences ->
                preferences[floatPreferencesKey(tag)]
            }.first() ?: default
        }
        return value
    }

    private fun getStoredInt(tag: String, default: Int = -1): Int {
        var value = -1
        runBlocking {
            value = context.dataStore.data.map { preferences ->
                preferences[intPreferencesKey(tag)]
            }.first() ?: default
        }

        return value
    }

    private fun getStoredLong(tag: String, default: Long = -1L): Long {
        var value = -1L
        runBlocking {
            value = context.dataStore.data.map { preferences ->
                preferences[longPreferencesKey(tag)] ?: default
            }.first()
        }
        return value

    }


    companion object {
        const val KEY_CURRENT_QUESTION_INDEX = "current_question_index"
        const val KEY_TOTAL_SCORE = "total_score"
        const val KEY_CHALLENGE_START_TIME = "challenge_start_time"
    }
}