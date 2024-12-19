package com.appadore.machinetest.ui.main.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import com.appadore.machinetest.R
import com.appadore.machinetest.databinding.FragmentScheduleChallengeBinding
import com.appadore.machinetest.ui.base.BaseFragment
import com.appadore.machinetest.ui.main.FlagChallengeViewModel
import com.appadore.machinetest.utils.ChallengeReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

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
    }

    private fun observeData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                showSnackBar(it)
            }
        }
    }

    private fun onClicks(){
        binding?.saveButton?.setOnClickListener {
            val hours = ((binding?.hourBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                        (binding?.hourBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                        .toIntOrNull() ?: 0
            val minutes = ((binding?.minuteBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                        (binding?.minuteBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                        .toIntOrNull() ?: 0
            val seconds = ((binding?.secondBox1?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0") +
                        (binding?.secondBox2?.text?.toString()?.takeIf { it.isNotEmpty() } ?: "0"))
                        .toIntOrNull() ?: 0

            val totalTimeInSeconds = hours * 3600 + minutes * 60 + seconds
            Log.e("TAG", "onClicks: totalTimeInSeconds - $totalTimeInSeconds", )
            challengeStartTime = System.currentTimeMillis() + totalTimeInSeconds * 1000
            Log.e("TAG", "onClicks: $challengeStartTime", )
            //viewModel.setTimer(totalTimeInSeconds)
            //goToNextPage()
            //startCountdownTimer()
            scheduleChallenge(hours, minutes, seconds)
        }
    }

    private fun goToNextPage() {
        val pos = viewModel.getCurrentQuestionIndex()
        viewModel.getQuestionByIndex(pos)?.let {
            Log.e("TAG", "goToNextPage: ", )
            replaceFragment(FlagChallengeFragment.newInstance(it, pos), R.id.fragment_container)
        }
    }

    private fun startCountdownTimer() {
        val remainingTime = challengeStartTime - System.currentTimeMillis()

        /*if (remainingTime <= 20000) {
            // If the challenge is already less than 20 seconds away, show the message and start the timer immediately
            startChallengeIn20Seconds()
        } else {
            // Start the 20-second timer

        }*/
        countdownTimer = object : CountDownTimer(remainingTime /*- 20000*/, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = millisUntilFinished / 1000
                binding?.timerText?.text = "${if (timeRemaining < 10) "0$timeRemaining" else timeRemaining}"
                binding?.layoutTimerCount?.visibility = View.VISIBLE
                binding?.layoutTimeSetter?.visibility = View.GONE
            }

            override fun onFinish() {
                // Start the 20 seconds countdown
                //startChallengeIn20Seconds()
                startChallenge()
            }
        }
        countdownTimer?.start()
    }

    private fun startChallengeIn20Seconds() {
        // Countdown for the last 20 seconds
        countdownTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding?.timerText?.text = "${if (seconds < 10) "0$seconds" else seconds}"
                binding?.layoutTimerCount?.visibility = View.VISIBLE
                binding?.layoutTimeSetter?.visibility = View.GONE
            }

            override fun onFinish() {
                // Start the challenge after 20 seconds
                startChallenge()
            }
        }
        countdownTimer?.start()
    }

    private fun startChallenge() {
        // Navigate to the first question or display it
        goToNextPage()
    }

    private fun scheduleChallenge(hour: Int, minute: Int, second: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ChallengeReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the exact time
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}