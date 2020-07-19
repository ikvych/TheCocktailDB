package com.ikvych.cocktail

import android.app.Application
import com.amitshekhar.DebugDB
import com.ikvych.cocktail.di.Injector
import java.util.*

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

}