package com.appadore.machinetest.data

import com.appadore.machinetest.utils.AppConstants

data class ProgressState(val state: Int = AppConstants.DEFAULT_PROGRESS_STATE, val isShow: Boolean) {
}