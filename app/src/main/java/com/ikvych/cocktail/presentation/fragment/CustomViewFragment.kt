package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentCustomViewBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class CustomViewFragment : BaseFragment<BaseViewModel, FragmentCustomViewBinding>() {

    override var contentLayoutResId = R.layout.fragment_custom_view
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    companion object {
        @JvmStatic
        fun newInstance() = CustomViewFragment()

    }
}