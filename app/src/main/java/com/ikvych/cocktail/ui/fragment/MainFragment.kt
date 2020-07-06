package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.FilterAdapter
import com.ikvych.cocktail.adapter.pager.DrinkPagerAdapter
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.databinding.FragmentMainBinding
import com.ikvych.cocktail.databinding.adapter.DataBindingAdapter
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragment
import com.ikvych.cocktail.util.Page
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainFragmentViewModel, FragmentMainBinding>(), BatteryListener,
    DataBindingAdapter.OnViewPagerChangeListener {

    override var contentLayoutResId: Int = R.layout.fragment_main
    override val viewModel: MainFragmentViewModel by viewModels()

    private lateinit var batteryReceiver: BatteryReceiver

    private lateinit var filterBtn: ImageButton
    private lateinit var filterIndicator: TextView

    private lateinit var sortBtn: ImageButton
    private lateinit var sortIndicator: TextView

    private lateinit var filterFragment: FilterFragment
    private lateinit var filterAdapter: FilterAdapter

    private lateinit var viewPager: ViewPager2
    private lateinit var drinkPagerAdapter: DrinkPagerAdapter
    private lateinit var tabLayout: TabLayout

    private lateinit var historyFragment: HistoryFragment
    private lateinit var favoriteFragment: FavoriteFragment

    private lateinit var batteryPercent: TextView
    private lateinit var batteryIcon: ImageView
    private lateinit var powerConnected: ImageView

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyFragment = HistoryFragment.newInstance()
        favoriteFragment = FavoriteFragment.newInstance()

        batteryReceiver = BatteryReceiver(this@MainFragment)
        viewModel.filteredFavoriteDrinksLiveData.observe(this, Observer { _ ->
            //trigger to init filteredFavoriteLiveData
        })
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {

        drinkPagerAdapter = DrinkPagerAdapter(
            arrayListOf(historyFragment, favoriteFragment),
            this
        )

        viewPager = dataBinding.vp2MainFragment
        viewPager.adapter = drinkPagerAdapter

        tabLayout = dataBinding.tlMainFragment
        Page.values().forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.name))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.viewPager2LiveData.value = Page.values()[tab!!.position]
            }
        })
        viewModel.viewPager2LiveData.value = Page.values()[viewPager.currentItem]
        viewModel.viewPager2LiveData.observe(this, Observer {
            tabLayout.selectTab(tabLayout.getTabAt(it.ordinal))
        })

        val filterRecyclerView: RecyclerView = dataBinding.rvFilterList
        filterAdapter = FilterAdapter(viewModel)
        filterRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.setHasFixedSize(true)
        filterRecyclerView.adapter = filterAdapter

        filterIndicator = atb_fragment_main.indicatorView

        filterBtn = atb_fragment_main.customBtn
        filterBtn.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_app_tool_bar_filter
            )
        )

        filterFragment = FilterFragment.newInstance()
        filterBtn.setOnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fcv_main_fragment, filterFragment, FilterFragment::class.java.simpleName)
            fragmentTransaction.addToBackStack(FilterFragment::class.java.name)
            fragmentTransaction.commit()
        }

        filterBtn.setOnLongClickListener {
            viewModel.resetFilters()
            filterAdapter.setData(arrayListOf())
            true
        }

        sortBtn = atb_fragment_main.sortBtn
        sortBtn.setOnClickListener {
            SortDrinkDialogFragment.newInstance(viewModel.sortLiveData.value)
                .show(childFragmentManager, SortDrinkDialogFragment::class.java.simpleName)
        }
        sortIndicator = atb_fragment_main.sortIndicatorView
        sortBtn.setOnLongClickListener {
            if (viewModel.sortLiveData.value != SortDrinkType.RECENT) {
                viewModel.sortLiveData.value = SortDrinkType.RECENT
                return@setOnLongClickListener true
            }
            false
        }

        fab_main_fragment.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        batteryPercent = tv_battery_percent
        batteryIcon = iv_battery_icon
        powerConnected = iv_power_connected

        viewModel.sortLiveData.observe(this, Observer{
            if (it == SortDrinkType.RECENT) {
                sortIndicator.visibility = View.GONE
            } else {
                sortIndicator.visibility = View.VISIBLE
            }
        })

        viewModel.filtersLiveData.observe(this, Observer {
            filterAdapter.setData(it.values.toList() as ArrayList)
            if (viewModel.isFiltersPresent()) {
                filterIndicator.visibility = View.VISIBLE
            } else {
                filterIndicator.visibility = View.GONE
            }
        })
        initLiveDataObserver()
    }

    override fun configureDataBinding(binding: FragmentMainBinding) {
        dataBinding.viewModel = viewModel
        dataBinding.listener = this
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
                        viewModel.sortLiveData.value = data as SortDrinkType
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
                viewModel.isBatteryChargingLiveData.value = status == BatteryManager.BATTERY_STATUS_CHARGING
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

    override fun onTabChanged(position: Int) {
/*        viewModel.viewPager2LiveData.value = Page.values()[position]*/
    }
}