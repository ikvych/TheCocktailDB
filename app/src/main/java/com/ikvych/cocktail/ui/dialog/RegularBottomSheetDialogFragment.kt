package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import androidx.core.os.bundleOf


class RegularBottomSheetDialogFragment<Data> :
    SimpleBaseDialogFragment<Data, SimpleBaseDialogFragment.SimpleDialogBuilder>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogBuilder= requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override var dialogBuilder: SimpleDialogBuilder =SimpleDialogBuilder()
    override var data: Data? =null


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