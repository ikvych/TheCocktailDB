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
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkCategoryDialogFragment
import com.ikvych.cocktail.ui.dialog.FilterDrinkIngredientDialogFragment
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : BaseFragment<BaseViewModel>() {

    override var contentLayoutResId: Int = R.layout.fragment_filter
    override val viewModel: BaseViewModel by viewModels()

    private lateinit var returnBtn: ImageButton
    private lateinit var acceptBtn: Button
    private lateinit var resetBtn: Button
    lateinit var parentViewModel: MainFragmentViewModel

    private lateinit var alcoholFilter: LinearLayout
    private lateinit var chosenAlcoholFilter: TextView
    private lateinit var categoryFilter: LinearLayout
    private lateinit var chosenCategoryFilter: TextView
    private lateinit var ingredientFilter: LinearLayout
    private lateinit var chosenIngredientFilter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    @ExperimentalStdlibApi
    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)

        alcoholFilter = im_alcohol_filter
        alcoholFilter.setOnClickListener {
            FilterDrinkAlcoholDialogFragment.newInstance()
                .show(childFragmentManager, FilterDrinkAlcoholDialogFragment::class.java.simpleName)
        }
        chosenAlcoholFilter = tv_chosen_alcohol_filter
        parentViewModel.alcoholFilterLiveData.observe(this, Observer {
            chosenAlcoholFilter.text = it.key
        })



        categoryFilter = im_category_filter
        categoryFilter.setOnClickListener {
            FilterDrinkCategoryDialogFragment.newInstance().show(
                childFragmentManager,
                FilterDrinkCategoryDialogFragment::class.java.simpleName
            )
        }
        chosenCategoryFilter = tv_chosen_category_filter
        parentViewModel.categoryFilterLiveData.observe(this, Observer {
            chosenCategoryFilter.text = it.key
        })



        ingredientFilter = im_ingredient_filter
        ingredientFilter.setOnClickListener {
            FilterDrinkIngredientDialogFragment.newInstance()
                .show(
                    childFragmentManager,
                    FilterDrinkIngredientDialogFragment::class.java.simpleName
                )
        }
        chosenIngredientFilter = view.findViewById(R.id.tv_chosen_ingredient_filter)
        parentViewModel.ingredientFilterLiveData.observe(this, Observer {
            chosenIngredientFilter.text = it.key
        })



        acceptBtn = btn_accept
        acceptBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        resetBtn = btn_reject
        resetBtn.setOnClickListener {
            parentViewModel.resetFilters()
        }

        returnBtn = atb_fragment_filter.returnBtn
        returnBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        parentViewModel.allFilteredLiveData.observe(this, Observer {
            if (parentViewModel.isFiltersPresent()) {
                val snackBar = Snackbar.make(coordinator_filter_fragment, it, Snackbar.LENGTH_SHORT)
                if (parentViewModel.isUndoEnabled()) {
                    snackBar.setAction("Undo") {
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
            AlcoholDrinkType -> {
                val alcoholType = data as AlcoholDrinkFilter
                val filters = parentViewModel.filtersLiveData.value!!
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                filters[alcoholType.type] = alcoholType
                parentViewModel.filtersLiveData.value = filters
            }
            CategoryDrinkType -> {
                val categoryType = data as CategoryDrinkFilter
                val filters = parentViewModel.filtersLiveData.value!!
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                filters[categoryType.type] = categoryType
                parentViewModel.filtersLiveData.value = filters
            }
            IngredientDrinkType -> {
                val ingredientType = data as IngredientDrinkFilter
                val filters = parentViewModel.filtersLiveData.value!!
                parentViewModel.lastAppliedFiltersLiveData.value =
                    parentViewModel.filtersLiveData.value!!.clone() as HashMap<DrinkFilterType, DrinkFilter>
                filters[ingredientType.type] = ingredientType
                parentViewModel.filtersLiveData.value = filters
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(
            drinkFilters: ArrayList<DrinkFilter> = arrayListOf()
        ) = FilterFragment().apply {
            arguments = Bundle().apply {
                drinkFilters.forEach {
                    putString(it.type.key, it.key)
                }
            }
        }
    }

}