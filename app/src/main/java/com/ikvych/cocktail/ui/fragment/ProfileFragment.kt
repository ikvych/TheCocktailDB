package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.ProfileFragmentViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var logOut: Button
    private lateinit var startTestFragmentBtn: Button
    private lateinit var testFragment: TestFragment

    private lateinit var changeBottomNavBarTitleVisibility: CheckBox

    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var profileViewModel: ProfileFragmentViewModel

    private lateinit var bottomSheetDialogFragment: RegularBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialogFragment = RegularBottomSheetDialogFragment.newInstance {
            titleText = "Log Out"
            descriptionText = "Are you Really want to exit?"
            leftButtonText = "Cancel"
            rightButtonText = "Accept"
        }
        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
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
        startTestFragmentBtn = view.findViewById(R.id.b_test_fragment)
        startTestFragmentBtn.setOnClickListener {
            testFragment = TestFragment.newInstance(R.layout.fragment_test, 5, "Ivan Kvych")
            val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fcv_main, testFragment)
            fragmentTransaction.addToBackStack(TestFragment::class.java.name)
            fragmentTransaction.commit()
        }

        changeBottomNavBarTitleVisibility = cb_show_hide_main_nav_bar_title
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
        fun newInstance(fragmentId: Int) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }
}