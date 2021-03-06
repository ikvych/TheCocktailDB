package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import com.google.android.material.appbar.AppBarLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.service.ApplicationService
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.util.*
import com.ikvych.cocktail.viewmodel.cocktail.CocktailDetailViewModel
import kotlinx.android.synthetic.main.activity_drink_details.*
import kotlin.reflect.KClass


class DrinkDetailActivity
    : BaseActivity<CocktailDetailViewModel, ActivityDrinkDetailsBinding>() {

    override val viewModelClass: KClass<CocktailDetailViewModel>
        get() = CocktailDetailViewModel::class
    override var contentLayoutResId: Int = R.layout.activity_drink_details

    private var maxImageWidth: Int? = null
    private var minImageWidth: Int? = null
    private var cachedImageWidth: Int? = null
    private var imageMarginStart: Int? = null
    private var imageMarginVertical: Int? = null

    private var imgContainerHeight: Int? = null

    private lateinit var imageViewParams: LinearLayout.LayoutParams

    override fun configureView(savedInstanceState: Bundle?) {
        //якщо присутній інтент SHOW_DRINK_OFFER_ON_DESTROY зупиняємо сервіс, для того щоб не настиковувались
        // пропозиції переглянути напій
        if (intent.hasExtra(SHOW_COCKTAIL_OFFER_ON_DESTROY)) {
            val intent = Intent(this, ApplicationService::class.java)
            stopService(intent)
        }
        //в залажності від того чи було передано id напою чи сам напій, вибирається спосіб як ініціалізувати
        //drinkLiveData з поточним напоєм. Якщо немає ніодого з варіантів, завершуємо актівіті
        var cocktailId: Long = -1
        when {
            intent.hasExtra(COCKTAIL) -> {
                viewModel.cocktailLiveData.value = intent.getParcelableExtra(COCKTAIL)
                cocktailId = viewModel.cocktailLiveData.value!!.id
                //Якщо присутній інтен SHOULD_SAVE_DRINK, тоді зберігаємо напій в базу даних
                if (intent.hasExtra(SHOULD_SAVE_COCKTAIL)) {
                    viewModel.saveCocktailIntoDb()
                }
            }
            intent.hasExtra(COCKTAIL_ID) -> {
                val currentCocktailId = intent.getLongExtra(COCKTAIL_ID, -1L)
                if (currentCocktailId == -1L) finish()
                cocktailId = currentCocktailId
                viewModel.findCocktailDbById(currentCocktailId)
            }
            else -> {
                finish()
            }
        }
        viewModel.firebase.logEvent(
            ANALYTIC_EVENT_COCKTAIL_DETAIL_OPEN, bundleOf(
                ANALYTIC_KEY_COCKTAIL_ID to cocktailId
            )
        )
        //ініціалізуємо слухач, для зміни розмірів і позиції зображення напою
        initAppBarLayoutListener()
    }

    override fun configureDataBinding(binding: ActivityDrinkDetailsBinding) {
        super.configureDataBinding(binding)
        dataBinding.viewModel = viewModel
    }

    private fun initAppBarLayoutListener() {
        abl_drink_detail.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                //init image parameters
                if (maxImageWidth == null) {
                    initImageParameters()
                }

                val totalsScrollRange = appBarLayout.totalScrollRange
                val offsetFactor = (-verticalOffset).toFloat() / totalsScrollRange
                val scaleFactor = 1F - offsetFactor

                val currentImageWidth = ((maxImageWidth!! - minImageWidth!!) * scaleFactor) + minImageWidth!!
                val currentMarginVertical =  ((imgContainerHeight!! / 2 - imageMarginVertical!!) * offsetFactor).toInt()

                imageViewParams.marginStart = (imageMarginStart!! * offsetFactor).toInt()
                imageViewParams.topMargin = currentMarginVertical
                imageViewParams.bottomMargin = currentMarginVertical
                imageViewParams.width = currentImageWidth.toInt()

                if (imageViewParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth.toInt()
                    iv_drink_image.layoutParams = imageViewParams
                    val rounded = currentImageWidth.toInt() / 2 * offsetFactor
                    iv_drink_image.cornerRadius = rounded
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

    private fun convertDpToPx(dp: Int): Float {
        return dp * this.resources.displayMetrics.density
    }

    private fun initImageParameters() {
        maxImageWidth = iv_drink_image.width
        imgContainerHeight = ll_drink_image_container.height
        ll_drink_image_container.layoutParams.height = iv_drink_image.height
        ll_drink_image_container.requestLayout()
        cachedImageWidth = maxImageWidth

        imageMarginStart = (resources.getDimension(R.dimen.offset_64)).toInt()
        imageMarginVertical = (resources.getDimension(R.dimen.offset_16)).toInt()
        minImageWidth = (resources.getDimension(R.dimen.offset_32)).toInt()

        imageViewParams = iv_drink_image.layoutParams as LinearLayout.LayoutParams
    }

    @Suppress("UNUSED_PARAMETER")
    fun resumePreviousActivity(view: View?) {
        finish()
    }

    override fun onDestroy() {
        if (intent.hasExtra(SHOW_COCKTAIL_OFFER_ON_DESTROY)) {
            val intent = Intent(this, ApplicationService::class.java)
            intent.putExtra(COCKTAIL_ID, viewModel.cocktailLiveData.value?.id)
            intent.action = ACTION_SHOW_COCKTAIL_OFFER
            startService(intent)
        }
        super.onDestroy()
    }

    companion object {
        const val ANALYTIC_EVENT_COCKTAIL_DETAIL_OPEN = "cocktail_detail_open"
        const val ANALYTIC_KEY_COCKTAIL_ID = "cocktail_id"
    }
}
