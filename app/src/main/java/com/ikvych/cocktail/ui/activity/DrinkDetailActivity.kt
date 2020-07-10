package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.service.ApplicationService
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.MainViewModel


class DrinkDetailActivity : BaseActivity() {

    override var contentLayoutResId: Int = R.layout.activity_drink_details

    private var drink: Drink? = null
    private var modelType: String? = null
    private lateinit var viewModel : MainViewModel


    private lateinit var appBarLayout: AppBarLayout
    private lateinit var imageView: ImageView
    private lateinit var imageViewContainer: LinearLayout

    private var maxImageWidth: Int? = null
    private var minImageWidth: Int? = null
    private var cachedImageWidth: Int? = null
    private var imageMarginStart: Int? = null
    private var imageMarginTop: Int? = null

    private lateinit var imageViewParams: LinearLayout.LayoutParams

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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
        if (intent != null && intent.hasExtra(DRINK_ID)) {
            val drinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
            if (drinkId != -1L) {
                drink = viewModel.findDrinkById(drinkId)
            } else {
                finish()
            }
        }

        val activityDrinkDetailsBinding: ActivityDrinkDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_drink_details)
        activityDrinkDetailsBinding.drink = drink

        appBarLayout = findViewById(R.id.abl)
        imageView = findViewById(R.id.iv_drink)
        imageViewContainer = findViewById(R.id.fl_image)

        initAppBarLayoutListener()
    }

    private fun initAppBarLayoutListener() {
        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                //init image parameters
                if (maxImageWidth == null) {
                    initImageParameters()
                }

                val totalsScrollRange = appBarLayout.totalScrollRange
                val offsetFactor = (-verticalOffset).toFloat() / totalsScrollRange
                val scaleFactor = 1F - offsetFactor

                val currentImageWidth = ((maxImageWidth!! - minImageWidth!!) * scaleFactor) + minImageWidth!!

                imageViewParams.marginStart = (imageMarginStart!! * offsetFactor).toInt()
                imageViewParams.topMargin = ((maxImageWidth!! / 2 - imageMarginTop!!) * offsetFactor).toInt()
                imageViewParams.width = currentImageWidth.toInt()

                if (imageViewParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth.toInt()
                    imageView.layoutParams = imageViewParams
                }

                if (scaleFactor == .0F) {
                    val stateList =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_primary))
                    findViewById<ImageView>(R.id.return_button).backgroundTintList = stateList
                } else {
                    val stateList =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.iv_return_button_bg))
                    findViewById<ImageView>(R.id.return_button).backgroundTintList = stateList
                }
            })
    }

    private fun initImageParameters() {
        maxImageWidth = imageView.width
        imageViewContainer.layoutParams.height = imageView.height
        imageViewContainer.requestLayout()
        cachedImageWidth = maxImageWidth

        imageMarginStart = (resources.getDimension(R.dimen.iv_detail_margin_start)).toInt()
        imageMarginTop = (resources.getDimension(R.dimen.iv_detail_margin_top)).toInt()
        minImageWidth = (resources.getDimension(R.dimen.iv_detail_min_width)).toInt()

        imageViewParams = imageView.layoutParams as LinearLayout.LayoutParams
    }

    private fun saveDrinkIntoDb(drink: Drink) {
        val mainActivityViewModel: MainViewModel =
            ViewModelProvider.AndroidViewModelFactory(
                application
            )
                .create(MainViewModel::class.java)
        mainActivityViewModel.saveDrink(drink)
    }

    fun resumePreviousActivity(view: View?) {
        finish()
    }

    override fun onDestroy() {
        if (modelType == SEARCH_MODEL_TYPE) {
            val intent = Intent(this, ApplicationService::class.java)
            intent.putExtra(DRINK_ID, drink?.getIdDrink())
            intent.action = ACTION_SHOW_DRINK_OFFER
            startService(intent)
        }
        super.onDestroy()
    }
}
