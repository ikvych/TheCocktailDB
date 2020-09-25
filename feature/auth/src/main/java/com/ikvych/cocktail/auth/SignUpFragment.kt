package com.ikvych.cocktail.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.ikvych.cocktail.api.MainStarter
import com.ikvych.cocktail.auth.databinding.FragmentSignUpBinding
import com.ikvych.cocktail.common.exception.ApiError
import com.ikvych.cocktail.prresentation.dialog.regular.ErrorDialogFragment
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import org.kodein.di.generic.instance
import kotlin.reflect.KClass

class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignUpBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_sign_up
    override val viewModelClass: KClass<SignUpViewModel>
        get() = SignUpViewModel::class

    private lateinit var inputMethodManager: InputMethodManager
    private val mainStarter: MainStarter by instance()

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewModel.shouldLogInLiveData.observe(this, Observer {
            closeKeyboard()
            if (it) {
                mainStarter.startMain()
/*                val intent = Intent(requireContext(), com.ikvych.cocktail.main.MainActivity::class.java)
                startActivity(intent)*/
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