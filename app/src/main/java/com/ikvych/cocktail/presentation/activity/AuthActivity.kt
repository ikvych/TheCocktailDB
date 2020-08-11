package com.ikvych.cocktail.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityAuthBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.fragment.SignInFragment
import com.ikvych.cocktail.presentation.fragment.SignUpFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class AuthActivity : BaseActivity<BaseViewModel, ActivityAuthBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_auth
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fcv_auth_activity, SignInFragment.newInstance())
        ft.commit()
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.tv_sign_up -> {
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fcv_auth_activity, SignUpFragment.newInstance())
                ft.commit()
            }
            R.id.tv_sign_in -> {
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fcv_auth_activity, SignInFragment.newInstance())
                ft.commit()
            }
        }
    }
}
