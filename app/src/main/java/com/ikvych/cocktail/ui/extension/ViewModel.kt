package com.ikvych.cocktail.ui.extension

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.ikvych.cocktail.di.Injector
import com.ikvych.cocktail.ui.activity.base.BaseActivity
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