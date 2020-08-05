package com.ikvych.cocktail.util

class CachedBatteryState private constructor() {
    var isCharging: Boolean = false
    var percent: Int = 0
    var isLow: Boolean = false

    fun getPercentAsString(): String {
        return "${percent}%"
    }

    private object HOLDER {
        val INSTANCE = CachedBatteryState()
    }

    companion object {
        val instance: CachedBatteryState by lazy { HOLDER.INSTANCE }
    }
}
