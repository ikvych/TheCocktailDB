package com.ikvych.cocktail.service

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.ACTION_SHOW_DRINK_OFFER
import com.ikvych.cocktail.constant.BOOT_COMPLETED
import com.ikvych.cocktail.constant.DRINK_ID
import com.ikvych.cocktail.ui.activity.SplashActivity


class ApplicationService : JobIntentService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isBootCompleted = intent?.hasExtra(BOOT_COMPLETED) ?: false
        if (isBootCompleted) {
            val context: Context = applicationContext
            Toast.makeText(
                context,
                "Додаток ${getString(R.string.app_name)} запуститься через: 3 секунд!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onHandleWork(intent: Intent) {
        val currentAction = intent.action

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

        when (currentAction) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val context: Context = applicationContext
                Intent(context, SplashActivity::class.java).apply {
                    startActivity(this)
                }
            }
            ACTION_SHOW_DRINK_OFFER -> {
                val drinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
                Intent().apply {
                    action = ACTION_SHOW_DRINK_OFFER
                    putExtra(DRINK_ID, drinkId)
                    sendBroadcast(this)
                }
            }
        }
    }

}