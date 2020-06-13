package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.constant.SEARCH_MODEL_TYPE
import com.ikvych.cocktail.constant.VIEW_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.viewmodel.MainActivityViewModel


class DrinkDetailActivity : BaseActivity<MainActivityViewModel>() {

    private var drink: Drink? = null
    private var modelType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_details)

        val intent = intent

        if (intent != null && intent.hasExtra(DRINK)) {
            drink = intent.getParcelableExtra(DRINK)
            if (intent.hasExtra(VIEW_MODEL_TYPE)) {
                val viewModelType = intent.getStringExtra(VIEW_MODEL_TYPE)
                if (viewModelType != null) {
                    when (viewModelType) {
                        MAIN_MODEL_TYPE -> {
                            modelType = MAIN_MODEL_TYPE
                        }
                        SEARCH_MODEL_TYPE -> {
                            modelType = SEARCH_MODEL_TYPE
                            saveDrinkIntoDb(drink!!)
                        }
                    }
                }
            }
        }

        val activityDrinkDetailsBinding: ActivityDrinkDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_drink_details)
        activityDrinkDetailsBinding.drink = drink
    }

    private fun saveDrinkIntoDb(drink: Drink) {
        val mainActivityViewModel: MainActivityViewModel = ViewModelProvider.AndroidViewModelFactory(
            application
        )
            .create(MainActivityViewModel::class.java)
        mainActivityViewModel.saveDrink(drink)
    }

    fun resumePreviousActivity(view: View?) {
        finish()
    }

}
