package com.ikvych.cocktail.prresentation.service

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import com.ikvych.cocktail.prresentation.R
import com.ikvych.cocktail.prresentation.util.ACTION_SHOW_COCKTAIL_OFFER
import com.ikvych.cocktail.prresentation.util.BOOT_COMPLETED
import com.ikvych.cocktail.prresentation.util.COCKTAIL_ID


class ApplicationService : JobIntentService() {

    private var shouldStop: Boolean = false

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
            for (i in 0..2) {
                if (shouldStop) {
                    return@onHandleWork
                }
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        when (currentAction) {
            Intent.ACTION_BOOT_COMPLETED -> {
/*                val context: Context = applicationContext
                Intent(context, com.ikvych.cocktail.splash.SplashActivity::class.java).apply {
                    startActivity(this)
                }*/
            }
            ACTION_SHOW_COCKTAIL_OFFER -> {
                val drinkId: Long = intent.getLongExtra(COCKTAIL_ID, -1L)
                Intent().apply {
                    action = ACTION_SHOW_COCKTAIL_OFFER
                    putExtra(COCKTAIL_ID, drinkId)
                    sendBroadcast(this)
                }
            }
        }
    }

    override fun onDestroy() {
        shouldStop = true
        super.onDestroy()
    }

}