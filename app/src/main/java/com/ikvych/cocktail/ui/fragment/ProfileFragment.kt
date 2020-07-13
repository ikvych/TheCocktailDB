package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentProfileBinding
import com.ikvych.cocktail.ui.activity.AuthActivity
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.*
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.ProfileFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<BaseViewModel, FragmentProfileBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_profile
    override val viewModel: ProfileFragmentViewModel by viewModels()

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

    override fun configureDataBinding(binding: FragmentProfileBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
        binding.parentViewModel = mainViewModel
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        viewModel.logOutLiveData.observe(this, Observer {
            if (it) {
                bottomSheetDialogFragment.show(
                    childFragmentManager,
                    RegularBottomSheetDialogFragment::class.java.simpleName
                )
                viewModel.logOutLiveData.value = false
            }
        })

        viewModel.startTestFragmentLiveData.observe(this, Observer {
            if (it) {
                val fragmentTransaction: FragmentTransaction =
                    childFragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.fcv_profile_fragment,
                    TestFragment.newInstance(5, "Ivan Kvych"),
                    TestFragment::class.java.simpleName
                )
                fragmentTransaction.addToBackStack(TestFragment::class.java.name)
                fragmentTransaction.commit()
                viewModel.startTestFragmentLiveData.value = false
            }
        })
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