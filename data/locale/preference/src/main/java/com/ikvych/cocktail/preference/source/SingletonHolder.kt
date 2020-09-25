package com.ikvych.cocktail.preference.source

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile private var _instance: T? = null
    val instance: A.() -> T = {
        _instance ?: synchronized(this@SingletonHolder) {
            val i = _instance
            if (i != null) i
            else {
                val created = this@SingletonHolder.creator!!(this)
                _instance = created
                this@SingletonHolder.creator = null
                created
            }
        }
    }

}

