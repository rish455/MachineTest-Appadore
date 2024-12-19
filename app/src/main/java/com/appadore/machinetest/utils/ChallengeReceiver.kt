
package com.appadore.machinetest.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ChallengeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Code to start the challenge
        Log.e("TAG", "onReceive: ", )
        Toast.makeText(context, "Flags Challenge Started!", Toast.LENGTH_SHORT).show()
        
        // Optionally, launch a new Activity or start a service for the challenge
    }
}