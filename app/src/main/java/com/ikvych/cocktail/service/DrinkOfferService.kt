package com.ikvych.cocktail.service

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.ikvych.cocktail.constant.ACTION_SHOW_DRINK_OFFER
import com.ikvych.cocktail.constant.DRINK_ID


class DrinkOfferService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        try {
            Log.d("MyLog", "Thread fall asleep")
            for (i in 0..2) {
                Thread.sleep(1000)
                Log.d("MyLog", "Thread sleep $i second")
            }
            Log.d("MyLog", "Thread wake up")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val drinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
        Intent().apply {
            action = ACTION_SHOW_DRINK_OFFER
            putExtra(DRINK_ID, drinkId)
            sendBroadcast(this)
        }

    }

}