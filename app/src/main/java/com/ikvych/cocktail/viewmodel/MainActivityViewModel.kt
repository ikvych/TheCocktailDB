package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

const val MAIN_ACTIVITY_SHARED_PREFERENCE = "MAIN_ACTIVITY_SHARED_PREFERENCE"

class MainActivityViewModel(
    application: Application
) : BaseViewModel(application), ApplicationLifeCycleObserver.OnLifecycleObserverListener {

    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData(true)
    val drinkOfTheDayLiveData: MutableLiveData<Drink?> = MutableLiveData()
    private var lifecycleObserver: ApplicationLifeCycleObserver
    private var sharedPreferences: SharedPreferences = application.getSharedPreferences(
        MAIN_ACTIVITY_SHARED_PREFERENCE,
        Context.MODE_PRIVATE
    )

    init {
        lifecycleObserver = ApplicationLifeCycleObserver(this, sharedPreferences)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    override fun onCleared() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleObserver)
        super.onCleared()
    }

    override fun shouldShowDrinkOfTheDay() {
        setDrinkOfTheDay()
    }

    fun findDrinkByName(drinkName: String): Drink? {
        return drinkRepository.findDrinkByName(drinkName)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrinkIntoDb(drink)
    }

    fun removeDrink(drink: Drink) {
        drinkRepository.removeDrink(drink)
    }


    private fun setDrinkOfTheDay() {
        //Оскільки lifecycle, до якого привязана робота цього метода, належить MainActivity, яка по своїй природні працює тільки з базою даних, то
        //даний метод не працює при повному перевстановленні додатку, оскільки разом з додатком видаляється сама база даних
        //в якій ми шукаємо напій дня

        //отримаю і форматую сьогоднішню дату, яка являється унікальним ключем для напою дня
        val currentDate = Date()
        val pattern = "MM-dd-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val stringDate: String = simpleDateFormat.format(currentDate)

        //шукаю в базі даних напій який має сьогоднішню дату в якості ключа
        var drinkOfTheDay = drinkRepository.findDrinkOfTheDay(stringDate)
        //якщо напій не знайдено значить сьогодні напій дня ще не визначався і потрібно його обрати
        if (drinkOfTheDay == null) {
            //витягаю з БД усі напої, якщо їх немає, або список порожній, значить
            // напій немає з чого вибирати і тому виходимо з методу getDrinkOfTheDay()
            val allDrinks = drinkRepository.getAllDrinksFromDb()
            if (allDrinks.isNullOrEmpty()) {
                return
            } else {
                //якщо список не порожній, дістаємо з нього випадковий напій і присвоюємо йому сьогоднішню дату
                // в якості унікального ключа, і зберігаємо в базу даних
                val newDrinkOfTheDay: Drink = allDrinks.random()
                newDrinkOfTheDay.setDrinkOfDay(stringDate)
                drinkRepository.saveDrinkIntoDb(newDrinkOfTheDay)
            }
            //знову шукаємо напі по даній даті(очікується той що тільки що зберегли)
            drinkOfTheDay = drinkRepository.findDrinkOfTheDay(stringDate)
        }
        //передаємо напій дня в liveData яку відслідковує activity
        drinkOfTheDayLiveData.value = drinkOfTheDay
    }
}