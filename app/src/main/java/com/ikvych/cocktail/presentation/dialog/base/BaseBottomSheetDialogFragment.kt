package com.ikvych.cocktail.presentation.dialog.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType

abstract class BaseBottomSheetDialogFragment<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> protected constructor() :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    protected var onDialogClickListener: OnBottomSheetDialogFragmentClickListener<Data, ButtonType, Type>? =
        null
        private set
    protected var onDialogDismissListener: OnBottomSheetDialogFragmentDismissListener<Data, ButtonType, Type>? =
        null
        private set

    protected abstract val dialogType: Type
    protected open var data: Data? = null
    protected abstract val contentLayoutResId: Int

    private val clickableViews = mutableListOf<View>()

    init {
        this.setStyle(STYLE_NO_TITLE, R.style.DialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentLayoutResId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickableViews.clear()
        clickableViews.addAll(obtainClickableViews())
        clickableViews.forEach {
            it.setOnClickListener(this)
        }
    }

    protected open fun obtainClickableViews(): List<View> {
        return emptyList()
    }

    protected abstract fun getButtonType(view: View): ButtonType

    @Suppress("UNUSED_PARAMETER")
    protected open fun obtainDataForView(view: View): Data? {
        return data
    }

    override fun onClick(v: View?) {
        val buttonType = getButtonType(v ?: return)
        callOnClick(v, buttonType)
    }

    protected open fun callOnClick(v: View, buttonType: ButtonType) {
        onDialogClickListener?.apply {
            val data = obtainDataForView(v)

            val acceptClick = this.shouldBottomSheetDialogFragmentAcceptClick(
                this@BaseBottomSheetDialogFragment,
                dialogType,
                buttonType,
                data
            )

            if (!acceptClick) return

            this.onBottomSheetDialogFragmentClick(
                this@BaseBottomSheetDialogFragment,
                buttonType,
                dialogType,
                data
            )
        }

        dismiss()
    }

    @Suppress("UNUSED_PARAMETER")
    protected open fun configureDialog(dialog: Dialog) {
        //stub
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.also {
            it.attributes.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        }
        dialog.setOnDismissListener(this)
        configureDialog(dialog)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissListener?.onBottomSheetDialogFragmentDismiss(this, dialogType,  data)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is OnBottomSheetDialogFragmentClickListener<*, *, *>) {
            "Bottom Sheet Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnBottomSheetDialogFragmentClickListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogClickListener = context as? OnBottomSheetDialogFragmentClickListener<Data, ButtonType, Type>
        check(context is OnBottomSheetDialogFragmentDismissListener<*, *, *>) {
            "Bottom Sheet Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnBottomSheetDialogFragmentDismissListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogDismissListener =
            context as? OnBottomSheetDialogFragmentDismissListener<Data, ButtonType, Type>
    }

    override fun onDetach() {
        super.onDetach()
        onDialogClickListener = null
        onDialogDismissListener = null
    }

    interface OnBottomSheetDialogFragmentDismissListener<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> {
        fun onBottomSheetDialogFragmentDismiss(
            dialog: DialogFragment,
            type: Type,
            data: Data?
        )
    }

    interface OnBottomSheetDialogFragmentClickListener<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> {
        fun onBottomSheetDialogFragmentClick(
            dialog: DialogFragment,
            buttonType: ButtonType,
            type: Type,
            data: Data?
        )

        fun shouldBottomSheetDialogFragmentAcceptClick(
            dialog: DialogFragment,
            dialogType: Type,
            buttonType: ButtonType,
            data: Data?
        ): Boolean {
            return true
        }
    }
}