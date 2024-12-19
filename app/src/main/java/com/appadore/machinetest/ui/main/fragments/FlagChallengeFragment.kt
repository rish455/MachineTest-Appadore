package com.appadore.machinetest.ui.main.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.appadore.machinetest.R
import com.appadore.machinetest.data.model.Country
import com.appadore.machinetest.data.model.Question
import com.appadore.machinetest.databinding.FragmentFlagChallengeBinding
import com.appadore.machinetest.ui.base.AdapterClickListener
import com.appadore.machinetest.ui.base.BaseFragment
import com.appadore.machinetest.ui.main.FlagChallengeViewModel
import com.appadore.machinetest.ui.main.adapters.ChallengeOptionListAdapter
import com.appadore.machinetest.utils.AppConstants
import com.appadore.machinetest.utils.AppUtils.getParcelableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlagChallengeFragment : BaseFragment<FlagChallengeViewModel, FragmentFlagChallengeBinding>() {

    companion object {
        private const val ARG_QUESTION = "question"
        private const val ARG_POSITION = "position"

        fun newInstance(question: Question, currentPos: Int): FlagChallengeFragment {
            val fragment = FlagChallengeFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ARG_QUESTION, question)
                putInt(ARG_POSITION, currentPos)
            }
            return fragment
        }
    }

    private lateinit var mAdapter: ChallengeOptionListAdapter
    private var currentQuestion: Question? = null
    private var currentQuestionIndex: Int = 0


    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentFlagChallengeBinding.inflate(inflater)

    override fun getViewModelType() = FlagChallengeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        setIsSharedViewModel()
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initUI()
    }

    private fun observeData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                showSnackBar(it)
            }
        }
    }


    private fun setUpRecyclerView(dataSet: ArrayList<Country>) {
        binding?.optionsList?.layoutManager = GridLayoutManager(requireContext(), 2)
        mAdapter = ChallengeOptionListAdapter(dataSet, mClick)
        binding?.optionsList?.adapter = mAdapter
    }

    private val mClick = object : AdapterClickListener<Country> {
        override fun onAdapterItemClicked(item: Country) {
            if (::mAdapter.isInitialized){
                mAdapter.updateSelection(item)
            }
        }
    }

    private fun getArgumentsData() {
        arguments?.let {
            currentQuestion = it.getParcelableCompat(ARG_QUESTION)
            currentQuestionIndex = it.getInt(ARG_POSITION, 0)
        }
    }

    private fun initUI() {
        currentQuestion?.let {
            binding?.tvQstCount?.text = getString(R.string.question_count, currentQuestionIndex + 1)
            setUpRecyclerView(it.countries)
            startCountdown(AppConstants.DEFAULT_QUESTION_INTERVAL)
            binding?.item = it
        }
    }


    private fun goToNextQuestion() {
        if (viewModel.getTotalQuestions() == currentQuestionIndex + 1) {
            replaceFragment(ResultChallengeFragment.newInstance(), R.id.fragment_container)
        } else {
            currentQuestionIndex += 1
            viewModel.setCurrentQuestionIndex(currentQuestionIndex)
            viewModel.getQuestionByIndex(currentQuestionIndex)?.let {
                replaceFragment(newInstance(it, currentQuestionIndex), R.id.fragment_container)
            }
        }
    }

    private fun startCountdown(timeInterval: Long, isQuestionCountdown: Boolean = true) {
        val timer = object : CountDownTimer(timeInterval, AppConstants.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / AppConstants.COUNT_DOWN_INTERVAL
                if (isQuestionCountdown)
                    binding?.tvCountdown?.text = "$secondsRemaining"
                else {
                    binding?.tvIntervalCountdown?.text = "$secondsRemaining"
                }
                /*if (secondsRemaining > 0) {
                    binding?.tvCountdown?.text = "$secondsRemaining"
                } else {
                    onFinish()
                }*/
            }

            override fun onFinish() {
                if (isQuestionCountdown){
                    updateAnswer()
                    startNextQuestionInterval()
                } else {
                    goToNextQuestion()
                }
            }
        }
        timer.start()
    }


    private fun updateAnswer() {
        val selectedCountry = mAdapter.getSelectedOption()
        val correctAnswerId = currentQuestion?.answerId
        if (selectedCountry == null) {
            showSnackBar("No option was selected, so your answer is counted as wrong.")
        } else {
            if (selectedCountry.id == correctAnswerId) {
                showSnackBar("Your answer is correct.")
                selectedCountry.ansStatus = AppConstants.AnswerStatus.CORRECT
                viewModel.updateTotalScore()
            } else {
                showSnackBar("Your answer is wrong.")
                selectedCountry.ansStatus = AppConstants.AnswerStatus.WRONG
            }
        }
        mAdapter.updateAnswer(selectedCountry, correctAnswerId ?: 0)
        /*if (viewModel.getTotalQuestions() == currentQuestionIndex + 1) {
            showSnackBar("Challenge Completed")
        } else {
            showSnackBar("Time over! Next question in 10 seconds.")
        }*/

    }

    private fun startNextQuestionInterval(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            binding?.layoutTimerCount?.visibility = View.VISIBLE
            startCountdown(AppConstants.DEFAULT_QUESTION_TIMEOUT_INTERVAL, false)
        }
    }
}