package com.appadore.machinetest.data.model

import com.google.gson.annotations.SerializedName

data class FlagChallenge(
    @SerializedName("questions" ) var questions : ArrayList<Question> = arrayListOf()
)
