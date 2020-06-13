package com.ikvych.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import com.ikvych.cocktail.listener.DrinkOfferListener

class DrinkOfferReceiver(
    private val listener: DrinkOfferListener
) : BroadcastReceiver() {

    private lateinit var asyncTask: Task

    override fun onReceive(context: Context, intent: Intent) {
        asyncTask = Task(pendingResult = goAsync(), listener = listener)
        asyncTask.execute(intent)
    }

    class Task(
        private val pendingResult: PendingResult,
        private val listener: DrinkOfferListener
    ) : AsyncTask<Intent, Unit, Unit>() {

        override fun doInBackground(vararg intent: Intent) {
            intent.forEach {
                listener.update(intent = it)
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            pendingResult.finish()
        }
    }
}

