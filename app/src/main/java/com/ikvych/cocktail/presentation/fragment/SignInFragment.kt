package com.ikvych.cocktail.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.core.common.exception.ApiException
import com.ikvych.cocktail.databinding.FragmentSignInBinding
import com.ikvych.cocktail.exception.ApiError
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.dialog.regular.ErrorDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.ActionSingleDialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType
import com.ikvych.cocktail.presentation.dialog.type.NotificationDialogType
import com.ikvych.cocktail.presentation.filter.TextInputFilter
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.auth.SignInViewModel
import com.ikvych.cocktail.util.widget.custom.LinerLayoutWithKeyboardListener
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.lang.Exception
import kotlin.reflect.KClass

class SignInFragment : BaseFragment<SignInViewModel, FragmentSignInBinding>(),
    LinerLayoutWithKeyboardListener.KeyBoardListener {

    override var contentLayoutResId: Int = R.layout.fragment_sign_in
    override val viewModelClass: KClass<SignInViewModel>
        get() = SignInViewModel::class

    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun configureDataBinding(binding: FragmentSignInBinding) {
        binding.viewModel = viewModel
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        llwkl_auth_container.listener = this
        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        tiet_auth_login.filters = arrayOf(inputFilter)
        tiet_auth_password.filters = arrayOf(inputFilter)

        viewModel.requestFocusOnLoginLiveData.observe(this, Observer {
            if (it != null) {
                tiet_auth_login.requestFocus()
                viewModel.requestFocusOnLoginLiveData.value = null
            }
        })
        viewModel.requestFocusOnPasswordLiveData.observe(this, Observer {
            if (it != null) {
                tiet_auth_password.requestFocus()
                viewModel.requestFocusOnPasswordLiveData.value = null
            }
        })
        viewModel.shouldLogInLiveData.observe(this, Observer {
            closeKeyboard()
            //якщо it==true значить помилок в введених даних немає, і тоді стартую MainActivity
            //у іншому випадку показує діалог з причиною помилки
            if (it) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            } else {
                ErrorDialogFragment.newInstance {
                    titleText = getString(R.string.auth_invalid_title)
                    leftButtonText = getString(R.string.all_ok_button)
                    descriptionText = viewModel.errorMessageLiveData.value!!
                }.show(childFragmentManager, ErrorDialogFragment::class.java.simpleName)
            }
        })

        tiet_auth_login.requestFocus()
    }

    override fun toProcessError(exception: Exception) {
        if (exception is ApiError && exception.code == 400) {
            ErrorDialogFragment.newInstance {
                titleText = getString(R.string.auth_invalid_title)
                leftButtonText = getString(R.string.all_ok_button)
                descriptionText = getString(R.string.auth_invalid_data)
            }.show(childFragmentManager, ErrorDialogFragment::class.java.simpleName)
        } else {
            super.toProcessError(exception)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onDialogFragmentClick(dialog, buttonType, type, data)
        when (type) {
            NotificationDialogType -> {
                when (buttonType) {
                    ActionSingleDialogButton -> {
                        if (!viewModel.isKeyboardShownLiveData.value!!) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        }
                    }
                }
            }
        }
    }

    private fun closeKeyboard() {
        val view: View = requireActivity().currentFocus ?: return
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) {
        viewModel.isKeyboardShownLiveData.value = isShowing
    }

    override fun onStop() {
        closeKeyboard()
        super.onStop()
    }

}