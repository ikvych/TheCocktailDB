package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.comparator.type.SortOrder
import com.ikvych.cocktail.ui.base.*


class ErrorAuthDialogFragment :
    SimpleBaseDialogFragment<String, SingleDialogButton, NotificationDialogType, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    override val dialogType: NotificationDialogType = NotificationDialogType

    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun obtainDataForView(view: View): String? {
        return data
    }

    override fun getButtonType(view: View): SingleDialogButton {
        return when (view.id) {
            R.id.lb_dialog_bs_left -> ActionSingleDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    companion object {
        fun newInstance(builder: SimpleDialogBuilder.() -> Unit): ErrorAuthDialogFragment {
            return getInstance(builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleDialogBuilder.() -> Unit
        ): ErrorAuthDialogFragment {
            val fragment = ErrorAuthDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleDialogBuilder().apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}