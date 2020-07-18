package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentFilterBinding
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.*
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkCategoryDialogFragment
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkGlassDialogFragment
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlin.reflect.KClass

class FilterFragment : BaseFragment<BaseViewModel, FragmentFilterBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_filter

    lateinit var parentViewModel: MainFragmentViewModel
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    @ExperimentalStdlibApi
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        //стартує dialogFragment для визначення типу фільтрування по вмісту алкоголю
        im_alcohol_filter_item.setOnClickListener {
            FilterDrinkAlcoholDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.ALCOHOL] as AlcoholDrinkFilter)
                .show(childFragmentManager, FilterDrinkAlcoholDialogFragment::class.java.simpleName)
        }

        //стартує dialogFragment для визначення типу фільтрування по категорії напою
        im_category_filter_item.setOnClickListener {
            FilterDrinkCategoryDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.CATEGORY] as CategoryDrinkFilter).show(
                childFragmentManager,
                FilterDrinkCategoryDialogFragment::class.java.simpleName
            )
        }

        //стартує dialogFragment для визначення типу фільтрування по інгредієнтах
        im_ingredient_filter_item.setOnClickListener {
            FilterDrinkIngredientDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.INGREDIENT] as IngredientDrinkFilter)
                .show(
                    childFragmentManager,
                    FilterDrinkIngredientDialogFragment::class.java.simpleName
                )
        }

        //стартує dialogFragment для визначення типу фільтрування по бокалу
        im_glass_filter_item.setOnClickListener {
            FilterDrinkGlassDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.GLASS] as GlassDrinkFilter)
                .show(
                    childFragmentManager,
                    FilterDrinkGlassDialogFragment::class.java.simpleName
                )
        }

        //Відслідковує обрані фільтри і показує на ui, який тип фільтру якому значенню відповідає
        parentViewModel.filtersLiveData.observe(this, Observer {
            tv_alcohol_filter_value.text = it[DrinkFilterType.ALCOHOL]?.key
            tv_category_filter_value.text = it[DrinkFilterType.CATEGORY]?.key
            tv_ingredient_filter_value.text = it[DrinkFilterType.INGREDIENT]?.key
            tv_glass_filter_value.text = it[DrinkFilterType.GLASS]?.key
        })

        //повертає на батьківський фрагмент
        btn_to_result.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //обнуляє всі фільтри
        btn_reject.setOnClickListener {
            parentViewModel.resetFilters()
        }

        //кастомна кнопка return - повертає на батьківський фрагмент
        atb_fragment_filter.returnBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //відслідковує і показує результати фільтрування в snackBar
        parentViewModel.filteredAndSortedResultDrinksLiveData.observe(this, Observer {
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
                parentViewModel.filtersLiveData.value = parentViewModel.filtersLiveData.value!!.apply { this[alcoholType.type] = alcoholType }
            }
            CategoryDrinkDialogType -> {
                val categoryType = data as CategoryDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value = parentViewModel.filtersLiveData.value!!.apply { this[categoryType.type] = categoryType }
            }
            IngredientDrinkDialogType -> {
                val ingredientType = data as IngredientDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value = parentViewModel.filtersLiveData.value!!.apply { this[ingredientType.type] = ingredientType }
            }
            GlassDrinkDialogType -> {
                val glassType = data as GlassDrinkFilter
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                parentViewModel.filtersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.apply {
                        this[glassType.type] = glassType
                    }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilterFragment()
    }


}