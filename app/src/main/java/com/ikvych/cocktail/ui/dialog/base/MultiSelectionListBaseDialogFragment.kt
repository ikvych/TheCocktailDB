package com.ikvych.cocktail.ui.dialog.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.dialog.base.type.DialogButton
import com.ikvych.cocktail.ui.dialog.base.type.DialogType
import kotlinx.android.synthetic.main.layout_dialog_filter_list_component.*

abstract class MultiSelectionListBaseDialogFragment<
        Data,
        Element,
        ButtonType : DialogButton,
        Type : DialogType<ButtonType>>
protected constructor() : SimpleBaseDialogFragment<Data, ButtonType, Type, SimpleBaseDialogFragment.SimpleDialogBuilder>() {

    interface OnMultiSelectionListClick {
        fun onListItemClick(v: View?)
    }

    override val contentLayoutResId = R.layout.layout_dialog_simple
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_filter_list_component

    abstract val dialogListDataAdapter: DialogListDataAdapter<Element>
    abstract val selectedElements: ArrayList<Element>
    abstract val onClickListener: OnMultiSelectionListClick
    protected open lateinit var listAdapter: DialogListAdapter

    open var listElement: List<Element> = mutableListOf()

    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        super.configureExtraContent(container, savedInstanceState)
        listAdapter.newElements = listElement
        rv_dialog_sort_container.apply {
            setHasFixedSize(true)
            adapter = this@MultiSelectionListBaseDialogFragment.listAdapter
        }
    }

    protected inner class DialogListAdapter() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        View.OnClickListener {

        private val layoutId = R.layout.item_multi_filter_type

        var newElements: List<Element> = arrayListOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int {
            return newElements.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with(holder.itemView as AppCompatButton) {
                text = dialogListDataAdapter.getName(newElements[position])
                tag = newElements[position]
                var isEnabledButton = true
                for (i in selectedElements.indices) {
                    if (tag == selectedElements[i]) {
                        isEnabledButton = false
                        break
                    }
                }
/*                val stateList =
                    ColorStateList.valueOf(
                        Color.GRAY
                    )
                val stateList2 =
                    ColorStateList.valueOf(
                        Color.GREEN
                    )
                backgroundTintList = if (!isEnabledButton) {
                    stateList
                } else {
                    stateList2
                }*/
                isSelected = !isEnabledButton
                setOnClickListener(this@DialogListAdapter)
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun onClick(v: View?) {
            onClickListener.onListItemClick(v)
        }

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    }

    interface DialogListDataAdapter<Element> {
        fun getName(data: Element): CharSequence
    }
}
