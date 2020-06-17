package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {

    val login: String = "l"
    val password: String = "1"

    lateinit var loginView: EditText
    lateinit var passwordView: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        loginView = findViewById(R.id.login)
        passwordView = findViewById(R.id.password)
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

        loginView.addTextChangedListener(textWatcher)
        passwordView.addTextChangedListener(textWatcher)
        invalidateAuthData()
    }

    private fun onLoginButtonListener(): (v: View) -> Unit {
        return {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun invalidateAuthData() {
        val login: String = loginView.text.toString()
        val password = passwordView.text.toString()

        button.isEnabled = login == this.login && password == this.password
    }
}
