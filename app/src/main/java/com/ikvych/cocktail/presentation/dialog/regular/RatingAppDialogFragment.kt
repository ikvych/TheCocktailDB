package com.ikvych.cocktail.presentation.dialog.regular

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.dialog.type.ActionSingleDialogButton
import com.ikvych.cocktail.presentation.dialog.type.NotificationDialogType
import com.ikvych.cocktail.presentation.dialog.base.SimpleBaseDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.RatingDialogType
import com.ikvych.cocktail.presentation.dialog.type.SingleDialogButton
import kotlinx.android.synthetic.main.layout_rating_bar.*


class RatingAppDialogFragment :
    SimpleBaseDialogFragment<Float, SingleDialogButton, RatingDialogType, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    override val dialogType: RatingDialogType =
        RatingDialogType
    override val extraContentLayoutResId: Int
        get() = R.layout.layout_rating_bar

    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override var data: Float? = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rb_app.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            data = rating
        }
    }

    override fun obtainDataForView(view: View): Float? {
        return data
    }

    override fun getButtonType(view: View): SingleDialogButton {
        return when (view.id) {
            R.id.b_dialog_left_button -> ActionSingleDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    companion object {
        fun newInstance(builder: SimpleDialogBuilder.() -> Unit): RatingAppDialogFragment {
            return getInstance(
                builder
            )
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleDialogBuilder.() -> Unit
        ): RatingAppDialogFragment {
            val fragment =
                RatingAppDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleDialogBuilder().apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}