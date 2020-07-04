package com.ikvych.cocktail.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.TextInputFilter
import com.ikvych.cocktail.listener.AuthTextWatcher
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.ErrorAuthDialogFragment
import com.ikvych.cocktail.viewmodel.AuthViewModel
import com.ikvych.cocktail.widget.custom.LinerLayoutWithKeyboardListener


const val EXTRA_KEY_LOGIN = "EXTRA_KEY_LOGIN"
const val EXTRA_KEY_PASSWORD = "EXTRA_KEY_PASSWORD"

class AuthActivity : BaseActivity<AuthViewModel>(),
    LinerLayoutWithKeyboardListener.KeyBoardListener {

    override var contentLayoutResId: Int = R.layout.activity_auth
    override val viewModel: AuthViewModel by viewModels()

    private var loginTextWatcher: TextWatcher? = null
    private var passwordTextWatcher: TextWatcher? = null

    private lateinit var loginInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

    private lateinit var textInputEditLogin: TextInputEditText
    private lateinit var textInputEditPassword: TextInputEditText

    private lateinit var submitButton: Button
    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_KEY_LOGIN, textInputEditLogin.text.toString())
        outState.putString(EXTRA_KEY_PASSWORD, textInputEditPassword.text.toString())
    }

    override fun configureView(savedInstanceState: Bundle?) {
        val keyboardObserver =
            findViewById<LinerLayoutWithKeyboardListener>(R.id.llwkl_auth_container)
        keyboardObserver.listener = this
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        loginInputLayout = findViewById(R.id.til_auth_login)
        passwordInputLayout = findViewById(R.id.til_auth_password)

        loginTextWatcher = LoginTextWatcher()
        passwordTextWatcher = PasswordTextWatcher()


        textInputEditLogin = findViewById(R.id.tiet_auth_login)
        textInputEditLogin.filters = arrayOf(inputFilter)

        textInputEditPassword = findViewById(R.id.tiet_auth_password)
        textInputEditPassword.filters = arrayOf(inputFilter)

        viewModel.loginInputLiveData.value = textInputEditLogin.text.toString()
        viewModel.passwordInputLiveData.value = textInputEditPassword.text.toString()

        if (savedInstanceState != null) {
            textInputEditLogin.setText(savedInstanceState.getString(EXTRA_KEY_LOGIN))
            textInputEditPassword.setText(savedInstanceState.getString(EXTRA_KEY_PASSWORD))
        }

        submitButton = findViewById(R.id.b_auth_login)

        submitButton.setOnClickListener {
            closeKeyboard()

            if (viewModel.isLoginDataMatchPatternLiveData.value!!.first &&
                viewModel.isLoginDataMatchPatternLiveData.value!!.second &&
                viewModel.isLoginDataValidLiveData.value!!
            ) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
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

        textInputEditLogin.addTextChangedListener(loginTextWatcher)
        textInputEditPassword.addTextChangedListener(passwordTextWatcher)

        viewModel.isLoginDataMatchPatternLiveData.observe(this, Observer { })
        viewModel.errorMessageViewModel.observe(this, Observer { })
        viewModel.isLoginDataValidLiveData.observe(this, Observer { })

        textInputEditLogin.requestFocus()

    }

    inner class LoginTextWatcher : AuthTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            viewModel.loginInputLiveData.value = s.toString()
        }
    }

    inner class PasswordTextWatcher : AuthTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            viewModel.passwordInputLiveData.value = s.toString()
        }
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
