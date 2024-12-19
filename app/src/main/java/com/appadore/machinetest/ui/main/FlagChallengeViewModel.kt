package com.appadore.machinetest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appadore.machinetest.data.model.Question
import com.appadore.machinetest.data.repository.FlagChallengeRepository
import com.appadore.machinetest.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlagChallengeViewModel @Inject constructor(private val _repository: FlagChallengeRepository) :
    BaseViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    fun loadQuestions() {
        _questions.value = _repository.getQuestionsFromJson()
    }

    fun getQuestionByIndex(pos: Int): Question? {
        return _questions.value?.get(pos)
    }

    fun getTotalQuestions(): Int {
        return _questions.value?.size ?: 0
    }

    fun getCurrentQuestionIndex (): Int {
        return _repository.getCurrentQuestionIndex()
    }

    fun setCurrentQuestionIndex (index: Int) {
        _repository.setCurrentQuestionIndex(index)
    }

    fun updateTotalScore() {
        _repository.updateTotalScore()
    }

    fun getTotalScore (): Int {
        return _repository.getTotalScore()
    }

    fun getChallengeStartTime (): Long {
        return _repository.getChallengeStartTime()
    }

    fun setChallengeStartTime (value: Long) {
        _repository.setChallengeStartTime(value)
    }
}