package com.appadore.machinetest.ui.main.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.appadore.machinetest.R
import com.appadore.machinetest.databinding.FragmentScheduleChallengeBinding
import com.appadore.machinetest.ui.base.BaseFragment
import com.appadore.machinetest.ui.main.FlagChallengeViewModel
import com.appadore.machinetest.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleChallengeFragment :
    BaseFragment<FlagChallengeViewModel, FragmentScheduleChallengeBinding>() {

    private var challengeStartTime: Long = 0
    private var countdownTimer: CountDownTimer? = null

    companion object {

        fun newInstance(): ScheduleChallengeFragment {
            val fragment = ScheduleChallengeFragment()
            return fragment
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentScheduleChallengeBinding.inflate(inflater)

    override fun getViewModelType() = FlagChallengeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        setIsSharedViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        onClicks()
        initUI()
    }

    private fun initUI() {
        challengeStartTime = viewModel.getChallengeStartTime()
        val remainingTime = challengeStartTime - System.currentTimeMillis()
        if (remainingTime > 0) {
            initCountdownTimer()
        }
    }

    private fun observeData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                showSnackBar(it)
            }
        }
    }

    private fun onClicks() {
        binding?.saveButton?.setOnClickListener {
            val hours = ((binding?.hourBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                    (binding?.hourBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                .toIntOrNull() ?: 0
            val minutes =
                ((binding?.minuteBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                        (binding?.minuteBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                    .toIntOrNull() ?: 0
            val seconds =
                ((binding?.secondBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                        (binding?.secondBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                    .toIntOrNull() ?: 0

            val totalTimeInSeconds = hours * 3600 + minutes * 60 + seconds

            if (totalTimeInSeconds < 20) {
                showSnackBar("Minimum time should be 20 seconds")
                return@setOnClickListener
            }

            challengeStartTime = System.currentTimeMillis() + totalTimeInSeconds * 1000
            viewModel.setChallengeStartTime(challengeStartTime)
            initCountdownTimer()
        }
    }

    private fun initCountdownTimer() {
        val remainingTime = challengeStartTime - System.currentTimeMillis()

        if (remainingTime > 20000) {
            binding?.layoutTimerCount?.visibility = View.VISIBLE
            binding?.layoutTimeSetter?.visibility = View.GONE
            binding?.timerText?.text = AppUtils.convertMillisToTimeFormat(challengeStartTime)
        }

        startCountdownTimer(remainingTime)
    }

    private fun startCountdownTimer(remainingTime: Long) {
        countdownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = millisUntilFinished / 1000
                if (timeRemaining <= 20) {
                    binding?.timerText?.text =
                        "${if (timeRemaining < 10) "0$timeRemaining" else timeRemaining}"
                    binding?.layoutTimerCount?.visibility = View.VISIBLE
                    binding?.layoutTimeSetter?.visibility = View.GONE
                }
            }

            override fun onFinish() {
                startChallenge()
            }
        }
        countdownTimer?.start()
    }

    private fun startChallenge() {
        viewModel.getQuestionByIndex(0)?.let {
            showSnackBar("Challenge Started!")
            replaceFragment(FlagChallengeFragment.newInstance(it, 0), R.id.fragment_container)
        }
    }

}