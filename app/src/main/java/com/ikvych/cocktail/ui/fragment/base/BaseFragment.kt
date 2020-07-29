package com.ikvych.cocktail.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ikvych.cocktail.ui.dialog.base.BaseBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.base.BaseDialogFragment
import com.ikvych.cocktail.ui.dialog.type.DialogButton
import com.ikvych.cocktail.ui.dialog.type.DialogType
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class BaseFragment<ViewModel : BaseViewModel, DataBinding: ViewDataBinding> : Fragment(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>{

    protected abstract var contentLayoutResId: Int
    protected abstract val viewModel: ViewModel
    protected lateinit var dataBinding: DataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyLog", "onCreateView - ${this.toString()}")
        dataBinding = DataBindingUtil.inflate(inflater, contentLayoutResId, container, false)
        dataBinding.lifecycleOwner = this@BaseFragment
        configureDataBinding(dataBinding)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureView(view, savedInstanceState)
        Log.d("MyLog", "onViewCreated - ${this.toString()}")
    }

    protected open fun configureDataBinding(binding: DataBinding) {
        //stub
    }

    protected open fun configureView(view: View, savedInstanceState: Bundle?) {
        // stub
    }

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("MyLog", "onAttach - ${this.toString()}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyLog", "onCreate - ${this.toString()}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MyLog", "onStart - ${this.toString()}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyLog", "onResume - ${this.toString()}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyLog", "onPause - ${this.toString()}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MyLog", "onStop - ${this.toString()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MyLog", "onDestroyView - ${this.toString()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLog", "onDestroy - ${this.toString()}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MyLog", "onDetach - ${this.toString()}")
    }
}