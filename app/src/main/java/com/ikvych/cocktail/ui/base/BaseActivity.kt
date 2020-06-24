package com.ikvych.cocktail.ui.base

import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.receiver.FlyModeReceiver


abstract class BaseActivity : AppCompatActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>> {

    @CallSuper
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

    @CallSuper
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

    private val flyModeReceiver: FlyModeReceiver = FlyModeReceiver()

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
}