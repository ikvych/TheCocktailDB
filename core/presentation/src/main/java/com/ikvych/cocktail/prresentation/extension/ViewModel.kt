package com.ikvych.cocktail.prresentation.extension

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.ikvych.cocktail.prresentation.ui.base.activity.BaseActivity
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.ui.base.viewmodel.BaseViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import kotlin.reflect.KClass

/**
 * Returns a [Lazy] delegate which provides a [VM] instance retrieved from [ViewModelProvider].
 * Receives [KClass] of [VM] to be able to use in cases where [VM] generic is not provided directly or not reified.
 *
 * Could be used in classes which implements both [ViewModelStoreOwner] and [KodeinAware] interfaces.
 */
inline fun <VM : ViewModel, reified T> T.kodeinViewModel(clazz: KClass<VM>, owner: ViewModelStoreOwner = this)
        : Lazy<VM> where  T : ViewModelStoreOwner, T : KodeinAware {
    return lazy {
        ViewModelProvider(owner, direct.instance()).get(clazz.java)
    }
}

/**
 * Returns a [Lazy] delegate which provides a [VM] instance retrieved from [ViewModelProvider].
 * [VM] generic should be provided directly (exact type of ViewModel) or be reified in called place (from other inline methods).
 *
 * Could be used in classes which implements both [ViewModelStoreOwner] and [KodeinAware] interfaces.
 */
inline fun <reified VM : ViewModel, reified T> T.kodeinViewModel(owner: ViewModelStoreOwner = this)
        : Lazy<VM> where  T : ViewModelStoreOwner, T : KodeinAware = kodeinViewModel(VM::class, owner)


@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseFragment<*, DataBinding>.viewModels(
    noinline owner: () -> ViewModelStoreOwner =  { this },
    saveStateOwner: SavedStateRegistryOwner = this,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
/*    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(
            application = requireActivity().application,
            owner = saveStateOwner
        )
    }*/
    return ViewModelLazy(ViewModel::class, { owner().viewModelStore }, /*factoryPromise*/factoryProducer!!)
}

@MainThread
fun <ViewModel : BaseViewModel, DataBinding : ViewDataBinding> BaseFragment<ViewModel, DataBinding>.baseViewModels(
    owner: ViewModelStoreOwner = this,
    saveStateOwner: SavedStateRegistryOwner = this,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
/*    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(requireActivity().application, saveStateOwner)
    }*/

    return ViewModelLazy(viewModelClass, { owner.viewModelStore }, /*factoryPromise*/factoryProducer!!)
}


@MainThread
inline fun <reified ViewModel : BaseViewModel, reified DataBinding : ViewDataBinding> BaseActivity<*, DataBinding>.viewModels(
    owner: ViewModelStoreOwner = this,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
/*    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application, this)
    }*/

    return ViewModelLazy(ViewModel::class, { owner.viewModelStore }, direct.instance())
}

@MainThread
fun <ViewModel : BaseViewModel, DataBinding : ViewDataBinding> BaseActivity<ViewModel, DataBinding>.baseViewModels(
    owner: ViewModelStoreOwner = this,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<ViewModel> {
/*    val factoryPromise = factoryProducer ?: {
        Injector.ViewModelFactory(application, this)
    }*/

    return ViewModelLazy(viewModelClass, { owner.viewModelStore }, direct.instance())
}