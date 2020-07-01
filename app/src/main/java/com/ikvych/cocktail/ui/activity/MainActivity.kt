package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.receiver.TimerReceiver
import com.ikvych.cocktail.service.TimerService
import com.ikvych.cocktail.ui.base.*
import com.ikvych.cocktail.ui.dialog.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel


class MainActivity : BaseActivity<MainActivityViewModel>(), TimerReceiver.OnTimerReceiverListener,
    ApplicationLifeCycleObserver.OnLifecycleObserverListener {

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModels()

    private lateinit var timerReceiver: TimerReceiver

    private lateinit var serviceTimerIntent: Intent
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mainFragment: MainFragment
    private lateinit var profileFragment: ProfileFragment
    private var drinkOfTheDay: Drink? = null

    override fun onBackPressed() {
        super.onBackPressed()
        // Відстежує активний елементи BottomNavigationView і переключає на відповідний таб
        val lastIndex = supportFragmentManager.backStackEntryCount
        val lastEntry: FragmentManager.BackStackEntry?
        if (lastIndex != 0) {
            lastEntry = supportFragmentManager.getBackStackEntryAt(lastIndex - 1)
            when (lastEntry.name) {
                MainFragment::class.java.simpleName -> {
                    val mainMenu = bottomNavigationView.menu[0]
                    mainMenu.isChecked = true
                }
                ProfileFragment::class.java.simpleName -> {
                    val profileMenu = bottomNavigationView.menu[1]
                    profileMenu.isChecked = true
                }
            }
        } else {
            val mainMenu = bottomNavigationView.menu[0]
            mainMenu.isChecked = true
        }
    }

    override fun configureView(savedInstanceState: Bundle?) {
        timerReceiver = TimerReceiver(this)

        val lifecycleObserver = ApplicationLifeCycleObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
        serviceTimerIntent = Intent(this, TimerService::class.java)

        viewModel.navBarTitleVisibilityLiveData.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t!!) {
                    bottomNavigationView.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                } else {
                    bottomNavigationView.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
                }
            }
        })

        bottomNavigationView = findViewById(R.id.bnv_main)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // знаходить останню транзацію і порівнює з поточною, щоб не зберігалися до BackStack
            // виклики однієї і тієї самої таби підряд
            val lastIndex = supportFragmentManager.backStackEntryCount
            var lastEntry: FragmentManager.BackStackEntry? = null
            if (lastIndex != 0) {
                lastEntry = supportFragmentManager.getBackStackEntryAt(lastIndex - 1)
            }
            when (item.itemId) {
                R.id.menu_main_fragment -> {
                    if (lastEntry != null && lastEntry.name != MainFragment::class.java.simpleName) {
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.hide(profileFragment)
                        ft.show(mainFragment)
                        ft.setPrimaryNavigationFragment(mainFragment)
                        ft.addToBackStack(MainFragment::class.java.simpleName)
                        ft.commit()
                        true
                    } else {
                        false
                    }
                }

                R.id.menu_profile_fragment -> {
                    if (lastEntry == null || lastEntry.name != ProfileFragment::class.java.simpleName) {
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.hide(mainFragment)
                        ft.show(profileFragment)
                        ft.setPrimaryNavigationFragment(profileFragment)
                        ft.addToBackStack(ProfileFragment::class.java.simpleName)
                        ft.commit()
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }

        profileFragment = ProfileFragment.newInstance()
        mainFragment = MainFragment.newInstance()
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fcv_container,
            profileFragment,
            ProfileFragment::class.java.simpleName
        )
        fragmentTransaction.hide(profileFragment)
        fragmentTransaction.add(R.id.fcv_container, mainFragment, MainFragment::class.java.simpleName)
        fragmentTransaction.setPrimaryNavigationFragment(mainFragment)
        fragmentTransaction.commit()
    }

    override fun actionOnStart() {
/*        val timerReceiverFilter = IntentFilter().apply {
            addAction(START_BACKGROUND_TIMER)
        }
        registerReceiver(timerReceiver, timerReceiverFilter)*/

    }

    override fun actionOnStop() {
/*        startService(Intent(this, TimerService::class.java))
        unregisterReceiver(timerReceiver)*/
    }

    override fun onClick(v: View?) {
        // відкриває деталізацію коктейлю
        if (v is CardView) {
            val view = v.findViewById<TextView>(R.id.tv_drink_name)
            val drinkName = view?.text
            if (drinkName != null) {
                val drink = viewModel.findDrinkByName(drinkName.toString())
                val intent = Intent(this, DrinkDetailActivity::class.java)
                intent.putExtra(DRINK, drink)
                startActivity(intent)
            }
        }
        // додає і видаляє з улюблених
        if (v is CheckBox) {
            val drinkName = v.tag as String
            val drink = viewModel.findDrinkByName(drinkName)
            if (v.isChecked) {
                drink.setIsFavorite(true)
                viewModel.saveDrink(drink)
            } else {
                drink.setIsFavorite(false)
                viewModel.saveDrink(drink)
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    // відкриває деталізацію напою
                    R.id.menu_drink_open -> {
                        val view = v?.findViewById<TextView>(R.id.tv_drink_name)
                        val drinkName = view?.text ?: ""
                        val drink = viewModel.findDrinkByName(drinkName.toString())

                        val intent = Intent(this@MainActivity, DrinkDetailActivity::class.java)
                        intent.putExtra(DRINK, drink)
                        startActivity(intent)
                        true
                    }
                    // створює pinned shortcut
                    R.id.menu_drink_pin_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val textViewDrinkName = v?.findViewById<TextView>(R.id.tv_drink_name)
                            val drinkName = textViewDrinkName?.text ?: ""
                            val drink: Drink = viewModel.findDrinkByName(drinkName.toString())
                            val shortcutManager: ShortcutManager? =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                )
                            val drinkImageView = v!!.findViewById<ImageView>(R.id.iv_drink_image)
                            val drinkAdaptiveIcon = drinkImageView.drawToBitmap().toAdaptiveIcon()
                            val shortcut =
                                ShortcutInfo.Builder(this@MainActivity, drink.getStrDrink())
                                    .setShortLabel(drink.getStrDrink()!!)
                                    .setLongLabel("Launch ${drink.getStrDrink()}")
                                    .setIcon(drinkAdaptiveIcon)
                                    .setIntents(
                                        arrayOf(
                                            Intent(
                                                this@MainActivity,
                                                MainActivity::class.java
                                            ).apply {
                                                action = Intent.ACTION_CREATE_SHORTCUT
                                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            },
                                            Intent(
                                                this@MainActivity,
                                                DrinkDetailActivity::class.java
                                            ).apply {
                                                putExtra(DRINK_ID, drink.getIdDrink())
                                                action = Intent.ACTION_CREATE_SHORTCUT
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                    )
                                    .build()

                            shortcutManager!!.dynamicShortcuts = listOf(shortcut)

                            if (alreadyExist(shortcut)) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Shortcut already exist",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                shortcutManager.requestPinShortcut(shortcut, null)
                            }

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Your Device don't supports current action",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_drink_item_shortcut)
            show()
        }
        return true
    }

    private fun alreadyExist(shortCut: ShortcutInfo): Boolean {
        var isExist = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager: ShortcutManager? =
                ContextCompat.getSystemService(this, ShortcutManager::class.java)

            shortcutManager!!.pinnedShortcuts.forEach { shortcutInfo: ShortcutInfo? ->
                if (shortCut.id == shortcutInfo!!.id) {
                    isExist = true
                    return isExist
                }
            }
        }
        return isExist
    }

    override fun onReceive() {
        stopService(Intent(this, TimerService::class.java))
        try {
            drinkOfTheDay = viewModel.getDrinkOfTheDay()
        } catch (ex: NoSuchElementException) {

        }
        val fragment = supportFragmentManager.findFragmentByTag(RegularBottomSheetDialogFragment::class.java.simpleName)
        if (fragment !is RegularBottomSheetDialogFragment ) {
            RegularBottomSheetDialogFragment.newInstance {
                if (drinkOfTheDay == null) {
                    titleText = "З поверненням"
                } else {
                    titleText = "З поверненням"
                    descriptionText = "Пропонуємо переглянути напій дня: ${drinkOfTheDay!!.getStrDrink()}"
                    rightButtonText = "Переглянути"
                    leftButtonText = "Ні, дякую"
                }
            }.show(
                supportFragmentManager,
                RegularBottomSheetDialogFragment::class.java.simpleName
            )
        }

    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
/*        when (type) {
            RegularDialogType -> {
                when (buttonType) {
                    RightDialogButton -> {
                        val intent = Intent(this, DrinkDetailActivity::class.java)
                        intent.putExtra(DRINK, drinkOfTheDay)
                        startActivity(intent)
                    }
                    LeftDialogButton -> {
                        dialog.dismiss()
                    }
                }
            }
        }*/
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onBottomSheetDialogFragmentDismiss(dialog, type, data)
        when (type) {
            RegularDialogType -> {
/*                unregisterReceiver(timerReceiver)*/
            }
        }
    }
}

