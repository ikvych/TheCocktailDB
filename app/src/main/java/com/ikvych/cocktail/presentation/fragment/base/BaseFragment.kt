package com.ikvych.cocktail.presentation.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ikvych.cocktail.presentation.dialog.base.BaseBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.base.BaseDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType
import com.ikvych.cocktail.presentation.extension.baseViewModels
import com.ikvych.cocktail.presentation.extension.observeNotNull
import com.ikvych.cocktail.exception.handler.base.ErrorHandler
import com.ikvych.cocktail.exception.handler.ErrorHandlerProvider
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.lang.Exception
import kotlin.reflect.KClass

abstract class BaseFragment<ViewModel : BaseViewModel, DataBinding: ViewDataBinding> : Fragment(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>{

    protected abstract var contentLayoutResId: Int
    protected val viewModel: ViewModel by baseViewModels()
    protected lateinit var dataBinding: DataBinding
    abstract val viewModelClass: KClass<ViewModel>
    protected lateinit var errorHandler: ErrorHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, contentLayoutResId, container, false)
        dataBinding.lifecycleOwner = this@BaseFragment
        configureDataBinding(dataBinding)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureView(view, savedInstanceState)
        errorHandler =
            ErrorHandlerProvider(
                childFragmentManager
            ) {}
        viewModel.errorLiveData.observeNotNull(requireActivity()) {
            toProcessError(it)
        }
    }

    protected open fun toProcessError(exception: Exception) {
        errorHandler.handleError(exception)
    }

    protected open fun configureDataBinding(binding: DataBinding) {
        //stub
    }

    protected open fun configureView(view: View, savedInstanceState: Bundle?) {
        // stub
    }

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }
}