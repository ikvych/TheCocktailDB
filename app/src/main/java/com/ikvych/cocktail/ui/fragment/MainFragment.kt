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
import com.google.android.material.tabs.TabLayoutMediator
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.FilterAdapter
import com.ikvych.cocktail.adapter.pager.DrinkPagerAdapter
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.*
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.DialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogType
import com.ikvych.cocktail.ui.dialog.base.type.ItemListDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.SortDrinkDrinkDialogType
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.util.Page
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.widget_app_toolbar.*

class MainFragment : BaseFragment<MainFragmentViewModel>(), BatteryListener,
    FilterAdapter.OnClickRemoveItemFilterListener, View.OnClickListener, View.OnLongClickListener {

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

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        vp2_main_fragment.adapter = DrinkPagerAdapter(this)
        TabLayoutMediator(tl_main_fragment, vp2_main_fragment) { tab, position ->
            when (position) {
                Page.HISTORY.ordinal -> tab.text = getText(R.string.main_tab_layout_history_tab)
                Page.FAVORITE.ordinal -> tab.text = getText(R.string.main_tab_layout_favorite_tab)
                else -> throw IllegalStateException("Unknown page")
            }
        }.attach()

        val filterRecyclerView: RecyclerView = rv_filter_list
        filterAdapter = FilterAdapter(requireContext(), this)
        filterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.setHasFixedSize(true)
        filterRecyclerView.adapter = filterAdapter

        ib_filer_btn.setOnClickListener(this)
        ib_filer_btn.setOnLongClickListener(this)
        ib_sort_btn.setOnClickListener(this)
        ib_sort_btn.setOnLongClickListener(this)
        fab_main_fragment.setOnClickListener(this)

        batteryPercent = tv_battery_percent
        batteryIcon = iv_battery_icon
        powerConnected = iv_power_connected

        initLiveDataObserver()
    }

    private fun initLiveDataObserver() {
        viewModel.sortTypeLiveData.observe(this, Observer {
            if (it == SortDrinkType.RECENT) {
                atb_fragment_main.sortIndicatorView.visibility = View.GONE
            } else {
                atb_fragment_main.sortIndicatorView.visibility = View.VISIBLE
            }
        })

        viewModel.filtersLiveData.observe(this, Observer { it ->
            val filterList: ArrayList<DrinkFilter> = arrayListOf()
            it.values.forEach {list ->
                list.forEach {
                    filterList.add(it)
                }
            }
            filterAdapter.filterList = filterList
            if (viewModel.isFiltersPresent()) {
                atb_fragment_main.indicatorView.visibility = View.VISIBLE
            } else {
                atb_fragment_main.indicatorView.visibility = View.GONE
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

    override fun removeDrinkFilter(drinkFilter: DrinkFilter) {
        when (drinkFilter.type) {
            DrinkFilterType.CATEGORY -> {
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value!!.apply {
                    this[drinkFilter.type] = arrayListOf(CategoryDrinkFilter.NONE)
                }
            }
            DrinkFilterType.ALCOHOL -> {
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value!!.apply {
                    this[drinkFilter.type] = arrayListOf(AlcoholDrinkFilter.NONE)
                }
            }
            DrinkFilterType.GLASS -> {
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value!!.apply {
                    this[drinkFilter.type] = arrayListOf(GlassDrinkFilter.NONE)
                }
            }
            DrinkFilterType.INGREDIENT -> {
                val currentFilter = IngredientDrinkFilter.values().first { it.key == drinkFilter.key }
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value!!.apply {
                    val filters: List<DrinkFilter> = this[drinkFilter.type]!!
                    if (filters.size == 1) {
                        this[drinkFilter.type] = arrayListOf(IngredientDrinkFilter.NONE)
                    } else {
                        this[drinkFilter.type] = this[drinkFilter.type]!!.filter { it != currentFilter}
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.ib_filer_btn -> {
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
            R.id.ib_filer_btn -> {
                viewModel.resetFilters()
                filterAdapter.filterList = arrayListOf()
                true
            }
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
}