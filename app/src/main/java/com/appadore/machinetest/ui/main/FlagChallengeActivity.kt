package com.appadore.machinetest.ui.main

import com.appadore.machinetest.R
import com.appadore.machinetest.databinding.ActivityFlagChallengeBinding
import com.appadore.machinetest.ui.base.BaseActivity
import com.appadore.machinetest.ui.main.fragments.FlagChallengeFragment
import com.appadore.machinetest.ui.main.fragments.ResultChallengeFragment
import com.appadore.machinetest.ui.main.fragments.ScheduleChallengeFragment
import com.appadore.machinetest.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlagChallengeActivity : BaseActivity<FlagChallengeViewModel, ActivityFlagChallengeBinding>() {

    override fun getLayout() = R.layout.activity_flag_challenge

    override fun getViewModelType() = FlagChallengeViewModel::class.java

    override fun bindViews() {
        viewModel.loadQuestions()
        viewModel.questions.observe(this) {
            initUI()
        }
    }

    private fun initUI() {

        var currentQuestionIndex = viewModel.getCurrentQuestionIndex()
        val challengeStartTime = viewModel.getChallengeStartTime()
        val remainingScheduleTime = challengeStartTime - System.currentTimeMillis()
        val elapsedTime = challengeStartTime + getTotalChallengeTime()
        val elapsedTimeRemaining = System.currentTimeMillis() - elapsedTime

        if (elapsedTimeRemaining < 0) {
            currentQuestionIndex = getCurrentQuestionIndexByElapsedTime(challengeStartTime)
            //Log.e(TAG, "initUI: currentQuestionIndexByElapsedTime - $currentQuestionIndex", )
        }

        val isChallengeCompleted = viewModel.getTotalQuestions() == currentQuestionIndex + 1

        // Calculate remaining time for the current question
        val remainingTimeForCurrentQuestion =
            getRemainingTimeForCurrentQuestion(challengeStartTime, currentQuestionIndex)
        //Log.e(TAG, "initUI: currentQuestionIndex - $currentQuestionIndex", )
        //Log.e(TAG, "initUI: challengeStartTime - $challengeStartTime", )
        //Log.e(TAG, "initUI: remainingScheduleTime - $remainingScheduleTime", )
        //Log.e(TAG, "initUI: elapsedTime - $elapsedTime", )
        //Log.e(TAG, "initUI: elapsedTimeRemaining - $elapsedTimeRemaining", )
        //Log.e(TAG, "initUI: remainingTimeForCurrentQuestion - $remainingTimeForCurrentQuestion", )
        //Log.e(TAG, "initUI: isChallengeCompleted - $isChallengeCompleted", )


        when {
            remainingScheduleTime > 0 || currentQuestionIndex == -1 ->
                replaceFragment(ScheduleChallengeFragment.newInstance(), R.id.fragment_container)

            isChallengeCompleted ->
                replaceFragment(ResultChallengeFragment.newInstance(), R.id.fragment_container)

            else -> viewModel.getQuestionByIndex(currentQuestionIndex)?.let {
                replaceFragment(
                    FlagChallengeFragment.newInstance(
                        it,
                        currentQuestionIndex,
                        remainingTimeForCurrentQuestion
                    ), R.id.fragment_container
                )
            }
        }
    }


    private fun getCurrentQuestionIndexByElapsedTime(challengeStartTime: Long): Int {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - challengeStartTime
        val questionIndex = (elapsedTime / getTotalTimeForEachQuestion()).toInt()
        return questionIndex.coerceAtMost(viewModel.getTotalQuestions())
    }

    private fun getTotalChallengeTime(): Long {
        val totalQuestions = viewModel.getTotalQuestions()
        val totalChallengeTime = getTotalTimeForEachQuestion() * totalQuestions
        return totalChallengeTime
    }

    private fun getTotalTimeForEachQuestion(): Long {
        return AppConstants.QUESTION_INTERVAL + AppConstants.QUESTION_TIMEOUT_INTERVAL
    }

    private fun getRemainingTimeForCurrentQuestion(
        challengeStartTime: Long,
        currentQuestionIndex: Int
    ): Long {
        val elapsedTime = System.currentTimeMillis() - challengeStartTime
        val timeSpentForPreviousQuestions = currentQuestionIndex * getTotalTimeForEachQuestion()
        val remainingTimeForCurrentQuestion =
            getTotalTimeForEachQuestion() - (elapsedTime - timeSpentForPreviousQuestions)
        return remainingTimeForCurrentQuestion.coerceAtLeast(0)
    }

}