package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.LeftDialogButton
import com.ikvych.cocktail.ui.base.RegularDialogButton
import com.ikvych.cocktail.ui.base.RegularDialogType
import com.ikvych.cocktail.ui.base.RightDialogButton


class RegularBottomSheetDialogFragment<Data> :
    SimpleBaseDialogFragment<Data, RegularDialogButton, RegularDialogType, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    override val dialogType: RegularDialogType = RegularDialogType
/*    override val dialogBuilder: SimpleDialogBuilder by instanceState(SimpleDialogBuilder(), EXTRA_KEY_BUILDER)
    override val data: Data? by instanceStateNullable(customKey = EXTRA_KEY_DATA)*/

    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override var data: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder= requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.lb_dialog_bs_left -> LeftDialogButton
            R.id.lb_dialog_bs_right -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }




    companion object {
        fun newInstance(builder: SimpleDialogBuilder.() -> Unit): RegularBottomSheetDialogFragment<Any?> {
            return newInstance(null, builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun <Data> newInstance(
            data: Data? = null,
            builder: SimpleDialogBuilder.() -> Unit
        ): RegularBottomSheetDialogFragment<Data> {
            val fragment = RegularBottomSheetDialogFragment<Data>()
            fragment.arguments = bundleOf (
                EXTRA_KEY_BUILDER to (SimpleDialogBuilder().apply(builder)),
                EXTRA_KEY_DATA to data
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_DATA = "EXTRA_KEY_DATA"
    }
}