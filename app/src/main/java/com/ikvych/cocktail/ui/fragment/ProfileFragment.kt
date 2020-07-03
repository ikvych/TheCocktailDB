package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.ProfileFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<ProfileFragmentViewModel>() {

    override var contentLayoutResId: Int = R.layout.fragment_profile
    override val viewModel: ProfileFragmentViewModel by viewModels()

    private lateinit var logOut: Button
    private lateinit var startTestFragmentBtn: Button
    private lateinit var testFragment: TestFragment

    private lateinit var changeBottomNavBarTitleVisibility: CheckBox

    private lateinit var mainViewModel: MainActivityViewModel

    private lateinit var bottomSheetDialogFragment: RegularBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialogFragment = RegularBottomSheetDialogFragment.newInstance{
            titleText = getString(R.string.profile_log_out_dialog_title)
            descriptionText = getString(R.string.profile_log_out_dialog_message)
            leftButtonText = getString(R.string.all_cancel_button)
            rightButtonText = getString(R.string.all_accept_button)
        }
        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
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
        startTestFragmentBtn.setOnClickListener {
            testFragment = TestFragment.newInstance(5, "Ivan Kvych")
            val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fcv_container, testFragment)
            fragmentTransaction.addToBackStack(TestFragment::class.java.name)
            fragmentTransaction.commit()
        }

        changeBottomNavBarTitleVisibility = cb_main_nav_bar_title_visibility
        // видимість титульного напису на BottomNavigationView по замовчуванню true
        mainViewModel.navBarTitleVisibilityLiveData.value = changeBottomNavBarTitleVisibility.isChecked
        changeBottomNavBarTitleVisibility.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.navBarTitleVisibilityLiveData.value = isChecked }
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