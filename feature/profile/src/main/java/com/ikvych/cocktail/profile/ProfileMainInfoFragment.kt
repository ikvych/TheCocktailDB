package com.ikvych.cocktail.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.profile.databinding.FragmentProfileMainInfoBinding
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_main_info.*
import kotlin.reflect.KClass

class ProfileMainInfoFragment :
    BaseFragment<EditProfileViewModel, FragmentProfileMainInfoBinding>(),
    View.OnClickListener {
    override var contentLayoutResId: Int = R.layout.fragment_profile_main_info
    override val viewModelClass: KClass<com.ikvych.cocktail.profile.EditProfileViewModel>
        get() = com.ikvych.cocktail.profile.EditProfileViewModel::class

    val activityViewModel: com.ikvych.cocktail.profile.EditProfileViewModel
        get() {
            return ViewModelProvider(requireActivity()).get(com.ikvych.cocktail.profile.EditProfileViewModel::class.java)
        }

    override fun configureDataBinding(binding: FragmentProfileMainInfoBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        atb_profile_main_info.returnBtn.setOnClickListener(this)
        viewModel.shouldReturnLiveData.observe(this, Observer {
            if (it) {
                parentFragmentManager.popBackStack()
                viewModel.shouldReturnLiveData.value = false
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileMainInfoFragment()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            com.ikvych.cocktail.prresentation.R.id.ib_return_button -> {
                parentFragmentManager.popBackStack()
            }
        }
    }
}