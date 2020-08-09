package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentEditProfileBinding
import com.ikvych.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import com.ikvych.cocktail.viewmodel.EditProfileViewModel
import com.ikvych.cocktail.viewmodel.ProfileActivityViewModel
import kotlin.reflect.KClass

class EditProfileFragment : BaseFragment<EditProfileViewModel, FragmentEditProfileBinding>() {
    override var contentLayoutResId: Int = R.layout.fragment_edit_profile
    override val viewModelClass: KClass<EditProfileViewModel>
        get() = EditProfileViewModel::class

    override fun configureDataBinding(binding: FragmentEditProfileBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }
}