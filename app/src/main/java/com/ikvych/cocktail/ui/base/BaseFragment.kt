package com.ikvych.cocktail.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ikvych.cocktail.data.entity.Drink

const val FRAGMENT_ID = "com.ikvych.cocktail.ViewId"

abstract class BaseFragment() : Fragment(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>{

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyLog", "onCreateView - ${this.toString()}")
        return inflater.inflate(requireArguments().getInt(FRAGMENT_ID), container, false)
    }

    protected open fun configureView(view: View, savedInstanceState: Bundle?) {
        // stub
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureView(view, savedInstanceState)
        Log.d("MyLog", "onViewCreated - ${this.toString()}")
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