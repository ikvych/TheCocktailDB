package com.ikvych.cocktail.setting

import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import com.ikvych.cocktail.setting.databinding.FragmentCustomViewBinding
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