package com.ikvych.cocktail.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.TextInputFilter
import com.ikvych.cocktail.listener.AuthTextWatcher
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.ErrorAuthDialogFragment
import com.ikvych.cocktail.widget.custom.LinerLayoutWithKeyboardListener
import java.util.regex.Pattern


class AuthActivity : BaseActivity(), LinerLayoutWithKeyboardListener.KeyBoardListener {

    private var isValidLogin: Boolean = false
    private var isValidPassword: Boolean = false
    private var isKeyboardShown: Boolean = false

    private lateinit var loginErrorMessage: String
    private lateinit var passwordErrorMessage: String

    private var loginTextWatcher: TextWatcher? = null
    private var passwordTextWatcher: TextWatcher? = null

    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private lateinit var loginInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

    private lateinit var textInputEditLogin: TextInputEditText
    private lateinit var textInputEditPassword: TextInputEditText

    private lateinit var submitButton: Button
    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)



        val keyboardObserver = findViewById<LinerLayoutWithKeyboardListener>(R.id.login_root)
        keyboardObserver.listener = this
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        loginInputLayout = findViewById(R.id.til_login)
        passwordInputLayout = findViewById(R.id.til_password)

        loginTextWatcher = AuthTextWatcher(loginInputLayout)
        passwordTextWatcher = AuthTextWatcher(passwordInputLayout)

        textInputEditLogin = findViewById(R.id.tiet_login)
        textInputEditLogin.filters = arrayOf(inputFilter)

        textInputEditPassword = findViewById(R.id.tiet_password)
        textInputEditPassword.filters = arrayOf(inputFilter)

        submitButton = findViewById(R.id.button)

        submitButton.setOnClickListener(onLoginButtonListener())

        textInputEditLogin.addTextChangedListener(loginTextWatcher)
        textInputEditPassword.addTextChangedListener(passwordTextWatcher)

        loginErrorMessage = resources.getString(R.string.invalid_login_text)
        passwordErrorMessage = resources.getString(R.string.invalid_password_text)

        textInputEditLogin.requestFocus()
    }

    private fun onLoginButtonListener(): (v: View) -> Unit {
        return {
            invalidateAuthData()
            if (isValidLogin && isValidPassword) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                var finalErrorMessage: String? = null
                if (!isValidLogin) {
                    finalErrorMessage = loginErrorMessage
                }
                if (!isValidPassword) {
                    if (finalErrorMessage != null) {
                        finalErrorMessage += "\n${passwordErrorMessage}"
                    } else {
                        finalErrorMessage = passwordErrorMessage
                    }
                }
                ErrorAuthDialogFragment.newInstance(){
                    titleText = "Invalid data"
                    leftButtonText = "Ok"
                    descriptionText = finalErrorMessage!!
                }.show(supportFragmentManager, ErrorAuthDialogFragment::class.java.simpleName)
            }
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
                        if (!isKeyboardShown) {
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        }
                    }
                }
            }
        }
    }

    private fun invalidateAuthData() {
        val login = textInputEditLogin.text.toString()
        val password = textInputEditPassword.text.toString()

        closeKeyboard()

        if (passwordPattern.matcher(password).matches()) {
            isValidPassword = true
        } else {
            isValidPassword = false
            textInputEditPassword.requestFocus()
        }

        if (loginPattern.matcher(login).matches()) {
            isValidLogin = true
        } else {
            isValidLogin = false
            textInputEditLogin.requestFocus()
        }
    }

    private fun closeKeyboard() {
        val view: View = this.currentFocus ?: return
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) {
        isKeyboardShown = isShowing
    }
}
