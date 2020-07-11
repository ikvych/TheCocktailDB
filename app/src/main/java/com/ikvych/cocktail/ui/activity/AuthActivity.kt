package com.ikvych.cocktail.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityAuthBinding
import com.ikvych.cocktail.filter.TextInputFilter
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.ErrorAuthDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.ActionSingleDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogType
import com.ikvych.cocktail.ui.dialog.base.type.NotificationDialogType
import com.ikvych.cocktail.viewmodel.AuthViewModel
import com.ikvych.cocktail.widget.custom.LinerLayoutWithKeyboardListener
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity<AuthViewModel, ActivityAuthBinding>(),
    LinerLayoutWithKeyboardListener.KeyBoardListener {

    override var contentLayoutResId: Int = R.layout.activity_auth
    override val viewModel: AuthViewModel by viewModels()

    private lateinit var textInputEditLogin: TextInputEditText
    private lateinit var textInputEditPassword: TextInputEditText

    private lateinit var submitButton: Button
    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun configureView(savedInstanceState: Bundle?) {
        val keyboardObserver =
            findViewById<LinerLayoutWithKeyboardListener>(R.id.llwkl_auth_container)
        keyboardObserver.listener = this
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        textInputEditLogin = tiet_auth_login
        textInputEditLogin.filters = arrayOf(inputFilter)
        textInputEditPassword = tiet_auth_password
        textInputEditPassword.filters = arrayOf(inputFilter)

        submitButton = b_auth_login

        submitButton.setOnClickListener {
            closeKeyboard()

            if (viewModel.isLoginDataMatchPatternLiveData.value!!.first &&
                viewModel.isLoginDataMatchPatternLiveData.value!!.second &&
                viewModel.isLoginDataValidLiveData.value!!
            ) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                if (!viewModel.isLoginDataMatchPatternLiveData.value!!.second) {
                    textInputEditPassword.requestFocus()
                }
                if (!viewModel.isLoginDataMatchPatternLiveData.value!!.first) {
                    textInputEditLogin.requestFocus()
                }
                ErrorAuthDialogFragment.newInstance {
                    titleText = getString(R.string.auth_invalid_title)
                    leftButtonText = getString(R.string.all_ok_button)
                    descriptionText = viewModel.errorMessageViewModel.value!!
                }.show(supportFragmentManager, ErrorAuthDialogFragment::class.java.simpleName)
                submitButton.background
            }
        }

        viewModel.isLoginDataMatchPatternLiveData.observe(this, Observer { })
        viewModel.errorMessageViewModel.observe(this, Observer { })
        viewModel.isLoginDataValidLiveData.observe(this, Observer { })

        textInputEditLogin.requestFocus()
    }

    override fun configureDataBinding(binding: ActivityAuthBinding) {
        binding.viewModel = viewModel
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
                        if (!viewModel.isKeyboardShown.value!!) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        }
                    }
                }
            }
        }
    }

    private fun closeKeyboard() {
        val view: View = this.currentFocus ?: return
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) {
        viewModel.isKeyboardShown.value = isShowing
    }
}
