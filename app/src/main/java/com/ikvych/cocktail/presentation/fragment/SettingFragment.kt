package com.ikvych.cocktail.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentSettingBinding
import com.ikvych.cocktail.presentation.activity.ProfileActivity
import com.ikvych.cocktail.presentation.activity.SplashActivity
import com.ikvych.cocktail.presentation.dialog.bottom.SelectLanguageBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.*
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.presentation.enumeration.Language
import com.ikvych.cocktail.viewmodel.cocktail.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.user.ProfileActivityViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlin.reflect.KClass

class SettingFragment : BaseFragment<ProfileActivityViewModel, FragmentSettingBinding>(),
    View.OnClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_setting
    override val viewModelClass: KClass<ProfileActivityViewModel>
        get() = ProfileActivityViewModel::class
    private val mainViewModel: MainActivityViewModel
        get() {
            return ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        b_start_test_fragment.setOnClickListener(this)
        b_change_language.setOnClickListener(this)
        b_start_profile.setOnClickListener(this)
    }

    override fun configureDataBinding(binding: FragmentSettingBinding) {
        super.configureDataBinding(binding)
        binding.parentViewModel = mainViewModel
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.b_start_test_fragment -> {
                val fragmentTransaction: FragmentTransaction =
                    childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile_fragment,
                    TestFragment.newInstance(5, "Ivan Kvych"),
                    TestFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(TestFragment::class.java.name)
                fragmentTransaction.commit()
            }
            R.id.b_change_language -> {
                SelectLanguageBottomSheetDialogFragment.newInstance(
                    Language.values()[viewModel.selectedLanguageLiveData.value!!]
                )
                    .show(
                        childFragmentManager,
                        SelectLanguageBottomSheetDialogFragment::class.java.simpleName
                    )
            }
            R.id.b_start_profile -> {
                startActivity(Intent(requireContext(), ProfileActivity::class.java))
            }
        }
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
        when (type) {
            SelectLanguageDialogType -> {
                when (buttonType) {
                    ItemListDialogButton -> {
                        val selectedLanguage = data as Language
                        viewModel.selectedLanguageLiveData.value = selectedLanguage.ordinal
                        val intent = Intent(requireContext(), SplashActivity::class.java)
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                        requireActivity().startActivity(intent)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }

}