package com.ikvych.cocktail.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.api.ProfileStarter
import com.ikvych.cocktail.prresentation.dialog.type.DialogButton
import com.ikvych.cocktail.prresentation.dialog.type.DialogType
import com.ikvych.cocktail.prresentation.dialog.type.ItemListDialogButton
import com.ikvych.cocktail.prresentation.dialog.type.SelectLanguageDialogType
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import com.ikvych.cocktail.setting.databinding.FragmentSettingBinding
import kotlinx.android.synthetic.main.fragment_setting.*
import org.kodein.di.generic.instance
import kotlin.reflect.KClass

class SettingFragment :
    BaseFragment</*ProfileActivityViewModel*/BaseViewModel, FragmentSettingBinding>(),
    View.OnClickListener {

    interface SettingFragmentListener {

    }

    override var contentLayoutResId: Int = R.layout.fragment_setting
    private val profileStarter: ProfileStarter by instance()

    /*    override val viewModelClass: KClass<ProfileActivityViewModel>
            get() = ProfileActivityViewModel::class*/
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class
/*    private val mainViewModel: BaseViewModel
        get() {
            return ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        }*/

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        b_start_test_fragment.setOnClickListener(this)
        b_change_language.setOnClickListener(this)
        b_start_profile.setOnClickListener(this)
        b_start_custom_view.setOnClickListener(this)
        b_start_gif.setOnClickListener(this)
    }

    override fun configureDataBinding(binding: FragmentSettingBinding) {
        super.configureDataBinding(binding)
/*        binding.parentViewModel = mainViewModel*/
    }

    fun changeNavBarTitleVisibility() {

    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.b_start_gif -> {
                val fragmentTransaction: FragmentTransaction =
                    childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile_fragment,
                    GifFragment.newInstance(),
                    GifFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(GifFragment::class.java.name)
                fragmentTransaction.commit()
            }
/*            R.id.b_start_test_fragment -> {
                val fragmentTransaction: FragmentTransaction =
                    childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile_fragment,
                    MyMapFragmentFragment.newInstance(),
                    MyMapFragmentFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(MyMapFragmentFragment::class.java.name)
                fragmentTransaction.commit()
            }*/
/*            R.id.b_change_language -> {
                SelectLanguageBottomSheetDialogFragment.newInstance(
                    Language.values()[viewModel.selectedLanguageLiveData.value!!]
                )
                    .show(
                        childFragmentManager,
                        SelectLanguageBottomSheetDialogFragment::class.java.simpleName
                    )
            }*/
            R.id.b_start_profile -> {
                profileStarter.startProfile()
/*                startActivity(Intent(requireContext(), ProfileActivity::class.java))*/
            }
            R.id.b_start_custom_view -> {
                val fragmentTransaction: FragmentTransaction =
                    childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile_fragment,
                    CustomViewFragment.newInstance(),
                    CustomViewFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(CustomViewFragment::class.java.name)
                fragmentTransaction.commit()
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
/*                        val selectedLanguage = data as Language
                        viewModel.selectedLanguageLiveData.value = selectedLanguage.ordinal
                        val intent = Intent(
                            requireContext(),
                            com.ikvych.cocktail.splash.SplashActivity::class.java
                        )
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ).addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                        requireActivity().startActivity(intent)*/
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