package com.ikvych.cocktail.ui.dialog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.dialog.type.DialogButton
import com.ikvych.cocktail.ui.dialog.type.DialogType
import kotlinx.android.synthetic.main.layout_dialog_filter_list_component.*

abstract class ListBaseBottomSheetDialogFragment<
        Data,
        ButtonType : DialogButton,
        Type : DialogType<ButtonType>>
protected constructor() : SimpleBottomSheetBaseDialogFragment<Data, ButtonType, Type, SimpleBottomSheetBaseDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val contentLayoutResId = R.layout.layout_bottom_sheet_dialog_simple
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_filter_list_component

    abstract val dialogListDataAdapter: DialogListDataAdapter<Data>
    protected open lateinit var listAdapter: DialogListAdapter

    open var listData: List<Data> = mutableListOf()

    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        super.configureExtraContent(container, savedInstanceState)
        listAdapter.newData = listData
        rv_dialog_sort_container.apply {
            setHasFixedSize(true)
            adapter = this@ListBaseBottomSheetDialogFragment.listAdapter
        }
    }

    protected inner class DialogListAdapter(private val selectedButton: Any? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        View.OnClickListener {

        private val layoutId = R.layout.item_button_list

        var newData: List<Data> = arrayListOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int {
            return newData.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with(holder.itemView as AppCompatButton) {
                text = dialogListDataAdapter.getName(newData[position])
                tag = newData[position]
                isEnabled = (tag != selectedButton)
                setOnClickListener(this@DialogListAdapter)
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun onClick(v: View?) {
            /**
             * be sure to override method [obtainDataForView] and handle your model [Data]
             */
            callOnClick(v ?: return, getButtonType(v))
        }

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    }

    interface DialogListDataAdapter<Data> {
        fun getName(data: Data): CharSequence
    }
}