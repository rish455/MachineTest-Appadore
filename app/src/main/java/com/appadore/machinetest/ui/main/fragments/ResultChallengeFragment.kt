package com.appadore.machinetest.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.appadore.machinetest.R
import com.appadore.machinetest.data.model.Question
import com.appadore.machinetest.databinding.FragmentResultChallengeBinding
import com.appadore.machinetest.databinding.FragmentScheduleChallengeBinding
import com.appadore.machinetest.ui.base.BaseFragment
import com.appadore.machinetest.ui.main.FlagChallengeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultChallengeFragment :
    BaseFragment<FlagChallengeViewModel, FragmentResultChallengeBinding>() {

    companion object {

        fun newInstance(): ResultChallengeFragment {
            val fragment = ResultChallengeFragment()
            return fragment
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentResultChallengeBinding.inflate(inflater)

    override fun getViewModelType() = FlagChallengeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        setIsSharedViewModel()
        super.onCreate(savedInstanceState)
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

    private fun initUI(){
        binding?.score?.text = getString(R.string.total_score, viewModel.getTotalScore())
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            binding?.layoutScore?.visibility = View.VISIBLE
        }
    }
}