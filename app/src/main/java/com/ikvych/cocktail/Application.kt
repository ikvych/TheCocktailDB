package com.ikvych.cocktail

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.facebook.stetho.Stetho
import com.ikvych.cocktail.auth.di.authStarterModule
import com.ikvych.cocktail.detail.di.detailStarterModule
import com.ikvych.cocktail.impl.di.dbSourceModule
import com.ikvych.cocktail.preference.di.localSourceModule
import com.ikvych.cocktail.impl.di.netSourceModule
import com.ikvych.cocktail.main.di.mainStarterModule
import com.ikvych.cocktail.profile.di.profileStarterModule
import com.ikvych.cocktail.prresentation.di.firebaseModule
import com.ikvych.cocktail.prresentation.di.mapperModule
import com.ikvych.cocktail.repository.impl.di.repositoryModule
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreContextTranslators
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class Application : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
/*        Injector.init(this)*/
        //бібліотека яка дозволяє відслідковувати базу даних через веб браузер у реальному часі
        Stetho.initializeWithDefaults(this)
        registerNotificationChanel()
    }

    override val kodein = Kodein.lazy {
        import(androidCoreContextTranslators)
        import(appModule, true)

/*        import(navigationModule)*/

        import(repositoryModule)

        import(localSourceModule)
        import(dbSourceModule)
        import(netSourceModule)


        import(mapperModule)
        import(firebaseModule)

        import(mainStarterModule)
        import(authStarterModule)
        import(detailStarterModule)
        import(profileStarterModule)

/*        import(interactorModule)

        import(fcmSourceModule)*/
    }

    private val appModule = Kodein.Module("appModule") {
        // because {BuildConfig.APPLICATION_ID} in Library modules are not the same as in Application
/*        constant(APPLICATION_ID) with BuildConfig.APPLICATION_ID
        constant(BASE_URL) with baseUrl*/

        bind<Application>() with singleton { this@Application }
        bind<Context>() with singleton { this@Application }
        bind<DKodein>() with provider { this.dkodein }
    }

    private fun registerNotificationChanel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.push_notification_chanel)
            val descriptionText = getString(R.string.push_notification_chanel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    }

}