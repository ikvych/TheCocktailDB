package com.ikvych.cocktail.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.TextInputFilter
import com.ikvych.cocktail.listener.AuthTextWatcher
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.ErrorAuthDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.ActionSingleDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogType
import com.ikvych.cocktail.ui.dialog.base.type.NotificationDialogType
import com.ikvych.cocktail.viewmodel.AuthViewModel
import com.ikvych.cocktail.widget.custom.LinerLayoutWithKeyboardListener
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity<AuthViewModel>(),
    LinerLayoutWithKeyboardListener.KeyBoardListener {

    override var contentLayoutResId: Int = R.layout.activity_auth
    override val viewModel: AuthViewModel by viewModels()

    private var loginTextWatcher: TextWatcher = LoginTextWatcher()
    private var passwordTextWatcher: TextWatcher = PasswordTextWatcher()

    //фільтр який видалає пробіли
    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun configureView(savedInstanceState: Bundle?) {
        //кастомний linearLayout який визначає чи показана клавіатура чи прихована
        llwkl_auth_container.listener = this
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        tiet_auth_login.filters = arrayOf(inputFilter)
        tiet_auth_password.filters = arrayOf(inputFilter)

        viewModel.loginInputLiveData.value = tiet_auth_login.text.toString()
        viewModel.passwordInputLiveData.value = tiet_auth_password.text.toString()

        b_auth_login.setOnClickListener {
            closeKeyboard()
            //Якщо немає помилок у введених даних, стартую MainActivity.
            //У іншому випадку, визначаю в якому саме полі сталася помилка, щоб запросити фокус
            // на відповідне поле і показую діалог з причиною помилки
            if (viewModel.isAuthDataMatchPatternLiveData.value!!.first &&
                viewModel.isAuthDataMatchPatternLiveData.value!!.second &&
                viewModel.isAuthDataValidLiveData.value!!
            ) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                if (!viewModel.isAuthDataMatchPatternLiveData.value!!.second) {
                    tiet_auth_password.requestFocus()
                }
                if (!viewModel.isAuthDataMatchPatternLiveData.value!!.first) {
                    tiet_auth_login.requestFocus()
                }
                ErrorAuthDialogFragment.newInstance {
                    titleText = getString(R.string.auth_invalid_title)
                    leftButtonText = getString(R.string.all_ok_button)
                    descriptionText = viewModel.errorMessageLiveData.value!!
                }.show(supportFragmentManager, ErrorAuthDialogFragment::class.java.simpleName)
            }
        }

        tiet_auth_login.addTextChangedListener(loginTextWatcher)
        tiet_auth_password.addTextChangedListener(passwordTextWatcher)

        tiet_auth_login.requestFocus()
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
                        if (!viewModel.isKeyboardShownLiveData.value!!) {
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
        viewModel.isKeyboardShownLiveData.value = isShowing
    }
}
