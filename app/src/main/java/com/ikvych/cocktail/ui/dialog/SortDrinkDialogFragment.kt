package com.ikvych.cocktail.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.get
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.ui.base.*
import kotlinx.android.synthetic.main.layout_dialog_drink_sort_component.*


class SortDrinkDialogFragment :
    SimpleBaseDialogFragment<SortDrinkType, RegularDialogButton, RegularDialogType, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    private lateinit var sortRadioGroup: RadioGroup

    override val dialogType: RegularDialogType = RegularDialogType
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_drink_sort_component

    override var dialogBuilder: SimpleDialogBuilder = SimpleDialogBuilder()
    override var data: SortDrinkType? = SortDrinkType.RECENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun obtainDataForView(view: View): SortDrinkType? {
        return data
    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.b_dialog_left_button -> LeftDialogButton
            R.id.b_dialog_right_button -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        this.data = requireArguments().get(EXTRA_KEY_DATA) as? SortDrinkType ?: SortDrinkType.RECENT

        sortRadioGroup = rg_drink_sort_container
        SortDrinkType.values().forEach {
            sortRadioGroup.addView(
                RadioButton(requireContext()).apply {
                    id = it.ordinal
                    text = it.key
                    if (it.key == data!!.key) {
                        isChecked = true
                    }
                }
            )
        }

        sortRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val button = group.get(checkedId) as RadioButton
            val text = button.text
            SortDrinkType.values().forEach {
                if (it.key == text) {
                    data = it
                    return@forEach
                }
            }
        }
    }

    companion object {
        fun newInstance(
            data: SortDrinkType? = null,
            builder: SimpleDialogBuilder.() -> Unit
        ): SortDrinkDialogFragment {
            return getInstance(data, builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            data: SortDrinkType? = null,
            builder: SimpleDialogBuilder.() -> Unit
        ): SortDrinkDialogFragment {
            val fragment = SortDrinkDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleDialogBuilder().apply(builder)),
                EXTRA_KEY_DATA to data
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_DATA = "EXTRA_KEY_DATA"
    }
}