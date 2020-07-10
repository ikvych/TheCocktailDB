package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.dialog.base.type.LeftDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.RegularDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.ResumeApplicationDialogType
import com.ikvych.cocktail.ui.dialog.base.type.RightDialogButton


class ResumeAppBottomSheetDialogFragment :
    SimpleBottomSheetBaseDialogFragment<Long, RegularDialogButton, ResumeApplicationDialogType, SimpleBottomSheetBaseDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val dialogType: ResumeApplicationDialogType = ResumeApplicationDialogType
    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()
    override var data: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        data = requireArguments().getLong(EXTRA_KEY_DRINK_ID)
    }

    override fun obtainDataForView(view: View): Long? {
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
        fun newInstance(
            drinkId: Long,
            builder: SimpleBottomSheetDialogBuilder.() -> Unit
        ): ResumeAppBottomSheetDialogFragment {
            return getInstance(drinkId, builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            drinkId: Long,
            builder: SimpleBottomSheetDialogBuilder.() -> Unit
        ): ResumeAppBottomSheetDialogFragment {
            val fragment = ResumeAppBottomSheetDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleBottomSheetDialogBuilder().apply(builder)),
                EXTRA_KEY_DRINK_ID to drinkId
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_DRINK_ID = "EXTRA_KEY_DRINK_ID"
    }
}