package com.appadore.machinetest.data.repository

import android.content.Context
import com.appadore.machinetest.data.local.DataStoreHelper
import com.appadore.machinetest.data.model.FlagChallenge
import com.appadore.machinetest.data.model.Question
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FlagChallengeRepository @Inject constructor(
    private val _dataStoreHelper: DataStoreHelper,
    private val _context: Context
) {

    fun setCurrentQuestionIndex(pos: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _dataStoreHelper.setCurrentQuestionIndex(pos)
        }

    }

    fun getCurrentQuestionIndex(): Int {
        return _dataStoreHelper.getCurrentQuestionIndex()
    }

    fun updateTotalScore() {
        CoroutineScope(Dispatchers.IO).launch {
            _dataStoreHelper.updateTotalScore()
        }
    }

    fun getTotalScore(): Int {
        return _dataStoreHelper.getTotalScore()
    }

    fun setChallengeStartTime(value: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _dataStoreHelper.setChallengeStartTime(value)
        }

    }

    fun getChallengeStartTime(): Long {
        return _dataStoreHelper.getChallengeStartTime()
    }

    fun getQuestionsFromJson(): List<Question> {
        val jsonString: String = try {
            val inputStream = _context.assets.open("questions.json")
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }

        return parseJsonToQuestions(jsonString)
    }

    private fun parseJsonToQuestions(jsonString: String): List<Question> {
        return try {
            val gson = Gson()
            val response = gson.fromJson(jsonString, FlagChallenge::class.java)
            response.questions
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}