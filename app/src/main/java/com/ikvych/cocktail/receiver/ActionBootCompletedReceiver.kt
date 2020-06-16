package com.ikvych.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ikvych.cocktail.constant.BOOT_COMPLETED
import com.ikvych.cocktail.service.ApplicationService

class ActionBootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("MyLog", "Start Up Receiver ACTION_BOOT_COMPLETED")
            val newIntent = Intent(context, ApplicationService::class.java)
            newIntent.putExtra(BOOT_COMPLETED, true)
            context?.startService(newIntent)
        }
    }

}