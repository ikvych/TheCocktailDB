package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentEditProfileBinding
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.user.EditProfileViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlin.reflect.KClass

class EditProfileFragment : BaseFragment<EditProfileViewModel, FragmentEditProfileBinding>(),
View.OnClickListener{
    override var contentLayoutResId: Int = R.layout.fragment_edit_profile
    override val viewModelClass: KClass<EditProfileViewModel>
        get() = EditProfileViewModel::class

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        tv_edit_main_info.setOnClickListener(this)
        atb_edit_profile.returnBtn.setOnClickListener(this)
    }

    override fun configureDataBinding(binding: FragmentEditProfileBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.tv_edit_main_info -> {
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile,
                    ProfileMainInfoFragment.newInstance(),
                    ProfileMainInfoFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(ProfileMainInfoFragment::class.java.name)
                fragmentTransaction.commit()
            }
            R.id.ib_return_button -> {
                parentFragmentManager.popBackStack()
            }
        }
    }
}