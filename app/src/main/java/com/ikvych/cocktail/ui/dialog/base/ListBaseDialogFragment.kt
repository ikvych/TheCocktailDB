package com.ikvych.cocktail.ui.dialog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.FrameLayout
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.base.BaseAdapter
import com.ikvych.cocktail.adapter.list.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_dialog_filter_list_component.*

abstract class ListBaseDialogFragment<
        Data,
        ButtonType : DialogButton,
        Type : DialogType<ButtonType>>
protected constructor() : SimpleBaseDialogFragment<Data, ButtonType, Type, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    override val contentLayoutResId = R.layout.layout_dialog_simple
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_filter_list_component

    abstract val dialogListDataAdapter: DialogListDataAdapter<Data>
    protected open val listAdapter: BaseAdapter<Data, *> = DialogListAdapter()

    open var listData: List<Data> = mutableListOf()

    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        super.configureExtraContent(container, savedInstanceState)
        listAdapter.newData = listData
        rv_dialog_sort_list.apply {
            setHasFixedSize(true)
            adapter = this@ListBaseDialogFragment.listAdapter
        }
    }

    protected inner class DialogListAdapter : BaseAdapter<Data, BaseViewHolder>(R.layout.item_dialog_filter_list),
        View.OnClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
            return BaseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            with(holder.itemView as CheckedTextView) {
                text = dialogListDataAdapter.getName(listData[position])
                tag = listData[position]
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

    }

    interface DialogListDataAdapter<Data> {
        fun getName(data: Data): CharSequence
    }
}