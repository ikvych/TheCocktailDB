package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.appbar.AppBarLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.service.ApplicationService
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.DrinkDetailViewModel
import kotlin.reflect.KClass


class DrinkDetailActivity
    : BaseActivity<DrinkDetailViewModel, ActivityDrinkDetailsBinding>() {

    override val viewModelClass: KClass<DrinkDetailViewModel>
        get() = DrinkDetailViewModel::class
    override var contentLayoutResId: Int = R.layout.activity_drink_details

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
        when {
            intent.hasExtra(COCKTAIL) -> {
                viewModel.cocktailLiveData.value = intent.getParcelableExtra(COCKTAIL)
            }
            intent.hasExtra(COCKTAIL_ID) -> {
                val currentCocktailId = intent.getLongExtra(COCKTAIL_ID, -1L)
                if (currentCocktailId == -1L) finish()
                viewModel.findCocktailDbById(currentCocktailId)
            }
            else -> {
                finish()
            }
        }
        if (intent.hasExtra(SHOULD_SAVE_COCKTAIL)) {
            viewModel.saveCocktailIntoDb()
        }

        appBarLayout = findViewById(R.id.abl_drink_detail)
        imageView = findViewById(R.id.iv_drink_image)
        imageViewContainer = findViewById(R.id.ll_drink_image_container)

        initAppBarLayoutListener()
    }

    override fun configureDataBinding(binding: ActivityDrinkDetailsBinding) {
        super.configureDataBinding(binding)
        dataBinding.viewModel = viewModel
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

                //change transparency of background
/*                val stateList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this,
                            R.color.iv_return_button_bg
                        )
                    ).withAlpha((scaleFactor * 255).toInt())
                findViewById<ImageView>(R.id.ib_return).backgroundTintList = stateList*/


                val stateList =
                    ColorStateList.valueOf(
                        Color.rgb(
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt(),
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt(),
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt()
                        )
                    )
                findViewById<ImageView>(R.id.ib_return).backgroundTintList = stateList
            })
    }

    private fun initImageParameters() {
        maxImageWidth = imageView.width
        imageViewContainer.layoutParams.height = imageView.height
        imageViewContainer.requestLayout()
        cachedImageWidth = maxImageWidth

        imageMarginStart = (resources.getDimension(R.dimen.offset_64)).toInt()
        imageMarginTop = (resources.getDimension(R.dimen.offset_16)).toInt()
        minImageWidth = (resources.getDimension(R.dimen.offset_32)).toInt()

        imageViewParams = imageView.layoutParams as LinearLayout.LayoutParams
    }

    fun resumePreviousActivity(view: View?) {
        finish()
    }

    override fun onDestroy() {
        if (intent.hasExtra(SHOW_COCKTAIL_OFFER_ON_DESTROY)) {
            val intent = Intent(this, ApplicationService::class.java)
            stopService(intent)
            intent.putExtra(COCKTAIL_ID, viewModel.cocktailLiveData.value?.id)
            intent.action = ACTION_SHOW_COCKTAIL_OFFER
            startService(intent)
        }
        super.onDestroy()
    }
}
