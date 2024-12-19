package com.appadore.machinetest.ui.main.adapters

import com.appadore.machinetest.R
import com.appadore.machinetest.data.model.Country
import com.appadore.machinetest.ui.base.AdapterClickListener
import com.appadore.machinetest.ui.base.BaseListAdapter
import com.appadore.machinetest.utils.AppConstants

class ChallengeOptionListAdapter(
    var data: ArrayList<Country>,
    private val _listener: AdapterClickListener<Country>
) : BaseListAdapter() {
    override fun getLayoutIdForType(viewType: Int) = R.layout.list_item_option

    override fun getDataAtPosition(position: Int) = data[position]

    override fun getClickListener() = _listener

    override fun getItemCount() = data.size


    fun updateSelection(item: Country) {
        if (item.ansStatus == AppConstants.AnswerStatus.NOT_ANSWERED) {
            data.forEach {
                it.isSelected = it.id == item.id
            }
            notifyDataSetChanged()
        }
    }

    fun getSelectedOption(): Country? {
        return data.firstOrNull { it.isSelected }
    }

    fun updateAnswer(ansId: Int) {
        data.forEach {
            it.isCorrect = it.id == ansId
            it.ansStatus = AppConstants.AnswerStatus.ANSWERED
        }
        val selectedIndex = data.indexOfFirst { it.isSelected }

        if (selectedIndex != -1) {
            val selectedOption = data[selectedIndex]
            if (selectedOption.id == ansId)
                data[selectedIndex].ansStatus = AppConstants.AnswerStatus.CORRECT
            else
                data[selectedIndex].ansStatus = AppConstants.AnswerStatus.WRONG

        } else {
            println("No option is selected.")
        }
        notifyDataSetChanged()
    }

    fun updateAnswer(selectedCountry: Country?, ansId: Int) {
        data.forEach {
            if (selectedCountry != null && it.id == selectedCountry.id) {
                it.ansStatus = selectedCountry.ansStatus
            } else {
                it.ansStatus = AppConstants.AnswerStatus.ANSWERED
            }
            it.isCorrect = it.id == ansId
        }
        notifyDataSetChanged()
    }
}

