package com.ikvych.cocktail.ui.base

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.receiver.FlyModeReceiver


abstract class BaseActivity : AppCompatActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Drink>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Drink> {

    override fun onBottomSheetDialogFragmentDismiss(dialog: DialogFragment, data: Drink?) {

    }

    override fun onBottomSheetDialogFragmentClick(dialog: DialogFragment, data: Drink?) {

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