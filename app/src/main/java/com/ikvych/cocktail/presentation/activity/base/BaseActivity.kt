package com.ikvych.cocktail.presentation.activity.base

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.ikvych.cocktail.receiver.FlyModeReceiver
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.presentation.dialog.base.BaseBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.base.BaseDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.DialogButton
import com.ikvych.cocktail.presentation.dialog.type.DialogType
import com.ikvych.cocktail.presentation.extension.baseViewModels
import com.ikvych.cocktail.presentation.extension.observeNotNull
import com.ikvych.cocktail.presentation.enumeration.Language
import com.ikvych.cocktail.exception.handler.base.ErrorHandler
import com.ikvych.cocktail.exception.handler.ErrorHandlerProvider
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.lang.Exception
import java.util.*
import kotlin.reflect.KClass


abstract class BaseActivity<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> :
    AppCompatActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    View.OnClickListener, View.OnLongClickListener {

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val flyModeReceiver: FlyModeReceiver = FlyModeReceiver()
    private lateinit var errorHandler: ErrorHandler
    protected abstract var contentLayoutResId: Int
    protected val viewModel: ViewModel by baseViewModels()
    protected lateinit var dataBinding: DataBinding
    abstract val viewModelClass: KClass<ViewModel>

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chooseLanguage = Language.values()[viewModel.selectedLanguageLiveData.value!!]
        val locale = Locale(chooseLanguage.locale)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            with(LocaleList(locale)) {
                LocaleList.setDefault(this)
                configuration.setLocales(this)
            }
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        dataBinding = DataBindingUtil.setContentView(this, contentLayoutResId)
        dataBinding.lifecycleOwner = this@BaseActivity
        configureDataBinding(dataBinding)
        configureView(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    protected open fun configureDataBinding(binding: DataBinding) {
        //stub
    }

    protected open fun configureView(savedInstanceState: Bundle?) {
        //stub
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        errorHandler =
            ErrorHandlerProvider(
                supportFragmentManager
            ) {}
        viewModel.errorLiveData.observeNotNull(this) {
            toProcessErrors(it)
        }
    }

    protected open fun toProcessErrors(exception: Exception) {
        errorHandler.handleError(exception)
    }

    @CallSuper
    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onDialogFragmentDismiss(
            dialog,
            type,
            data
        )
    }

    @CallSuper
    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onDialogFragmentClick(
            dialog,
            buttonType,
            type,
            data
        )
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onBottomSheetDialogFragmentDismiss(
            dialog,
            type,
            data
        )
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)?.onBottomSheetDialogFragmentClick(
            dialog,
            buttonType,
            type,
            data
        )
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(flyModeReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(flyModeReceiver)
    }

    override fun onClick(v: View?) {
        //stub
    }

    override fun onLongClick(v: View?): Boolean {
        //stub
        return false
    }

/*    //region Convenient Observe Methods
    @MainThread
    protected inline fun <reified T> LiveData<T>.observe(noinline observer: (T) -> Unit) {
        this.observe(this@BaseActivity, observer)
    }

    @MainThread
    protected fun <T> LiveData<T?>.observeNotNull(observer: (T) -> Unit) {
        this.observeNotNull(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T?>.observeTillDestroyNotNull(crossinline observer: (T) -> Unit) {
        this.observeTillDestroyNotNull(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T>.observeTillDestroy(crossinline observer: (T) -> Unit) {
        this.observeTillDestroy(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T>.observeNonNullOnce(crossinline observer: (T) -> Unit) {
        this.observeNotNullOnce(this@BaseActivity, observer)
    }
    //endregion*/
}