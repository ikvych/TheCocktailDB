package com.ikvych.cocktail.presentation.extension

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.ikvych.cocktail.di.Injector
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseFragment<*, DataBinding>.viewModels(
    owner: ViewModelStoreOwner = this,
    saveStateOwner: SavedStateRegistryOwner = this,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(
            application = requireActivity().application,
            owner = saveStateOwner
        )
    }
    return ViewModelLazy(ViewModel::class, { owner.viewModelStore }, factoryPromise)
}

@MainThread
fun <ViewModel : BaseViewModel, DataBinding : ViewDataBinding> BaseFragment<ViewModel, DataBinding>.baseViewModels(
    owner: ViewModelStoreOwner = this,
    saveStateOwner: SavedStateRegistryOwner = this,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(requireActivity().application, saveStateOwner)
    }

    return ViewModelLazy(viewModelClass, { owner.viewModelStore }, factoryPromise)
}


@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseActivity<*, DataBinding>.viewModels(
    owner: ViewModelStoreOwner = this,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application, this)
    }

    return ViewModelLazy(ViewModel::class, { owner.viewModelStore }, factoryPromise)
}

@MainThread
fun <ViewModel : BaseViewModel, DataBinding : ViewDataBinding> BaseActivity<ViewModel, DataBinding>.baseViewModels(
    owner: ViewModelStoreOwner = this,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application, this)
    }
    return ViewModelLazy(viewModelClass, { owner.viewModelStore }, factoryPromise)
}