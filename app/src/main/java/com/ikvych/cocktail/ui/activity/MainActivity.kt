package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : RecyclerViewActivity<MainActivityViewModel>(), BatteryListener {

    lateinit var batteryReceiver: BatteryReceiver

    private var isPowerConnected: Boolean = false
    private var isBatteryLow: Boolean = false
    private var percent: Int = 0

    private lateinit var batteryPercent: TextView
    private lateinit var batteryIcon: ImageView
    private lateinit var powerConnected: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryReceiver = BatteryReceiver(this)

        batteryPercent = findViewById(R.id.tv_battery_percent)
        batteryIcon = findViewById(R.id.iv_battery_icon)
        powerConnected = findViewById(R.id.iv_power_connected)

        initViewModel(MainActivityViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.db_recycler_view, MAIN_MODEL_TYPE)
        initLiveDataObserver()



        fab.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val batteryReceiverFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(batteryReceiver, batteryReceiverFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(batteryReceiver)
    }

    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(this@MainActivity)
        } else {
            setDbRecyclerViewVisible(this@MainActivity)
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(this@MainActivity)
        } else {
            setDbRecyclerViewVisible(this@MainActivity)
        }
    }

    override fun onBatteryChange(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                isPowerConnected = true
                isPowerConnected(isPowerConnected)
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                isPowerConnected = false
                isPowerConnected(isPowerConnected)
            }
            Intent.ACTION_BATTERY_LOW -> {
                isBatteryLow = true
                isBatteryLow(isBatteryLow)
            }
            Intent.ACTION_BATTERY_OKAY -> {
                isBatteryLow = false
            }
            Intent.ACTION_BATTERY_CHANGED -> {
                percent = intent.let {
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    level * 100 / scale.toFloat()
                }.toInt()

                displayBatteryState()
            }
        }
    }

    private fun displayBatteryState() {
        val textPercent = "${percent}%"
        batteryPercent.text = textPercent

        if (!isBatteryLow && !isPowerConnected) {
            batteryPercent.setTextColor(ContextCompat.getColor(this, R.color.color_on_surface))
            powerConnected.setColorFilter(ContextCompat.getColor(this, R.color.color_on_surface))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_battery))
            batteryIcon.setColorFilter(ContextCompat.getColor(this, R.color.color_on_surface))
        }
    }

    private fun isPowerConnected(isPowerConnected: Boolean) {
        if (isPowerConnected) {
            powerConnected.visibility = View.VISIBLE
            batteryPercent.setTextColor(ContextCompat.getColor(this, R.color.battery_charging))
            powerConnected.setColorFilter(ContextCompat.getColor(this, R.color.battery_charging))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_battery_charge))
            batteryIcon.setColorFilter(ContextCompat.getColor(this, R.color.battery_charging))
        } else {
            powerConnected.visibility = View.GONE
            isBatteryLow(isBatteryLow)
            displayBatteryState()
        }
    }

    private fun isBatteryLow(isBatteryLow: Boolean) {
        if (isBatteryLow) {
            batteryPercent.setTextColor(ContextCompat.getColor(this, R.color.battery_low))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_battery_low))
            batteryIcon.setColorFilter(ContextCompat.getColor(this, R.color.battery_low))
        }
    }

}
