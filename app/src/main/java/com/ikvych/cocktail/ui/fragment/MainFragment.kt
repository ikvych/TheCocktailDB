package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.FilterAdapter
import com.ikvych.cocktail.adapter.pager.DrinkPagerAdapter
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.databinding.FragmentMainBinding
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragment
import com.ikvych.cocktail.ui.dialog.type.DialogButton
import com.ikvych.cocktail.ui.dialog.type.DialogType
import com.ikvych.cocktail.ui.dialog.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.type.SortDrinkDrinkDialogType
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.util.Page
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.widget_app_toolbar.*

class MainFragment : BaseFragment<MainFragmentViewModel, FragmentMainBinding>(), BatteryListener,
    View.OnClickListener, View.OnLongClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_main
    override val viewModel: MainFragmentViewModel by viewModels()

    private lateinit var batteryReceiver: BatteryReceiver
    private lateinit var filterAdapter: FilterAdapter

    private lateinit var batteryPercent: TextView
    private lateinit var batteryIcon: ImageView
    private lateinit var powerConnected: ImageView

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryReceiver = BatteryReceiver(this@MainFragment)
        viewModel.filteredAndSortedFavoriteDrinksLiveData.observe(this, Observer { _ ->
            //trigger to init filteredFavoriteLiveData
        })
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        val drinkPagerAdapter = DrinkPagerAdapter(this)
        vp2_main_fragment.adapter = drinkPagerAdapter

        //ініціалізую таби для viewPager2
        Page.values().forEach {
            tl_main_fragment.addTab(tl_main_fragment.newTab().setText(it.name))
        }
        //відслідковує кліки по табам і передаю значення у відповідну liveData
        tl_main_fragment.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.viewPager2LiveData.value = Page.values()[tab!!.position]
            }
        })
        //відслідковує скрол по viewPager2 і передаю відповідне значення в tl_main_fragment щоб
        //забезпечити переключення таб
        viewModel.viewPager2LiveData.observe(this, Observer {
            tl_main_fragment.selectTab(tl_main_fragment.getTabAt(it.ordinal))
        })

        //RecyclerView для обраних фільтрів
        val filterRecyclerView: RecyclerView = rv_filter_list
        filterAdapter = FilterAdapter(viewModel)
        filterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.setHasFixedSize(true)
        filterRecyclerView.adapter = filterAdapter

        ib_filter_btn.setOnClickListener(this)
        ib_filter_btn.setOnLongClickListener(this)
        ib_sort_btn.setOnClickListener(this)
        ib_sort_btn.setOnLongClickListener(this)
        fab_main_fragment.setOnClickListener(this)

        batteryPercent = tv_battery_percent
        batteryIcon = iv_battery_icon
        powerConnected = iv_power_connected

        initLiveDataObserver()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.ib_filter_btn -> {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_main_fragment,
                    FilterFragment.newInstance(),
                    FilterFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(FilterFragment::class.java.name)
                fragmentTransaction.commit()
            }
            R.id.ib_sort_btn -> {
                SortDrinkDialogFragment.newInstance(viewModel.sortTypeLiveData.value)
                    .show(childFragmentManager, SortDrinkDialogFragment::class.java.simpleName)
            }
            R.id.fab_main_fragment -> {
                startActivity(Intent(requireContext(), SearchActivity::class.java))
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v == null) {
            return false
        }
        return when (v.id) {
            R.id.ib_filter_btn -> {
                viewModel.resetFilters()
                filterAdapter.setData(arrayListOf())
                true
            }
            //можна винести клік по цій кнопці у viewModel через dataBinding, але оскільки вона належить кастомному тулбару
            //то поки не брався це реалізовувати, і взагалі сумніваюся чи так потрібно робити
            R.id.ib_sort_btn -> {
                if (viewModel.sortTypeLiveData.value != SortDrinkType.RECENT) {
                    viewModel.sortTypeLiveData.value = SortDrinkType.RECENT
                    return true
                }
                false
            }
            else -> false
        }
    }

    override fun configureDataBinding(binding: FragmentMainBinding) {
        dataBinding.viewModel = viewModel
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            SortDrinkDrinkDialogType -> {
                when (buttonType) {
                    ItemListDialogButton -> {
                        viewModel.sortTypeLiveData.value = data as SortDrinkType
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        filterAdapter.setLifecycleDestroyed()
    }

    override fun onBatteryChange(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> viewModel.isBatteryLowLiveData.value = true
            Intent.ACTION_BATTERY_OKAY -> viewModel.isBatteryLowLiveData.value = false
            Intent.ACTION_BATTERY_CHANGED -> {
                val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                viewModel.isBatteryChargingLiveData.value =
                    status == BatteryManager.BATTERY_STATUS_CHARGING
                            || status == BatteryManager.BATTERY_STATUS_FULL
                viewModel.batteryPercentLiveData.value = intent.let {
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    level * 100 / scale.toFloat()
                }.toInt()
            }
        }
    }

    private fun initLiveDataObserver() {
        viewModel.sortTypeLiveData.observe(this, Observer {
            if (it == SortDrinkType.RECENT) {
                atb_fragment_main.sortIndicatorView.visibility = View.GONE
            } else {
                atb_fragment_main.sortIndicatorView.visibility = View.VISIBLE
            }
        })

        viewModel.filtersLiveData.observe(this, Observer {
            val filterList: ArrayList<DrinkFilter> = arrayListOf()
            it.values.forEach { list ->
                list.forEach {
                    filterList.add(it)
                }
            }
            filterAdapter.setData(filterList)
            if (viewModel.isFiltersPresent()) {
                tv_filter_indicator.visibility = View.VISIBLE
            } else {
                tv_filter_indicator.visibility = View.GONE
            }
        })

        viewModel.batteryPercentLiveData.observe(this, Observer {
            val textPercent = "${it!!}%"
            batteryPercent.text = textPercent

            val isBatteryLow = viewModel.isBatteryLowLiveData.value!!
            val isPowerConnected = viewModel.isBatteryChargingLiveData.value!!
            if (!isBatteryLow && !isPowerConnected) {
                batteryPercent.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_on_surface
                    )
                )
                powerConnected.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_on_surface
                    )
                )
                batteryIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_battery
                    )
                )
                batteryIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_on_surface
                    )
                )
            }
        })
        viewModel.isBatteryChargingLiveData.observe(this, Observer {
            if (it) {
                powerConnected.visibility = View.VISIBLE
                batteryPercent.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.battery_charging
                    )
                )
                powerConnected.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.battery_charging
                    )
                )
                batteryIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_battery_charge
                    )
                )
                batteryIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.battery_charging
                    )
                )
            } else {
                powerConnected.visibility = View.GONE
                viewModel.isBatteryLowLiveData.value = viewModel.isBatteryLowLiveData.value
            }
        })
        viewModel.isBatteryLowLiveData.observe(this, Observer {
            if (it) {
                batteryPercent.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.battery_low
                    )
                )
                batteryIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_battery_low
                    )
                )
                batteryIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.battery_low
                    )
                )
            }
        })
    }

}