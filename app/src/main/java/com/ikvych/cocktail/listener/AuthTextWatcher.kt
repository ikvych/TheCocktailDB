package com.ikvych.cocktail.listener

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AuthTextWatcher(
    private val textInputLayout: TextInputLayout
) : TextWatcher {


    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (textInputLayout.isErrorEnabled) {
            textInputLayout.isErrorEnabled = false
        }
    }
}