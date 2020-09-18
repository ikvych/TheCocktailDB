package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentCustomViewBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.util.widget.custom.LinearLayoutWithChildLimiter
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_custom_view.*
import kotlin.reflect.KClass

class CustomViewFragment : BaseFragment<BaseViewModel, FragmentCustomViewBinding>() {

    override var contentLayoutResId = R.layout.fragment_custom_view
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        val text = "Text\n2nd line\nSample Text"
/*        artv_test.maxLines = 2
        artv_test.ellipsize = TextUtils.TruncateAt.END
        artv_test.text = text
        val lp = tv_test.layoutParams as LinearLayoutWithChildLimiter.LinearLayoutWithChildLimiterLayoutParams*/
/*        lp.layoutPercentSize = 50*/
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomViewFragment()
    }
}