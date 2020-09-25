package com.ikvych.cocktail.prresentation.ui.base.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.prresentation.di.viewModelFactoryModule
import com.ikvych.cocktail.prresentation.dialog.base.BaseBottomSheetDialogFragment
import com.ikvych.cocktail.prresentation.dialog.base.BaseDialogFragment
import com.ikvych.cocktail.prresentation.dialog.type.DialogButton
import com.ikvych.cocktail.prresentation.dialog.type.DialogType
import com.ikvych.cocktail.prresentation.exception.handler.ErrorHandlerProvider
import com.ikvych.cocktail.prresentation.exception.handler.base.ErrorHandler
import com.ikvych.cocktail.prresentation.extension.kodeinViewModel
import com.ikvych.cocktail.prresentation.extension.observeNotNull
import com.ikvych.cocktail.prresentation.receiver.FlyModeReceiver
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import org.kodein.di.Kodein
import org.kodein.di.android.subKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.subKodein
import kotlin.reflect.KClass

abstract class BaseActivity<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> : KodeinActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    View.OnClickListener, View.OnLongClickListener  {

    override val kodein: Kodein by subKodein({ super.kodein }, true) {
        import(viewModelFactoryModule)
        bind<SavedStateHandle>() with provider { SavedStateHandle() }
/*        bind<ViewModelProvider.Factory>() with provider { ViewModelSaveStateFactory(
            this.dkodein,
            this@BaseActivity
        ) }*/
    }

    private val flyModeReceiver: FlyModeReceiver = FlyModeReceiver()
    private lateinit var errorHandler: ErrorHandler
    protected abstract var contentLayoutResId: Int
    protected lateinit var dataBinding: DataBinding
    abstract val viewModelClass: KClass<ViewModel>
    protected val viewModel: ViewModel by kodeinViewModel(viewModelClass)


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        val chooseLanguage = Language.values()[viewModel.selectedLanguageLiveData.value!!]
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
        resources.updateConfiguration(configuration, resources.displayMetrics)*/
        dataBinding = DataBindingUtil.setContentView(this, contentLayoutResId)
        dataBinding.lifecycleOwner = this@BaseActivity
        configureDataBinding(dataBinding)
        configureView(savedInstanceState)
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