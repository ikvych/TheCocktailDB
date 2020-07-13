package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentFilterBinding
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.dialog.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkCategoryDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.*
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BaseFragment<BaseViewModel, FragmentFilterBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_filter
    override val viewModel: BaseViewModel by viewModels()

    private lateinit var returnBtn: ImageButton
    private lateinit var parentViewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    override fun configureDataBinding(binding: FragmentFilterBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = parentViewModel
    }

    @ExperimentalStdlibApi
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        parentViewModel.showFilterLiveData.observe(this, Observer {
            when (it) {
                DrinkFilterType.ALCOHOL -> {
                    FilterDrinkAlcoholDialogFragment.newInstance(parentViewModel.alcoholFilterLiveData.value)
                        .show(
                            childFragmentManager,
                            FilterDrinkAlcoholDialogFragment::class.java.simpleName
                        )
                }
                DrinkFilterType.CATEGORY -> {
                    FilterDrinkCategoryDialogFragment.newInstance(parentViewModel.categoryFilterLiveData.value)
                        .show(
                            childFragmentManager,
                            FilterDrinkCategoryDialogFragment::class.java.simpleName
                        )
                }
                DrinkFilterType.INGREDIENT -> {
                    FilterDrinkIngredientDialogFragment.newInstance(parentViewModel.ingredientFilterLiveData.value)
                        .show(
                            childFragmentManager,
                            FilterDrinkIngredientDialogFragment::class.java.simpleName
                        )
                }
                DrinkFilterType.GLASS -> {
                }
            }
        })

        parentViewModel.popBackStackLiveData.observe(this, Observer {
            if (it) {
                parentFragmentManager.popBackStack()
                parentViewModel.popBackStackLiveData.value = false
            }
        })

        returnBtn = atb_fragment_filter.returnBtn
        returnBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        parentViewModel.allFilteredLiveData.observe(this, Observer {
            if (parentViewModel.isFiltersPresent()) {
                val snackBar = Snackbar.make(coordinator_fragment_filter, it, Snackbar.LENGTH_LONG)
                if (parentViewModel.isUndoEnabled()) {
                    snackBar.setAction(R.string.all_undo_button) {
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.lastAppliedFiltersLiveData.value
                    }
                }
                snackBar.show()
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
            AlcoholDrinkDialogType -> {
                val alcoholType = data as AlcoholDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.apply {
                        this[alcoholType.type] = alcoholType
                    }
            }
            CategoryDrinkDialogType -> {
                val categoryType = data as CategoryDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.apply {
                        this[categoryType.type] = categoryType
                    }
            }
            IngredientDrinkDialogType -> {
                val ingredientType = data as IngredientDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.apply {
                        this[ingredientType.type] = ingredientType
                    }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilterFragment()
    }
}