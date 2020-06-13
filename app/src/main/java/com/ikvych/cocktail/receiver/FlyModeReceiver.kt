package com.ikvych.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class FlyModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            val isAirplaneModeOn = intent!!.getBooleanExtra("state", false)
            if (isAirplaneModeOn) {
                Toast.makeText(
                    context,
                    "Смакуйте маргаритку із відчуттям міри. І надіємося, що Ви не пілот під  час польоту",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}