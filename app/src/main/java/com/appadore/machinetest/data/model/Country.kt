package com.appadore.machinetest.data.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.appadore.machinetest.BR
import com.appadore.machinetest.utils.AppConstants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("country_name") var countryName: String? = null,
    @SerializedName("id") var id: Int? = null,
    private var _isSelected: Boolean = false,
    private var _ansStatus: Int = AppConstants.AnswerStatus.NOT_ANSWERED,
    private var _isCorrect: Boolean = false
) : BaseObservable(), Parcelable {
    var isSelected: Boolean
        @Bindable get() = _isSelected
        set(value) {
            _isSelected = value
            notifyPropertyChanged(BR.selected)
        }

    var ansStatus: Int
        @Bindable get() = _ansStatus
        set(value) {
            _ansStatus = value
            notifyPropertyChanged(BR.ansStatus)
        }

    var isCorrect: Boolean
        @Bindable get() = _isCorrect
        set(value) {
            _isCorrect = value
            notifyPropertyChanged(BR.correct)
        }
}
