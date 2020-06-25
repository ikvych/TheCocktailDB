package com.ikvych.cocktail.ui.fragment

import android.content.Context
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
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.listener.BatteryListener
import com.ikvych.cocktail.listener.FilterResultCallBack
import com.ikvych.cocktail.listener.SortResultCallBack
import com.ikvych.cocktail.receiver.BatteryReceiver
import com.ikvych.cocktail.ui.activity.SearchActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.SexListBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.SortDrinkDialogFragment
import com.ikvych.cocktail.widget.custom.ApplicationToolBar
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(), BatteryListener, FilterFragment.OnFilterResultListener,
    FilterAdapter.OnClickItemFilterCloseListener, SortResultCallBack {

    override val callbacks: HashSet<OnSortResultListener> = hashSetOf()
    override fun addCallBack(listener: OnSortResultListener) {
        callbacks.add(listener)
    }

    override fun removeCallBack(listener: OnSortResultListener) {
        callbacks.remove(listener)
    }

    lateinit var batteryReceiver: BatteryReceiver
    var fragmentListener: FilterFragment.OnFilterResultListener? = null

    lateinit var filterBtn: ImageButton
    lateinit var filterIndicator: TextView

    var sortDrinkType: SortDrinkType = SortDrinkType.RECENT

    lateinit var sortBtn: ImageButton
    lateinit var sortIndicator: TextView

    lateinit var filterFragment: FilterFragment

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

    interface OnSortResultListener {
        fun onResult(sortDrinkType: SortDrinkType)
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            fragmentListener = activity as FilterFragment.OnFilterResultListener
            (activity as FilterResultCallBack).addCallBack(this)
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FilterResultCallBack | FilterFragment.OnFilterResultListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            (requireActivity() as FilterResultCallBack).removeCallBack(this)
            fragmentListener = null
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FilterResultCallBack")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyFragment = HistoryFragment.newInstance(R.layout.fragment_history)
        favoriteFragment = FavoriteFragment.newInstance(R.layout.fragment_favorite)

        drinkPagerAdapter = DrinkPagerAdapter(
            arrayListOf(historyFragment, favoriteFragment),
            this
        )
        batteryReceiver = BatteryReceiver(this)
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = drinkPagerAdapter

        tabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = getText(R.string.history)
            } else {
                tab.text = getText(R.string.favorite)
            }
        }.attach()


        val filterRecyclerView: RecyclerView = view.findViewById(R.id.rv_filter)
        filterAdapter = FilterAdapter(requireContext(), this)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.layoutManager = layoutManager
        filterRecyclerView.setHasFixedSize(true)
        filterRecyclerView.adapter = filterAdapter
        filterAdapter.filterList = filters

        filterIndicator =
            view.findViewById<ApplicationToolBar>(R.id.atb_fragment_main).indicatorView

        filterBtn = view.findViewById<ApplicationToolBar>(R.id.atb_fragment_main).customBtn
        filterBtn.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_app_tool_bar_filter
            )
        )

        filterBtn.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            filterFragment =
                FilterFragment.newInstance(R.layout.fragment_filter, filters)
            fragmentTransaction.hide(this)
            fragmentTransaction.add(R.id.fcv_main, filterFragment)
            fragmentTransaction.addToBackStack(FilterFragment::class.java.name)
            fragmentTransaction.commit()
        }

        filterBtn.setOnLongClickListener {
            fragmentListener!!.onFilterReset()
            callbacks.forEach {
                it.onResult(sortDrinkType)
            }
            true
        }

        sortBtn = view.findViewById<ApplicationToolBar>(R.id.atb_fragment_main).sortBtn
        sortBtn.setOnClickListener { v ->
            SortDrinkDialogFragment.newInstance(sortDrinkType) {
                this.titleText = "Sort history"
                this.leftButtonText = "Cancel"
                this.rightButtonText = "Accept"
            }.show(childFragmentManager)
        }
        sortIndicator =
            view.findViewById<ApplicationToolBar>(R.id.atb_fragment_main).sortIndicatorView
        sortBtn.setOnLongClickListener { v ->
            if (sortDrinkType != SortDrinkType.RECENT) {
                sortDrinkType = SortDrinkType.RECENT
                callbacks.forEach {
                    it.onResult(sortDrinkType)
                }
                sortIndicator.visibility = View.GONE
                true
            } else {
                false
            }
        }

        fab.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        batteryPercent = view.findViewById(R.id.tv_battery_percent)
        batteryIcon = view.findViewById(R.id.iv_battery_icon)
        powerConnected = view.findViewById(R.id.iv_power_connected)
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            RegularDialogType -> {
                when (buttonType) {
                    RightDialogButton -> {
                        val supportData = data as SortDrinkType ?: return
                        sortDrinkType = supportData
                        callbacks.forEach {
                            it.onResult(sortDrinkType)
                        }
                        if (sortDrinkType != SortDrinkType.RECENT) {
                            sortIndicator.visibility = View.VISIBLE
                        } else {
                            sortIndicator.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    override fun onFilterApply(drinkFilters: ArrayList<DrinkFilter>) {
        filters = drinkFilters
        filterAdapter.filterList = filters
        if (filters.isNotEmpty()) {
            filterIndicator.visibility = View.VISIBLE
        } else {
            filterIndicator.visibility = View.GONE
        }
    }

    override fun onFilterReset() {
        filters = arrayListOf()
        filterAdapter.filterList = filters
        filterIndicator.visibility = View.GONE
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
        filters.remove(drinkFilter)
        fragmentListener!!.onFilterApply(filters)
    }
}