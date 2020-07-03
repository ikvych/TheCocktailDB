package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivityViewModel(
    application: Application
) : BaseViewModel(application) {

    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun findDrinkByName(drinkName: String): Drink? {
        return drinkRepository.findDrinkByName(drinkName)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrinkIntoDb(drink)
    }

    fun getDrinkOfTheDay(): Drink? {
        //Оскільки ми викликаємо цей метод з MainActivity, яка по своїй природні працює тільки з базою даних, то
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
                return null
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
        return drinkOfTheDay
    }
}