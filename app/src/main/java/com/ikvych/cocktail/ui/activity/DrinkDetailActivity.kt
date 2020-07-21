package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding
import com.ikvych.cocktail.service.ApplicationService
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.DrinkDetailViewModel
import kotlinx.android.synthetic.main.activity_drink_details.*

class DrinkDetailActivity : BaseActivity<DrinkDetailViewModel>() {

    override val viewModel: DrinkDetailViewModel by viewModels()
    override var contentLayoutResId: Int = R.layout.activity_drink_details

    private var maxImageWidth: Int? = null
    private var minImageWidth: Int? = null
    private var cachedImageWidth: Int? = null
    private var imageMarginStart: Int? = null
    private var imageMarginTop: Int? = null

    private lateinit var imageViewParams: LinearLayout.LayoutParams

    override fun configureView(savedInstanceState: Bundle?) {
        //якщо присутній інтент SHOW_DRINK_OFFER_ON_DESTROY зупиняємо сервіс, для того щоб не спамились
        // пропозиції переглянути напій
        if (intent.hasExtra(SHOW_DRINK_OFFER_ON_DESTROY)) {
            stopService(Intent(this, ApplicationService::class.java))
        }
        //в залажності від того чи було передано id напою чи сам напій, вибирається спосіб як ініціалізувати
        //drinkLiveData з поточним напоєм. Якщо немає ніодого з варіантів, завершуємо актівіті
        when {
            intent.hasExtra(DRINK) -> {
                viewModel.drinkLiveData.value = intent.getParcelableExtra(DRINK)
            }
            intent.hasExtra(DRINK_ID) -> {
                viewModel.drinkIdLiveData.value = intent.getLongExtra(DRINK_ID, -1L)
            }
            else -> {
                finish()
            }
        }
        //Якщо присутній інтен SHOULD_SAVE_DRINK, тоді зберігаємо його в базу даних
        if (intent.hasExtra(SHOULD_SAVE_DRINK)) {
            viewModel.saveDrinkIntoDb()
        }

        val activityDrinkDetailsBinding: ActivityDrinkDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_drink_details)
        activityDrinkDetailsBinding.viewModel = viewModel

        //ініціалізуємо слухач, для зміни розмірів і позиції зображення напою
        initAppBarLayoutListener()
    }

    private fun initAppBarLayoutListener() {
        abl_drink_detail.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                //якщо maxImageWidth == null значить загальні параметри зображення потрібно ініціалізувати
                if (maxImageWidth == null) {
                    initImageParameters()
                }

                val totalsScrollRange = appBarLayout.totalScrollRange
                val offsetFactor = (-verticalOffset).toFloat() / totalsScrollRange
                val scaleFactor = 1F - offsetFactor

                val currentImageWidth =
                    ((maxImageWidth!! - minImageWidth!!) * scaleFactor) + minImageWidth!!

                imageViewParams.marginStart = (imageMarginStart!! * offsetFactor).toInt()
                imageViewParams.topMargin =
                    ((maxImageWidth!! / 2 - imageMarginTop!!) * offsetFactor).toInt()
                imageViewParams.width = currentImageWidth.toInt()

                if (imageViewParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth.toInt()
                    iv_drink_image.layoutParams = imageViewParams
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

                //плавно змінює колір тла для кнопки повернення
                val stateList =
                    ColorStateList.valueOf(
                        Color.rgb(
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt(),
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt(),
                            ((0.85 + (0.15 * offsetFactor)) * 255).toInt()
                        )
                    )
                ib_return.backgroundTintList = stateList
            })
    }

    private fun initImageParameters() {
        maxImageWidth = iv_drink_image.width
        ll_drink_image_container.layoutParams.height = iv_drink_image.height
        ll_drink_image_container.requestLayout()
        cachedImageWidth = maxImageWidth

        imageMarginStart = (resources.getDimension(R.dimen.offset_64)).toInt()
        imageMarginTop = (resources.getDimension(R.dimen.offset_16)).toInt()
        minImageWidth = (resources.getDimension(R.dimen.offset_32)).toInt()

        imageViewParams = iv_drink_image.layoutParams as LinearLayout.LayoutParams
    }

    @Suppress("UNUSED_PARAMETER")
    fun resumePreviousActivity(view: View?) {
        finish()
    }

    override fun onDestroy() {
        //старту інтент який запускає сервіс з пропозицією переглянути випадковий напій
        if (intent.hasExtra(SHOW_DRINK_OFFER_ON_DESTROY)) {
            val appServiceIntent = Intent(this, ApplicationService::class.java)
            appServiceIntent.putExtra(DRINK_ID, viewModel.drinkLiveData.value?.getIdDrink())
            appServiceIntent.action = ACTION_SHOW_DRINK_OFFER
            startService(appServiceIntent)
        }
        super.onDestroy()
    }
}
