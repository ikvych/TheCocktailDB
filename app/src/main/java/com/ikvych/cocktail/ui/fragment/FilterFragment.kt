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
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkDialogFragment
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.ui.dialog.type.*
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.ui.model.cocktail.IngredientModel
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_filter.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class FilterFragment : BaseFragment<BaseViewModel, FragmentFilterBinding>(), View.OnClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_filter
    lateinit var parentViewModel: MainFragmentViewModel
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
        parentViewModel.fragmentJustCreated = true
    }

    @ExperimentalStdlibApi
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        im_alcohol_filter_item.setOnClickListener(this)
        im_category_filter_item.setOnClickListener(this)
        im_ingredient_filter_item.setOnClickListener(this)
        im_glass_filter_item.setOnClickListener(this)
        acb_to_result.setOnClickListener(this)
        atb_fragment_filter.returnBtn.setOnClickListener(this)

        //відслідковує і показує результати фільтрування в snackBar
        parentViewModel.filteredAndSortedResultDrinksLiveData.observe(this, Observer {
            //якщо fragmentJustCreated == true, отже фрагмент щойно був створений і при першому заході
            //на нього, результати пошуку не потрбіно показувати
            if (!parentViewModel.fragmentJustCreated) {
                val snackBar = Snackbar.make(ll_btn_container, it, Snackbar.LENGTH_SHORT)
                //показую кнопку відмінити останні обрані фільтри у тому випадку якщо поточні і
                //попередні фільтри є різними
                if (parentViewModel.isUndoEnabled()) {
                    snackBar.setAction(R.string.all_undo_button) {
                        parentViewModel.filtersLiveData.value =
                            parentViewModel.lastAppliedFiltersLiveData.value
                    }
                }
                snackBar.show()
            } else {
                parentViewModel.fragmentJustCreated = false
            }
        })
    }

    override fun configureDataBinding(binding: FragmentFilterBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = parentViewModel
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.im_alcohol_filter_item -> {
                startFilterDialog(DrinkFilterType.ALCOHOL)
            }
            R.id.im_category_filter_item -> {
                startFilterDialog(DrinkFilterType.CATEGORY)
            }
            R.id.im_glass_filter_item -> {
                startFilterDialog(DrinkFilterType.GLASS)
            }
            R.id.im_ingredient_filter_item -> {
                startFilterDialogWithMultiSelection(DrinkFilterType.INGREDIENT)
            }
            //повертає на батьківський фрагмент
            R.id.acb_to_result -> {
                parentFragmentManager.popBackStack()
            }
            //кастомна кнопка return - повертає на батьківський фрагмент
            R.id.ib_return_button -> {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun startFilterDialog(filterType: DrinkFilterType) {
        FilterDrinkDialogFragment.newInstance(parentViewModel.filtersLiveData.value!![filterType]!!.first())
            .show(
                childFragmentManager,
                FilterDrinkDialogFragment::class.java.simpleName
            )
    }

    //наразі цей тип діалогу заточений під інгредієнти, в подальшому при необхідності можливо переробити
    //на DrinkFilterType
    @Suppress("UNCHECKED_CAST")
    private fun startFilterDialogWithMultiSelection(filterType: DrinkFilterType) {
        FilterDrinkIngredientDialogFragment.newInstance(
            parentViewModel.filtersLiveData.value!![filterType] as List<IngredientModel>,
            parentViewModel.ingredientsListLiveData.value ?: arrayListOf()
        )
            .show(
                childFragmentManager,
                FilterDrinkIngredientDialogFragment::class.java.simpleName
            )
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
                    FilterDrinkDialogType -> {
                        val filter = data as DrinkFilter
                        parentViewModel.updateFilter(filter)
                    }
                    else -> throw IllegalArgumentException("Unknown dialog type")
                }
            }
            RightListDialogButton -> {
                when (type) {
                    FilterListDrinkDialogType -> {
                        val filterTypes = data as List<DrinkFilter>
                        parentViewModel.updateFilterList(filterTypes)
                    }
                    else -> throw IllegalArgumentException("Unknown dialog type")
                }
            }
            LeftListDialogButton -> {
                dialog.dismiss()
            }
            else -> throw IllegalArgumentException("Unknown button type")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilterFragment()
    }

}