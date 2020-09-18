package com.ikvych.cocktail.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentCustomView2Binding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.util.widget.custom.GifView2
import com.ikvych.cocktail.util.widget.custom.RangeSeekBar
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_custom_view_2.*
import kotlin.reflect.KClass

class CustomViewFragment2 : BaseFragment<BaseViewModel, FragmentCustomView2Binding>(), RangeSeekBar.OnIndicatorChanged {

    override var contentLayoutResId: Int = R.layout.fragment_custom_view_2
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        seek_bar.setOnIndicatorChangedListener(this)
        id_change_range.setOnClickListener {
            seek_bar.setRange(1, 6, true)
        }
        id_change_range_animated.setOnClickListener {
            seek_bar.setRange(4, 5, false)
        }

        b_left.setOnClickListener {
            gv_gif.startAnim()
        }
        b_right.setOnClickListener {
            gv_gif.stopAnim()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gv_gif.aspectRationMode = GifView2.AspectRatioMode.WIDTH_MODE
        } else{
            gv_gif.aspectRationMode = GifView2.AspectRatioMode.HEIGHT_MODE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CustomViewFragment2()
    }

    override fun onLeftIndicatorChanged(value: Int) {

    }

    override fun onRightIndicatorChanged(value: Int) {

    }

}