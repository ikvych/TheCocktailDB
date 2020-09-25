package com.ikvych.cocktail.setting

import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import com.ikvych.cocktail.setting.databinding.FragmentGifBinding
import kotlin.reflect.KClass

class GifFragment : BaseFragment<BaseViewModel, FragmentGifBinding>() {

    override var contentLayoutResId: Int
        get() = R.layout.fragment_gif
        set(value) {}
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    companion object {
        @JvmStatic
        fun newInstance() =
            GifFragment()
    }

}