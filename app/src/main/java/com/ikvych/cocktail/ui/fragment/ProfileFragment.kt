package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.fragment.base.BaseFragment

class ProfileFragment : BaseFragment() {

    override var contentLayoutResId: Int = R.layout.fragment_profile
    private lateinit var logOut: Button
    private lateinit var startTestFragmentBtn: Button
    private lateinit var testFragment: TestFragment

    private lateinit var bottomSheetDialogFragment: RegularBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetDialogFragment = RegularBottomSheetDialogFragment.newInstance {
            titleText = "Log Out"
            descriptionText = "Are you Really want to exit?"
            leftButtonText = "Cancel"
            rightButtonText = "Accept"
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
        startTestFragmentBtn = view.findViewById(R.id.b_test_fragment)
        startTestFragmentBtn.setOnClickListener {
            testFragment = TestFragment.newInstance( 5, "Ivan Kvych")
            val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fcv_profile_fragment, testFragment)
            fragmentTransaction.addToBackStack(TestFragment::class.java.name)
            fragmentTransaction.commit()
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
                        requireActivity().finishAffinity()
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