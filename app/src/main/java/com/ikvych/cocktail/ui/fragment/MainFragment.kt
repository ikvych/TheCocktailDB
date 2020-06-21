package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_BUNDLE_KEY
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_KEY
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : RecyclerViewFragment<MainViewModel>(), BatteryListener {
    lateinit var batteryReceiver: BatteryReceiver

    private var isPowerConnected: Boolean = false
    private var isBatteryLow: Boolean = false
    private var percent: Int = 0

    private lateinit var batteryPercent: TextView
    private lateinit var batteryIcon: ImageView
    private lateinit var powerConnected: ImageView

    companion object {

        @JvmStatic
        fun newInstance(fragmentId: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ALCOHOL_FILTER_KEY) { _, bundle ->
            val result = bundle.getString(ALCOHOL_FILTER_BUNDLE_KEY)
            filterData(AlcoholDrinkFilter.valueOf(result!!))
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        batteryReceiver = BatteryReceiver(this)

        batteryPercent = requireView().findViewById(R.id.tv_battery_percent)
        batteryIcon = requireView().findViewById(R.id.iv_battery_icon)
        powerConnected = requireView().findViewById(R.id.iv_power_connected)

        initViewModel(MainViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.db_recycler_view, MAIN_MODEL_TYPE)
        initLiveDataObserver()

        fab.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
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
        requireContext().registerReceiver(batteryReceiver, batteryReceiverFilter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(batteryReceiver)
    }

    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireActivity())
        } else {
            setDbRecyclerViewVisible(requireActivity())
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireActivity())
        } else {
            setDbRecyclerViewVisible(requireActivity())
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
            batteryPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_on_surface))
            powerConnected.setColorFilter(ContextCompat.getColor(requireContext(), R.color.color_on_surface))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_battery))
            batteryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.color_on_surface))
        }
    }

    private fun isPowerConnected(isPowerConnected: Boolean) {
        if (isPowerConnected) {
            powerConnected.visibility = View.VISIBLE
            batteryPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.battery_charging))
            powerConnected.setColorFilter(ContextCompat.getColor(requireContext(), R.color.battery_charging))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_battery_charge))
            batteryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.battery_charging))
        } else {
            powerConnected.visibility = View.GONE
            isBatteryLow(isBatteryLow)
            displayBatteryState()
        }
    }

    private fun isBatteryLow(isBatteryLow: Boolean) {
        if (isBatteryLow) {
            batteryPercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.battery_low))
            batteryIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_battery_low))
            batteryIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.battery_low))
        }
    }
}