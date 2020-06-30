package com.ikvych.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class TimerReceiver(
    val listener: OnTimerReceiverListener
) : BroadcastReceiver() {

    interface OnTimerReceiverListener {
        fun onReceive()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        listener.onReceive()
    }
}