package com.ikvych.cocktail.ui.base

import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.receiver.FlyModeReceiver


abstract class BaseActivity : AppCompatActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    View.OnClickListener, View.OnLongClickListener{

    private val flyModeReceiver: FlyModeReceiver = FlyModeReceiver()

    @CallSuper
    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment)?.onDialogFragmentDismiss(
            dialog,
            type,
            data
        )
    }

    @CallSuper
    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment)?.onDialogFragmentClick(
            dialog,
            buttonType,
            type,
            data
        )
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment)?.onBottomSheetDialogFragmentDismiss(
            dialog,
            type,
            data
        )
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment)?.onBottomSheetDialogFragmentClick(
            dialog,
            buttonType,
            type,
            data
        )
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(flyModeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(flyModeReceiver)
    }

    override fun onClick(v: View?) {
        //stub
    }

    override fun onLongClick(v: View?): Boolean {
        //stub
        return false
    }
}