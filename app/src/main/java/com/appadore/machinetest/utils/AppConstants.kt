package com.appadore.machinetest.utils

object AppConstants {

    const val DEFAULT_PROGRESS_STATE = 1010
    const val QUESTION_INTERVAL = 30000L
    const val QUESTION_TIMEOUT_INTERVAL = 10000L
    const val QUESTION_TIMEOUT_INTERVAL_DELAY = 2000L
    const val COUNT_DOWN_INTERVAL = 1000L


    object AnswerStatus {
        const val NOT_ANSWERED = 0
        const val ANSWERED = 1
        const val CORRECT = 2
        const val WRONG = 3
    }

}