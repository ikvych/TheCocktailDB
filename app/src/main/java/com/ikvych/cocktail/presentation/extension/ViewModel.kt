package com.ikvych.cocktail.presentation.extension

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ikvych.cocktail.di.Injector
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseActivity<ViewModel, DataBinding>.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application,this)
    }

    return ViewModelLazy(ViewModel::class, { viewModelStore }, factoryPromise)
}

@MainThread
fun <ViewModel: BaseViewModel, DataBinding : ViewDataBinding> BaseActivity<ViewModel, DataBinding>.baseViewModels(
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application, this)
    }
    return ViewModelLazy(viewModelClass, { viewModelStore }, factoryPromise)
}

@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseFragment<*, DataBinding>.viewModels(
    owner: SavedStateRegistryOwner = this,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application = requireActivity().application, owner = owner)
    }
    return ViewModelLazy(ViewModel::class, {viewModelStore}, factoryPromise)
}

@MainThread
fun <ViewModel: BaseViewModel, DataBinding : ViewDataBinding> BaseFragment<ViewModel, DataBinding>.baseViewModels(
    owner: SavedStateRegistryOwner = this,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(requireActivity().application, owner)
    }

    return ViewModelLazy(viewModelClass, { viewModelStore }, factoryPromise)
}