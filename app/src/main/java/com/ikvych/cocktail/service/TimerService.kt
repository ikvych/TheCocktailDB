package com.ikvych.cocktail.service

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.ikvych.cocktail.constant.START_BACKGROUND_TIMER

class TimerService : JobIntentService() {

    var stopBroadcast: Boolean = false

    override fun onHandleWork(intent: Intent) {
        Log.d("MyService", "ServiceStart")
        try {
            for (i in 0..5) {
                if (stopBroadcast) {
                    break
                } else {
                    Thread.sleep(1000)
                    Intent().apply {
                        action = START_BACKGROUND_TIMER
                        sendBroadcast(this)
                    }
                    Log.d("MyService", "From cycle ${intent.toString()}")
                }
            }
        } catch (ex: InterruptedException) {

        }
    }

    override fun onDestroy() {
        stopBroadcast = true
        super.onDestroy()
    }
}