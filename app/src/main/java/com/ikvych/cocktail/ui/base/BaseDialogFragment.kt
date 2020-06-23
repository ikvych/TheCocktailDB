package com.ikvych.cocktail.ui.base

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikvych.cocktail.R

abstract class BaseDialogFragment<Data> protected constructor() :
    DialogFragment(),
    View.OnClickListener{

    protected var onDialogClickListener: OnDialogFragmentClickListener<Data>? =
        null
        private set
    protected var onDialogDismissListener: OnDialogFragmentDismissListener<Data>? =
        null
        private set

    protected open var data: Data? = null
    protected abstract val contentLayoutResId: Int

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
    }

    @Suppress("UNUSED_PARAMETER")
    protected open fun obtainDataForView(view: View): Data? {
        return data
    }

    override fun onClick(v: View?) {
        callOnClick(v?:return)
    }

    protected open fun callOnClick(v: View) {
        onDialogClickListener?.apply {
            val data = obtainDataForView(v)
            this.onBottomSheetDialogFragmentClick(
                this@BaseDialogFragment,
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
            it.attributes.windowAnimations = R.style.DialogFragment
        }
        dialog.setOnDismissListener(this)
        configureDialog(dialog)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissListener?.onBottomSheetDialogFragmentDismiss(this, data)
    }

    //region Show Dialog Methods
    override fun show(manager: FragmentManager, tag: String?) {
        showFragmentInternal(manager, tag ?: this::class.java.name)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return super.show(transaction, tag)
    }

    fun show(manager: FragmentManager) {
        showFragmentInternal(manager, this::class.java.name)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        showFragmentInternal(manager, tag ?: this::class.java.name, true)
    }

    fun showNow(manager: FragmentManager) {
        showFragmentInternal(manager, this::class.java.name, true)
    }

    protected open fun showFragmentInternal(manager: FragmentManager, tag: String, showNow: Boolean = false): Int {
        // workaround to handle gap if multiple dialog instances are called
        // before first fragment begin transaction and added
        // Note that fragments are not added to manager backstack
        var committedTransactionCount = 0
        val uniquePairKey = manager.toString() to tag
        val isDialogTaggedInstanceInPendingTransaction = instanceSet.contains(uniquePairKey)
        if (isDialogTaggedInstanceInPendingTransaction) return 0
        val fragment = manager.findFragmentByTag(tag)
        val onCommit: () -> Unit = { instanceSet.remove(uniquePairKey) }
        val transaction = manager.beginTransaction().runOnCommit(onCommit)
        try {
            if (fragment == null) {
                when {
                    showNow -> showNow(manager, tag, onCommit)
                    else -> committedTransactionCount = super.show(transaction, tag)
                }
                instanceSet.add(uniquePairKey)
            } else if (fragment is BottomSheetDialogFragment) {

                if (manager.fragments.lastOrNull() == fragment) {
                    instanceSet.remove(uniquePairKey)
                    return 0
                }

                if (this.javaClass == fragment.javaClass) {
                    setInitialSavedState(manager.saveFragmentInstanceState(fragment))
                }

                fragment.dismiss()

                when {
                    showNow -> showNow(manager, tag, onCommit)
                    else -> committedTransactionCount = super.show(transaction, tag)
                }
                instanceSet.add(uniquePairKey)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            when {
                showNow -> super.showNow(manager, tag)
                else -> super.show(transaction, tag)
            }
            instanceSet.add(uniquePairKey)
        }
        return committedTransactionCount
    }

    private fun showNow(manager: FragmentManager, tag: String, onCommit: () -> Unit) {
        try {
            var field = DialogFragment::class.java.getDeclaredField("mDismissed")
            field.isAccessible = true
            field.set(this, false)

            field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            field.isAccessible = true
            field.set(this, true)

            manager.beginTransaction().apply {
                runOnCommit(onCommit)
                add(this@BaseDialogFragment, tag)
                commitNow()
            }
        } catch (e: Exception) {
            //worst scenario. Possible duplicates
            e.printStackTrace()
            showNow(manager, tag)
            instanceSet.remove(manager.toString() to tag)
        }
    }
    //endregion

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is OnDialogFragmentClickListener<*>) {
            "Bottom Sheet Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnDialogFragmentClickListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogClickListener = context as? OnDialogFragmentClickListener<Data>
        check(context is OnDialogFragmentDismissListener<*>) {
            "Bottom Sheet Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnDialogFragmentDismissListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogDismissListener = context as? OnDialogFragmentDismissListener<Data>
    }

    override fun onDetach() {
        super.onDetach()
        onDialogClickListener = null
        onDialogDismissListener = null
    }

    interface OnDialogFragmentDismissListener<Data> {
        fun onBottomSheetDialogFragmentDismiss(
            dialog: DialogFragment,
            data: Data?
        )
    }

    interface OnDialogFragmentClickListener<Data> {
        fun onBottomSheetDialogFragmentClick(
            dialog: DialogFragment,
            data: Data?
        )
    }
    companion object {
        private val instanceSet = mutableSetOf<Pair<String, String>>()
    }
}