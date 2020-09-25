package com.ikvych.cocktail.firebase.firebase

/*import android.content.Intent
import androidx.core.app.JobIntentService*/

/*
class NotificationActionService : JobIntentService(){

    private lateinit var cocktailRepository: CocktailRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val cocktailId = intent?.getLongExtra(EXTRA_NOTIFICATION_COCKTAIL_ID, -1L)
        if (cocktailId != null && cocktailId != -1L) {
            cocktailRepository  = Injector.provideRepository(applicationContext, CocktailRepository::class.java)
            GlobalScope.launch {
                val cocktailDb = cocktailRepository.findCocktailById(cocktailId)
                if (cocktailDb == null) {
                    val cocktailNet = cocktailRepository.getCocktailById(cocktailId)
                    if (cocktailNet != null) {
                        cocktailNet.isFavorite = !cocktailNet.isFavorite
                        cocktailRepository.addOrReplaceCocktail(cocktailNet)
                    }
                } else {
                    cocktailDb.isFavorite = !cocktailDb.isFavorite
                    cocktailRepository.addOrReplaceCocktail(cocktailDb)
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onHandleWork(intent: Intent) {}
}*/
