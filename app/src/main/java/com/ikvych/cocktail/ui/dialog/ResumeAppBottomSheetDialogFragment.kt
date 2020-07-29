package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.dialog.type.LeftDialogButton
import com.ikvych.cocktail.ui.dialog.type.RegularDialogButton
import com.ikvych.cocktail.ui.dialog.type.ResumeApplicationDialogType
import com.ikvych.cocktail.ui.dialog.type.RightDialogButton


class ResumeAppBottomSheetDialogFragment :
    SimpleBottomSheetBaseDialogFragment<Any, RegularDialogButton, ResumeApplicationDialogType, SimpleBottomSheetBaseDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val dialogType: ResumeApplicationDialogType =
        ResumeApplicationDialogType

    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()
    override var data: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun obtainDataForView(view: View): Any? {
        return data
    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.b_dialog_left_button -> LeftDialogButton
            R.id.b_dialog_right_button -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    companion object {
        fun newInstance(builder: SimpleBottomSheetDialogBuilder.() -> Unit): ResumeAppBottomSheetDialogFragment {
            return getInstance(
                builder
            )
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleBottomSheetDialogBuilder.() -> Unit
        ): ResumeAppBottomSheetDialogFragment {
            val fragment =
                ResumeAppBottomSheetDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleBottomSheetDialogBuilder().apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}