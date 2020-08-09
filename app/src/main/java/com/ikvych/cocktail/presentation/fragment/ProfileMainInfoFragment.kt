package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentProfileMainInfoBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.EditProfileViewModel
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_profile_main_info.*
import kotlin.reflect.KClass

class ProfileMainInfoFragment : BaseFragment<EditProfileViewModel, FragmentProfileMainInfoBinding>(),
View.OnClickListener{
    override var contentLayoutResId: Int = R.layout.fragment_profile_main_info
    override val viewModelClass: KClass<EditProfileViewModel>
        get() = EditProfileViewModel::class

    val activityViewModel: EditProfileViewModel
        get() {
            return ViewModelProvider(requireActivity()).get(EditProfileViewModel::class.java)
        }

    override fun configureDataBinding(binding: FragmentProfileMainInfoBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        atb_profile_main_info.returnBtn.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileMainInfoFragment()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.ib_return_button -> {
                parentFragmentManager.popBackStack()
            }
        }
    }
}