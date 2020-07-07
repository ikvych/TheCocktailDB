package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentProfileBinding
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.activity.SplashActivity
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.dialog.bottom.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.bottom.SelectLanguageBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.regular.FilterDrinkAlcoholDialogFragment
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.ProfileFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<BaseViewModel, FragmentProfileBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_profile
    override val viewModel: ProfileFragmentViewModel by viewModels()

    private lateinit var logOut: Button
    private lateinit var startTestFragmentBtn: Button
    private lateinit var testFragment: TestFragment

    private val mainViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var bottomSheetDialogFragment: RegularBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialogFragment = RegularBottomSheetDialogFragment.newInstance {
            titleText = getString(R.string.profile_log_out_dialog_title)
            descriptionText = getString(R.string.profile_log_out_dialog_message)
            leftButtonText = getString(R.string.all_cancel_button)
            rightButtonText = getString(R.string.all_accept_button)
        }
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        logOut = view.findViewById(R.id.b_log_out)
        logOut.setOnClickListener {
            bottomSheetDialogFragment.show(
                childFragmentManager,
                RegularBottomSheetDialogFragment::class.java.simpleName
            )
        }
        startTestFragmentBtn = view.findViewById(R.id.b_start_test_fragment)
        testFragment = TestFragment.newInstance(5, "Ivan Kvych")
        startTestFragmentBtn.setOnClickListener {
            val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.add(
                R.id.fcv_profile_fragment,
                testFragment,
                TestFragment::class.java.simpleName
            )
            fragmentTransaction.addToBackStack(TestFragment::class.java.name)
            fragmentTransaction.commit()
        }

        // видимість титульного напису на BottomNavigationView по замовчуванню true
        s_main_nav_bar_title_visibility.isChecked =
            mainViewModel.navBarTitleVisibilityLiveData.value!!
        s_main_nav_bar_title_visibility.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.navBarTitleVisibilityLiveData.value = isChecked
        }

/*        s_battery_state_visibility.isChecked = mainViewModel.showBatteryStateLiveData.value!!
        s_battery_state_visibility.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), R.string.profile_fragment_battery_state_shown, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), R.string.profile_fragment_battery_state_hidden, Toast.LENGTH_SHORT).show()
            }
            mainViewModel.showBatteryStateLiveData.value = isChecked }*/
        s_battery_state_visibility.setOnCheckedChangeListener { _, isChecked ->
            SelectLanguageBottomSheetDialogFragment.newInstance()
                .show(childFragmentManager, SelectLanguageBottomSheetDialogFragment::class.java.simpleName)
/*            val intent = Intent(requireContext(), SplashActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK)
            requireActivity().startActivity(intent)*/
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
                        val intent = Intent(requireContext(), AuthActivity::class.java)
                        requireContext().startActivity(intent)
                        requireActivity().finish()
                    }
                    LeftDialogButton -> {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}