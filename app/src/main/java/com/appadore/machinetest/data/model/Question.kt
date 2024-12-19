package com.appadore.machinetest.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    @SerializedName("answer_id") var answerId: Int? = null,
    @SerializedName("countries") var countries: ArrayList<Country> = arrayListOf(),
    @SerializedName("country_code") var countryCode: String? = null
): Parcelable
