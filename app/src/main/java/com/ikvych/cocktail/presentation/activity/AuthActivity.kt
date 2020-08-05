package com.ikvych.cocktail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityAuthBinding
import com.ikvych.cocktail.presentation.filter.TextInputFilter
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.dialog.type.ActionSingleDialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType
import com.ikvych.cocktail.presentation.dialog.type.NotificationDialogType
import com.ikvych.cocktail.presentation.dialog.regular.ErrorAuthDialogFragment
import com.ikvych.cocktail.viewmodel.AuthViewModel
import com.ikvych.cocktail.widget.custom.LinerLayoutWithKeyboardListener
import kotlinx.android.synthetic.main.activity_auth.*
import kotlin.reflect.KClass

class AuthActivity : BaseActivity<AuthViewModel, ActivityAuthBinding>(),
    LinerLayoutWithKeyboardListener.KeyBoardListener {

    override var contentLayoutResId: Int = R.layout.activity_auth
    override val viewModelClass: KClass<AuthViewModel>
        get() = AuthViewModel::class

    private val inputFilter: InputFilter = TextInputFilter()

    private lateinit var inputMethodManager: InputMethodManager

    override fun configureView(savedInstanceState: Bundle?) {
        llwkl_auth_container.listener = this
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                ErrorAuthDialogFragment.newInstance {
                    titleText = getString(R.string.auth_invalid_title)
                    leftButtonText = getString(R.string.all_ok_button)
                    descriptionText = viewModel.errorMessageLiveData.value!!
                }.show(supportFragmentManager, ErrorAuthDialogFragment::class.java.simpleName)
            }
        })

        tiet_auth_login.requestFocus()
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