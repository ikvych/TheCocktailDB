package com.ikvych.cocktail.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.adapter.list.FilterAdapter
import com.ikvych.cocktail.presentation.adapter.pager.DrinkPagerAdapter
import com.ikvych.cocktail.presentation.filter.type.SortDrinkType
import com.ikvych.cocktail.databinding.FragmentMainBinding
import com.ikvych.cocktail.presentation.filter.DrinkFilter
import com.ikvych.cocktail.presentation.activity.SearchActivity
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType
import com.ikvych.cocktail.presentation.dialog.type.ItemListDialogButton
import com.ikvych.cocktail.presentation.dialog.type.SortDrinkDrinkDialogType
import com.ikvych.cocktail.presentation.dialog.regular.SortDrinkDialogFragment
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.util.CachedBatteryState
import com.ikvych.cocktail.presentation.enumeration.Page
import com.ikvych.cocktail.viewmodel.cocktail.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.cocktail.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.widget_app_toolbar.*
import kotlin.reflect.KClass

class MainFragment : BaseFragment<MainFragmentViewModel, FragmentMainBinding>(),
    View.OnClickListener, View.OnLongClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_main
    override val viewModelClass: KClass<MainFragmentViewModel>
        get() = MainFragmentViewModel::class
    private val parentViewModel: MainActivityViewModel by viewModels({requireActivity()})
/*
        get() {
            return ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        }
*/

    private lateinit var filterAdapter: FilterAdapter

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        //відслідковує кліки по табам і передає значення у відповідну liveData
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
        val filterRecyclerView: RecyclerView = dataBinding.rvFilterList
        filterAdapter = FilterAdapter(viewModel)
        filterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterRecyclerView.setHasFixedSize(true)
        filterRecyclerView.adapter = filterAdapter

        ib_custom_btn_2.setOnClickListener(this)
        ib_custom_btn_2.setOnLongClickListener(this)
        ib_custom_btn_1.setOnClickListener(this)
        ib_custom_btn_1.setOnLongClickListener(this)
        fab_main_fragment.setOnClickListener(this)

        initLiveDataObserver()

        viewModel.cachedBatteryStateLiveData.observe(this, Observer {
            manageBatteryState(it)
        })
    }

    override fun configureDataBinding(binding: FragmentMainBinding) {
        dataBinding.viewModel = viewModel
        dataBinding.parentViewModel = parentViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        filterAdapter.setLifecycleDestroyed()
    }

    private fun initLiveDataObserver() {
        viewModel.sortTypeLiveData.observe(this, Observer {
            if (it == SortDrinkType.RECENT) {
                atb_fragment_main.customBtnIndicatorView1.visibility = View.GONE
            } else {
                atb_fragment_main.customBtnIndicatorView1.visibility = View.VISIBLE
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
                tv_custom_button_indicator_2.visibility = View.VISIBLE
            } else {
                tv_custom_button_indicator_2.visibility = View.GONE
            }
        })
    }

    private fun manageBatteryState(state: CachedBatteryState) {
        if (!state.isLow && !state.isCharging) {
            tv_battery_percent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_on_surface
                )
            )
            iv_battery_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_battery
                )
            )
            iv_battery_icon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_on_surface
                )
            )
        }

        if (state.isCharging) {
            tv_battery_percent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.battery_charging
                )
            )
            iv_battery_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_battery_charge
                )
            )
            iv_battery_icon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.battery_charging
                )
            )
        } else if (state.isLow) {
            tv_battery_percent.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.battery_low
                )
            )
            iv_battery_icon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_battery_low
                )
            )
            iv_battery_icon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.battery_low
                )
            )
        }
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.ib_custom_btn_2 -> {
                val fragmentTransaction = childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_main_fragment,
                    FilterFragment.newInstance(),
                    FilterFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(FilterFragment::class.java.name)
                fragmentTransaction.commit()
            }
            R.id.ib_custom_btn_1 -> {
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
            R.id.ib_custom_btn_2 -> {
                viewModel.resetFilters()
                filterAdapter.setData(arrayListOf())
                true
            }
            //можна винести клік по цій кнопці у viewModel через dataBinding, але оскільки вона належить кастомному тулбару
            //то поки не брався це реалізовувати, і взагалі сумніваюся чи так потрібно робити
            R.id.ib_custom_btn_1 -> {
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