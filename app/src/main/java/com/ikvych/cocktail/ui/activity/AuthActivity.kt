package com.ikvych.cocktail.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.TextInputFilter
import com.ikvych.cocktail.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.regex.Pattern


class AuthActivity : BaseActivity() {

    private val login: String = "lllllll"
    private val password: String = "111111l"

    private val passwordPattern: Pattern =
        Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z~!@#\$%^&*]{6,}") //не менше 6 символів і містить хоча б одну цифру і хоча б одну літеру
    private val loginPattern: Pattern = Pattern.compile(".{7,}") //більше 6 символів

    private lateinit var textInputLogin: TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    private lateinit var textInputEditLogin: TextInputEditText
    private lateinit var textInputEditPassword: TextInputEditText

    private lateinit var submitButton: Button
    private val inputFilter: InputFilter = TextInputFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        textInputLogin = findViewById(R.id.til_login)
        textInputPassword = findViewById(R.id.til_password)

        textInputEditLogin = findViewById(R.id.tiet_login)
        textInputEditLogin.filters = arrayOf(inputFilter)

        textInputEditPassword = findViewById(R.id.tiet_password)
        textInputEditPassword.filters = arrayOf(inputFilter)

        submitButton = findViewById(R.id.button)

        submitButton.setOnClickListener(onLoginButtonListener())

        val textWatcher: TextWatcher = object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                invalidateAuthData()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }

        textInputEditLogin.addTextChangedListener(textWatcher)
        textInputEditPassword.addTextChangedListener(textWatcher)
        invalidateAuthData()
    }

    private fun onLoginButtonListener(): (v: View) -> Unit {
        return {
            val intent = Intent(this, MainActivity::class.java)
            closeKeyboard()
            startActivity(intent)
        }
    }

    private fun invalidateAuthData() {
        val login: String = textInputEditLogin.text.toString()
        val password = textInputEditPassword.text.toString()

        if (passwordPattern.matcher(password).matches() && loginPattern.matcher(login).matches()) {
            button.isEnabled = login == this.login && password == this.password
        }
    }

    private fun closeKeyboard() {
        val view: View = this.currentFocus ?: return
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
