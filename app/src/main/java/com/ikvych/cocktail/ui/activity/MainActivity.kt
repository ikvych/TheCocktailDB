package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.constant.COCKTAIL_ID
import com.ikvych.cocktail.databinding.ActivityMainBinding
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.ui.dialog.bottom.ResumeAppBottomSheetDialogFragment
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import kotlin.reflect.KClass


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>(){

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModelClass: KClass<MainActivityViewModel>
        get() = MainActivityViewModel::class

    private lateinit var bottomNavigationView: BottomNavigationView
    private var mainFragment: MainFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun onBackPressed() {
        super.onBackPressed()
        // Відстежує активний елементи BottomNavigationView і переключає на відповідний таб

        // Отримує останній номер транзакції в бекстеку
        val lastIndex = supportFragmentManager.backStackEntryCount
        val lastEntry: FragmentManager.BackStackEntry?
        //Якщо lastIndex != 0 отже у стеку ще є елементи
        if (lastIndex != 0) {
            //Отримуємо останню транзакцію у стеку
            lastEntry = supportFragmentManager.getBackStackEntryAt(lastIndex - 1)
            //Взалежності від імені яке ми давали транзакції при переключенні на іншу вкладку
            //визначаємо яка таба повинна стати активною
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
            //Якщо lastIndex == 0 отже у стеку більше немає елементів а отже ми повернулися на той
            // з якого починали, в даному випадку MainFragment
        } else {
            val mainMenu = bottomNavigationView.menu[0]
            mainMenu.isChecked = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyViewModel", "ACTIVITY ONCREATE  ${this}")
    }

    override fun configureView(savedInstanceState: Bundle?) {
        //Відслідковуємо напій дня, якщо він не дорівнює null значить потрібно показати діалог
        viewModel.cocktailOfTheDayLiveData.observe(this, Observer {
            if (it != null) {
                //Якщо діалог фрагмент не дорівнює null значить попердній фрагмент ще не закрили і показувати новий не потрібно
                val dialogFragment =
                    supportFragmentManager.findFragmentByTag(ResumeAppBottomSheetDialogFragment::class.java.simpleName)
                if (dialogFragment !is ResumeAppBottomSheetDialogFragment) {
                    ResumeAppBottomSheetDialogFragment.newInstance(drinkId = it.id) {
                        titleText = getString(R.string.resume_app_dialog_title)
                        descriptionText =
                            getString(R.string.resume_app_dialog_description) + "${it.names.default}"
                        rightButtonText = getString(R.string.resume_app_dialog_right_button)
                        leftButtonText = getString(R.string.resume_app_dialog_left_button)
                    }.show(
                        supportFragmentManager,
                        ResumeAppBottomSheetDialogFragment::class.java.simpleName
                    )
                    viewModel.cocktailOfTheDayLiveData.value = null
                }
            }
        })

        //ховає і показує title у NavigationBottomView
        viewModel.navBarTitleVisibilityLiveData.observe(this,
            Observer<Boolean> { t ->
                if (t!!) {
                    bottomNavigationView.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                } else {
                    bottomNavigationView.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
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
                        ft.hide(profileFragment!!)
                        ft.show(mainFragment!!)
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
                        ft.hide(mainFragment!!)
                        ft.show(profileFragment!!)
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

        profileFragment = supportFragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)
                as? ProfileFragment
        mainFragment = supportFragmentManager.findFragmentByTag(MainFragment::class.java.simpleName)
                as? MainFragment
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance()
            fragmentTransaction.add(
                R.id.fcv_container,
                profileFragment!!,
                ProfileFragment::class.java.simpleName
            )
            fragmentTransaction.hide(profileFragment!!)
        }
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance()
            fragmentTransaction.add(
                R.id.fcv_container,
                mainFragment!!,
                MainFragment::class.java.simpleName
            )
            fragmentTransaction.setPrimaryNavigationFragment(mainFragment!!)
            fragmentTransaction.commit()
        }
    }

    // Залишив реалізацію цього метода, бо поки не знаю як за допомогою viewModel зробити
    // виклик у view onLongClick
    override fun onLongClick(v: View?): Boolean {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    // відкриває деталізацію напою
                    R.id.menu_drink_open -> {
                        val view = v?.findViewById<TextView>(R.id.tv_drink_name)
                        val cocktailName = view?.text ?: ""
                        val cocktailLiveData = viewModel.findCocktailByName(cocktailName.toString())
                        cocktailLiveData.observe(this@MainActivity, Observer { cocktail ->
                            if (cocktail == null) { return@Observer }
                            val intent = Intent(this@MainActivity, DrinkDetailActivity::class.java)
                            intent.putExtra(COCKTAIL_ID, cocktail.id)
                            startActivity(intent)
                        })
                        true
                    }
                    // створює pinned shortcut
                    /*R.id.menu_drink_pin_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val textViewDrinkName =
                                v?.findViewById<TextView>(R.id.tv_drink_name)
                            val drinkName = textViewDrinkName?.text ?: ""
                            val drinkLiveData = viewModel.findCocktailByName(drinkName.toString())
                            drinkLiveData.observe(this@MainActivity, Observer {drink ->
                                if (drink == null) { return@Observer }
                                val shortcutManager: ShortcutManager? =
                                    ContextCompat.getSystemService(
                                        this@MainActivity,
                                        ShortcutManager::class.java
                                    )
                                val drinkImageView =
                                    v!!.findViewById<ImageView>(R.id.iv_drink_image)
                                val drinkBitmapIcon =
                                    drinkImageView.drawToBitmap()
                                val drinkAdaptiveIcon = drinkBitmapIcon.toAdaptiveIcon()
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
                                                    putExtra(COCKTAIL_ID, drink.getIdDrink())
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
                            })
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Your Device don't supports current action",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }*/
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

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
        when (type) {
            ResumeApplicationDialogType -> {
                when (buttonType) {
                    RightDialogButton -> {
                        val intent = Intent(this, DrinkDetailActivity::class.java)
                        intent.putExtra(COCKTAIL_ID, data as Long)
                        startActivity(intent)
                    }
                    LeftDialogButton -> {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

}

