package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.dialog.base.SimpleBottomSheetBaseDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.LeftDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.RegularDialogButton
import com.ikvych.cocktail.ui.dialog.base.type.RegularDialogType
import com.ikvych.cocktail.ui.dialog.base.type.RightDialogButton


open class RegularBottomSheetDialogFragment :
    SimpleBottomSheetBaseDialogFragment<Any, RegularDialogButton, RegularDialogType, SimpleBottomSheetBaseDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val dialogType: RegularDialogType = RegularDialogType

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
        fun newInstance(builder: SimpleBottomSheetDialogBuilder.() -> Unit): RegularBottomSheetDialogFragment {
            return getInstance(builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleBottomSheetDialogBuilder.() -> Unit
        ): RegularBottomSheetDialogFragment {
            val fragment = RegularBottomSheetDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleBottomSheetDialogBuilder().apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}