package com.appadore.machinetest.ui.main

import android.util.Log
import com.appadore.machinetest.R
import com.appadore.machinetest.databinding.ActivityFlagChallengeBinding
import com.appadore.machinetest.ui.base.BaseActivity
import com.appadore.machinetest.ui.main.fragments.FlagChallengeFragment
import com.appadore.machinetest.ui.main.fragments.ResultChallengeFragment
import com.appadore.machinetest.ui.main.fragments.ScheduleChallengeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlagChallengeActivity : BaseActivity<FlagChallengeViewModel, ActivityFlagChallengeBinding>() {

    override fun getLayout() = R.layout.activity_flag_challenge

    override fun getViewModelType() = FlagChallengeViewModel::class.java

    override fun bindViews() {
        viewModel.loadQuestions()
        //initUI()
        viewModel.questions.observe(this) {
            initUI()
        }
    }

    private fun initUI() {
        //val currentQuestionIndex = viewModel.getCurrentQuestionIndex()
        val currentQuestionIndex = 0
        if (currentQuestionIndex == 0) {
            replaceFragment(ScheduleChallengeFragment.newInstance(), R.id.fragment_container)
        } else if (viewModel.getTotalQuestions() == currentQuestionIndex + 1) {
            replaceFragment(ResultChallengeFragment.newInstance(), R.id.fragment_container)
        } else {
            viewModel.getQuestionByIndex(currentQuestionIndex)?.let {
                replaceFragment(
                    FlagChallengeFragment.newInstance(it, currentQuestionIndex),
                    R.id.fragment_container
                )
            }
        }
    }


}