package com.ikvych.cocktail.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentSettingBinding
import com.ikvych.cocktail.presentation.activity.AuthActivity
import com.ikvych.cocktail.presentation.activity.ProfileActivity
import com.ikvych.cocktail.presentation.activity.SplashActivity
import com.ikvych.cocktail.presentation.dialog.bottom.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.bottom.SelectLanguageBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.*
import com.ikvych.cocktail.presentation.extension.baseViewModels
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.util.Language
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.ProfileActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlin.reflect.KClass

class SettingFragment : BaseFragment<ProfileActivityViewModel, FragmentSettingBinding>(),
    View.OnClickListener {

    override var contentLayoutResId: Int = R.layout.fragment_setting
    override val viewModelClass: KClass<ProfileActivityViewModel>
        get() = ProfileActivityViewModel::class
    private val mainViewModel: MainActivityViewModel by activityViewModels()
    private val profileViewModel: ProfileActivityViewModel by baseViewModels()

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        b_log_out.setOnClickListener(this)
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
            R.id.b_log_out -> {
                RegularBottomSheetDialogFragment.newInstance {
                    titleText = getString(R.string.profile_log_out_dialog_title)
                    descriptionText = getString(R.string.profile_log_out_dialog_message)
                    leftButtonText = getString(R.string.all_cancel_button)
                    rightButtonText = getString(R.string.all_accept_button)
                }.show(
                    childFragmentManager,
                    RegularBottomSheetDialogFragment::class.java.simpleName
                )
            }
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
            RegularDialogType -> {
                when (buttonType) {
                    RightDialogButton -> {
                        profileViewModel.removeUser()
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        requireContext().startActivity(intent)
                        requireActivity().finish()
                    }
                    LeftDialogButton -> {
                        dialog.dismiss()
                    }
                }
            }
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