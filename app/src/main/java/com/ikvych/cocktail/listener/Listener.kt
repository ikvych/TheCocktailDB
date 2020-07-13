package com.ikvych.cocktail.listener

import android.view.View

class Listener : View.OnLongClickListener {
    override fun onLongClick(v: View?): Boolean {
        println()
        return false
    }
}