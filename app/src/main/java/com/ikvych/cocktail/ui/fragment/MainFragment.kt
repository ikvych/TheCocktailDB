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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.FilterAdapter
import com.ikvych.cocktail.adapter.pager.DrinkPagerAdapter
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragment
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragmentList
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainFragmentViewModel>(), BatteryListener,
    FilterAdapter.OnClickItemFilterCloseListener {

    override var contentLayoutResId: Int = R.layout.fragment_main
    override val viewModel: MainFragmentViewModel by viewModels()

/*    val mainActivityViewModel: MainActivityViewModel by viewModels()*/

    private lateinit var batteryReceiver: BatteryReceiver

    private lateinit var filterBtn: ImageButton
    private lateinit var filterIndicator: TextView

    private var sortDrinkType: SortDrinkType = SortDrinkType.RECENT

    private lateinit var sortBtn: ImageButton
    private lateinit var sortIndicator: TextView

    private lateinit var filterFragment: FilterFragment

    private var filters: ArrayList<DrinkFilter> = arrayListOf()
    private lateinit var filterAdapter: FilterAdapter

    private lateinit var viewPager: ViewPager2
    private lateinit var drinkPagerAdapter: DrinkPagerAdapter
    private lateinit var tabLayout: TabLayout

    private lateinit var historyFragment: HistoryFragment
    private lateinit var favoriteFragment: FavoriteFragment

    private var isPowerConnected: Boolean = false
    private var isBatteryLow: Boolean = false
    private var percent: Int = 0

    private lateinit var batteryPercent: TextView
    private lateinit var batteryIcon: ImageView
    private lateinit var powerConnected: ImageView

    private lateinit var bottomSheetDialogFragment: RegularBottomSheetDialogFragment

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        historyFragment = HistoryFragment.newInstance()
        favoriteFragment = FavoriteFragment.newInstance()

        drinkPagerAdapter = DrinkPagerAdapter(
            arrayListOf(historyFragment, favoriteFragment),
            this
        )
        batteryReceiver = BatteryReceiver(this@MainFragment)

        bottomSheetDialogFragment = RegularBottomSheetDialogFragment.newInstance{
            titleText = "Log Out"
            descriptionText = "Are you Really want to exit?"
            leftButtonText = "Cancel"
            rightButtonText = "Accept"
        }

        viewModel.filteredFavoriteDrinksLiveData.observe(this, Observer { _ ->
            //stub
        })
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        viewPager = vp2_main_fragment
        viewPager.adapter = drinkPagerAdapter


        tabLayout = tl_main_fragment
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = getText(R.string.history)
            } else {
                tab.text = getText(R.string.favorite)
            }
        }.attach()


        val filterRecyclerView: RecyclerView = rv_filter_list
        filterAdapter = FilterAdapter(requireContext(), this)
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

        filterBtn.setOnClickListener {
            val fragmentTransaction = childFragmentManager.beginTransaction()
            filterFragment = FilterFragment.newInstance(filters)
            fragmentTransaction.add(R.id.fcv_main_fragment, filterFragment, FilterFragment::class.java.simpleName)
            fragmentTransaction.addToBackStack(FilterFragment::class.java.name)
            fragmentTransaction.commit()
        }

        filterBtn.setOnLongClickListener {
            viewModel.resetFilters()
            filterAdapter.filterList = arrayListOf()
            true
        }

        sortBtn = atb_fragment_main.sortBtn
        sortBtn.setOnClickListener {
            SortDrinkDialogFragmentList.newInstance(sortDrinkType)
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
            filterAdapter.filterList = it.values.toList() as ArrayList
            if (viewModel.isFiltersPresent()) {
                filterIndicator.visibility = View.VISIBLE
            } else {
                filterIndicator.visibility = View.GONE
            }
        })
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            SortDrinkDrinkType -> {
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
    }

    private fun isPowerConnected(isPowerConnected: Boolean) {
        if (isPowerConnected) {
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
            isBatteryLow(isBatteryLow)
            displayBatteryState()
        }
    }

    private fun isBatteryLow(isBatteryLow: Boolean) {
        if (isBatteryLow) {
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
    }

    override fun onClick(drinkFilter: DrinkFilter) {
        when (drinkFilter.type) {
            DrinkFilterType.CATEGORY -> {
                viewModel.filtersLiveData.value!![drinkFilter.type] = CategoryDrinkFilter.NONE
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value
            }
            DrinkFilterType.ALCOHOL -> {
                viewModel.filtersLiveData.value!![drinkFilter.type] = AlcoholDrinkFilter.NONE
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value
            }
            DrinkFilterType.GLASS -> {

            }
            DrinkFilterType.INGREDIENT -> {
                viewModel.filtersLiveData.value!![drinkFilter.type] = IngredientDrinkFilter.NONE
                viewModel.filtersLiveData.value = viewModel.filtersLiveData.value
            }
        }
    }
}