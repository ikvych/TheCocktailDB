package com.ikvych.cocktail.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.core.common.exception.ApiException
import com.ikvych.cocktail.databinding.FragmentSignUpBinding
import com.ikvych.cocktail.exception.ApiError
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.dialog.regular.ErrorDialogFragment
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.auth.SignUpViewModel
import java.lang.Exception
import kotlin.reflect.KClass

class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignUpBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_sign_up
    override val viewModelClass: KClass<SignUpViewModel>
        get() = SignUpViewModel::class

    private lateinit var inputMethodManager: InputMethodManager

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewModel.shouldLogInLiveData.observe(this, Observer {
            closeKeyboard()
            if (it) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            } else {
                ErrorDialogFragment.newInstance {
                    titleText = getString(R.string.auth_invalid_title)
                    leftButtonText = getString(R.string.all_ok_button)
                    descriptionText = /*viewModel.errorMessageLiveData.value!!*/"Щось не так"
                }.show(childFragmentManager, ErrorDialogFragment::class.java.simpleName)
            }
        })
    }

    override fun configureDataBinding(binding: FragmentSignUpBinding) {
        binding.viewModel = viewModel
    }

    override fun toProcessError(exception: Exception) {
        if (exception is ApiError && exception.code == 401) {
            ErrorDialogFragment.newInstance {
                titleText = getString(R.string.auth_invalid_title)
                leftButtonText = getString(R.string.all_ok_button)
                descriptionText = getString(R.string.auth_user_already_exist).replace("$", viewModel.emailInputLiveData.value!!)
            }.show(childFragmentManager, ErrorDialogFragment::class.java.simpleName)
        } else {
            super.toProcessError(exception)
        }
    }

    private fun closeKeyboard() {
        val view: View = requireActivity().currentFocus ?: return
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onStop() {
        closeKeyboard()
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}