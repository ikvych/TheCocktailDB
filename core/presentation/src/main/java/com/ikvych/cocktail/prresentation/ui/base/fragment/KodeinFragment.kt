package com.ikvych.cocktail.prresentation.ui.base.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.ikvych.cocktail.prresentation.extension.fragmentKodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.subKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

@Suppress("LeakingThis")
open class KodeinFragment : Fragment(),
	KodeinAware{

	override val kodeinContext: KodeinContext<*> = kcontext(this)

	override val kodein: Kodein by subKodein(fragmentKodein()) {
/*		bind<Fragment>() with provider { this@KodeinFragment }*/
		bind<LifecycleOwner>(overrides = true) with provider { this@KodeinFragment }
	}
}