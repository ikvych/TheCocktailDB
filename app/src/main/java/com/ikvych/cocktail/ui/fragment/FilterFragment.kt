package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.*
import com.ikvych.cocktail.ui.dialog.*
import com.ikvych.cocktail.ui.dialog.base.type.*
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BaseFragment<BaseViewModel>(), View.OnClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_filter
    override val viewModel: BaseViewModel by viewModels()
    lateinit var parentViewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    @ExperimentalStdlibApi
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        im_alcohol_filter_item.setOnClickListener(this)
        im_category_filter_item.setOnClickListener(this)
        im_ingredient_filter_item.setOnClickListener(this)
        im_glass_filter_item.setOnClickListener(this)
        acb_to_result.setOnClickListener(this)
        acb_reset.setOnClickListener(this)
        atb_fragment_filter.returnBtn.setOnClickListener(this)

        //Відслідковує обрані фільтри і показує на ui, який тип фільтру якому значенню відповідає
        parentViewModel.filtersLiveData.observe(this, Observer {
            tv_alcohol_filter_value.text = it[DrinkFilterType.ALCOHOL]?.first()?.key
            tv_category_filter_value.text = it[DrinkFilterType.CATEGORY]?.first()?.key
            var text = String()
            it[DrinkFilterType.INGREDIENT]?.forEach { text += "${it.key}, " }
            tv_ingredient_filter_value.text = text
            tv_glass_filter_value.text = it[DrinkFilterType.GLASS]?.first()?.key
        })

        //відслідковує і показує результати фільтрування в snackBar
        parentViewModel.filteredAndSortedResultDrinksLiveData.observe(this, Observer {
            if (parentViewModel.isFiltersPresent()) {
                val snackBar = Snackbar.make(ll_btn_container, it, Snackbar.LENGTH_SHORT)
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

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            //стартує dialogFragment для визначення типу фільтрування по вмісту алкоголю
            R.id.im_alcohol_filter_item -> {
                FilterDrinkAlcoholDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.ALCOHOL]!!.first() as AlcoholDrinkFilter)
                    .show(
                        childFragmentManager,
                        FilterDrinkAlcoholDialogFragment::class.java.simpleName
                    )
            }
            //стартує dialogFragment для визначення типу фільтрування по категорії напою
            R.id.im_category_filter_item -> {
                FilterDrinkCategoryDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.CATEGORY]!!.first() as CategoryDrinkFilter)
                    .show(
                        childFragmentManager,
                        FilterDrinkCategoryDialogFragment::class.java.simpleName
                    )
            }
            //стартує dialogFragment для визначення типу фільтрування по інгредієнтах
            R.id.im_ingredient_filter_item -> {
                TestFilterDrinkIngredientDialogFragment.newInstance(
                    parentViewModel.filtersLiveData.value!![DrinkFilterType.INGREDIENT] as List<IngredientDrinkFilter>
                )
                    .show(
                        childFragmentManager,
                        FilterDrinkIngredientDialogFragment::class.java.simpleName
                    )
/*                FilterDrinkIngredientDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.INGREDIENT]!!.first() as IngredientDrinkFilter)
                    .show(
                        childFragmentManager,
                        FilterDrinkIngredientDialogFragment::class.java.simpleName
                    )*/
            }
            //стартує dialogFragment для визначення типу фільтрування по бокалу
            R.id.im_glass_filter_item -> {
                FilterDrinkGlassDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![DrinkFilterType.GLASS]!!.first() as GlassDrinkFilter)
                    .show(
                        childFragmentManager,
                        FilterDrinkGlassDialogFragment::class.java.simpleName
                    )
            }
            //повертає на батьківський фрагмент
            R.id.acb_to_result -> {
                parentFragmentManager.popBackStack()
            }
            //обнуляє всі фільтри
            R.id.acb_reset -> {
                parentViewModel.resetFilters()
            }
            //кастомна кнопка return - повертає на батьківський фрагмент
            R.id.ib_return_button -> {
                parentFragmentManager.popBackStack()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (buttonType) {
            ItemListDialogButton -> {
                when (type) {
                    AlcoholDrinkDialogType -> {
                        val alcoholType = data as AlcoholDrinkFilter
                        parentViewModel.lastAppliedFiltersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.apply {
                                this[alcoholType.type] = arrayListOf(alcoholType)
                            }
                    }
                    CategoryDrinkDialogType -> {
                        val categoryType = data as CategoryDrinkFilter
                        parentViewModel.lastAppliedFiltersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.apply {
                                this[categoryType.type] = arrayListOf(categoryType)
                            }
                    }
/*                    IngredientDrinkDialogType -> {
                        val ingredientType = data as IngredientDrinkFilter
                        parentViewModel.lastAppliedFiltersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.apply {
                                this[ingredientType.type] = arrayListOf(ingredientType)
                            }
                    }*/
                    GlassDrinkDialogType -> {
                        val glassType = data as GlassDrinkFilter
                        parentViewModel.lastAppliedFiltersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.apply {
                                this[glassType.type] = arrayListOf(glassType)
                            }
                    }
                }
            }
            RightListDialogButton -> {
                when (type) {
                    IngredientDrinkDialogType -> {
                        val ingredientTypes = data as List<IngredientDrinkFilter>
                        parentViewModel.lastAppliedFiltersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, List<DrinkFilter>>
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.filtersLiveData.value!!.apply {
                                this[DrinkFilterType.INGREDIENT] = ingredientTypes
                            }
                    }
                }
            }
            LeftListDialogButton -> {
                dialog.dismiss()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = FilterFragment()
    }
}