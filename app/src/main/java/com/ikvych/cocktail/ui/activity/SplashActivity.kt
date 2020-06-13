package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}