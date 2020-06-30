package com.ikvych.cocktail.service

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.ikvych.cocktail.constant.START_BACKGROUND_TIMER

class TimerService : JobIntentService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "ServiceStart onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleWork(intent: Intent) {
        try {
            for (i in 0..5) {
                Thread.sleep(1000)
                Intent().apply {
                    action = START_BACKGROUND_TIMER
                    sendBroadcast(this)
                }
            }
        } catch (ex: InterruptedException) {

        }
        Log.d("MyService", "ServiceStart onHandleWork")
    }

}