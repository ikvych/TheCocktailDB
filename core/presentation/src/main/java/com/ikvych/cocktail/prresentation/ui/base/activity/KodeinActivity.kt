package com.ikvych.cocktail.prresentation.ui.base.activity

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.android.subKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

@Suppress("LeakingThis")
abstract class KodeinActivity : AppCompatActivity(),
    KodeinAware,
    ActivityStarter{
    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein: Kodein by subKodein(closestKodein(), allowSilentOverride = true) {
        bind<Context>(overrides = true) with provider { this@KodeinActivity }
        bind<Activity>() with provider { this@KodeinActivity }
        bind<LifecycleOwner>() with provider { this@KodeinActivity }
        bind<ActivityStarter>() with provider { this@KodeinActivity }
    }
}