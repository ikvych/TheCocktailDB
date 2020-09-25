package com.ikvych.cocktail.prresentation.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.prresentation.dialog.base.BaseBottomSheetDialogFragment
import com.ikvych.cocktail.prresentation.dialog.base.BaseDialogFragment
import com.ikvych.cocktail.prresentation.dialog.type.DialogButton
import com.ikvych.cocktail.prresentation.dialog.type.DialogType
import com.ikvych.cocktail.prresentation.extension.observeNotNull
import com.ikvych.cocktail.prresentation.exception.handler.ErrorHandlerProvider
import com.ikvych.cocktail.prresentation.exception.handler.base.ErrorHandler
import com.ikvych.cocktail.prresentation.extension.kodeinViewModel
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import java.lang.Exception
import kotlin.reflect.KClass

abstract class BaseFragment<ViewModel : BaseViewModel, DataBinding: ViewDataBinding> : KodeinFragment(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>{

    protected abstract var contentLayoutResId: Int
    protected lateinit var dataBinding: DataBinding
    abstract val viewModelClass: KClass<ViewModel>
    protected val viewModel: ViewModel by kodeinViewModel(viewModelClass)
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