package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK_ID
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.ResumeAppBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.*
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.util.ShortcutType
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainActivityViewModel>() {

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModels()

    private var mainFragment: MainFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun configureView(savedInstanceState: Bundle?) {
        //Відслідковуємо liveData з напоєм дня, якщо він не дорівнює null значить потрібно показати діалог
        viewModel.drinkOfTheDayLiveData.observe(this, Observer {
            //якщо drink != null значить потрібно показати діалог з напоєм дня
            if (it != null) {
                //Якщо діалог фрагмент не дорівнює null значить попердній фрагмент ще не закрили і показувати новий не потрібно
                val dialogFragment =
                    supportFragmentManager.findFragmentByTag(ResumeAppBottomSheetDialogFragment::class.java.simpleName)
                if (dialogFragment !is ResumeAppBottomSheetDialogFragment) {
                    ResumeAppBottomSheetDialogFragment.newInstance(it.getIdDrink()!!) {
                        titleText = getString(R.string.resume_app_dialog_title)
                        descriptionText =
                            getString(R.string.resume_app_dialog_description) + "${it.getStrDrink()}"
                        rightButtonText = getString(R.string.resume_app_dialog_right_button)
                        leftButtonText = getString(R.string.resume_app_dialog_left_button)
                    }.show(
                        supportFragmentManager,
                        ResumeAppBottomSheetDialogFragment::class.java.simpleName
                    )
                    //обнуляю value у liveData. Це означатиме, що діалог уже був показаний і до наступного виходу з додатку
                    //його поки не потрбіно показувати
                    viewModel.drinkOfTheDayLiveData.value = null
                }
            }
        })

        //взалежності від значення value показується або приховується титульний напис у BottomNavigationBar
        viewModel.navBarTitleVisibilityLiveData.observe(this,
            Observer<Boolean> { t ->
                if (t!!) {
                    bnv_main.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                } else {
                    bnv_main.labelVisibilityMode =
                        LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
                }
            })

        profileFragment =
            supportFragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)
                    as? ProfileFragment
        mainFragment = supportFragmentManager.findFragmentByTag(MainFragment::class.java.simpleName)
                as? MainFragment

        if (profileFragment == null && mainFragment == null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            profileFragment = ProfileFragment.newInstance()
            fragmentTransaction.add(
                R.id.fcv_container,
                profileFragment!!,
                ProfileFragment::class.java.simpleName
            )
            fragmentTransaction.hide(profileFragment!!)
            mainFragment = MainFragment.newInstance()
            fragmentTransaction.add(
                R.id.fcv_container,
                mainFragment!!,
                MainFragment::class.java.simpleName
            )
            fragmentTransaction.setPrimaryNavigationFragment(mainFragment!!)
            fragmentTransaction.commit()
        }

        bnv_main.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main_fragment -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.hide(profileFragment!!)
                    ft.show(mainFragment!!)
                    ft.setPrimaryNavigationFragment(mainFragment)
                    ft.commit()
                    true
                }
                R.id.menu_profile_fragment -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.hide(mainFragment!!)
                    ft.show(profileFragment!!)
                    ft.setPrimaryNavigationFragment(profileFragment)
                    ft.commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        } else {
            when (v.id) {
                // відкриває деталізацію коктейлю
                R.id.cv_item_drink -> {
                    val drinkId = v.tag as Long
                    val intent = Intent(this, DrinkDetailActivity::class.java)
                    intent.putExtra(DRINK_ID, drinkId)
                    startActivity(intent)
                }
                // додає і видаляє з улюблених
                R.id.cb_is_favorite -> {
                    val drinkId = v.tag as Long
                    val drink = viewModel.findDrinkById(drinkId) ?: return
                    if ((v as CheckBox).isChecked) {
                        drink.setIsFavorite(true)
                        viewModel.saveDrinkIntoDb(drink)
                    } else {
                        drink.setIsFavorite(false)
                        viewModel.saveDrinkIntoDb(drink)
                    }
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v == null) return false
        //v is CardView якій я попередньо прописав tag як id напою
        val drinkId = v.tag as Long
        val drink = viewModel.findDrinkById(drinkId) ?: return false

        PopupMenu(this, v).apply {

            menu.add(0, R.id.menu_drink_open, 0, getString(R.string.open))
            menu.add(0, R.id.menu_drink_quick_preview, 0, getString(R.string.quick_preview))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (shortcutAlreadyExist(shortLabel = drink.getStrDrink(), shortcutType = ShortcutType.PINNED_SHORTCUT)) {
                    menu.removeItem(R.id.menu_drink_pin_shortcut)
                    menu.add(0, R.id.menu_drink_remove_shortcut,0, getString(R.string.remove_shortcut))
                } else {
                    menu.removeItem(R.id.menu_drink_remove_shortcut)
                    menu.add(0, R.id.menu_drink_pin_shortcut, 0, getString(R.string.pin_shortcut))
                }
                if (shortcutAlreadyExist(shortLabel = drink.getStrDrink(), shortcutType = ShortcutType.DYNAMIC_SHORTCUT)) {
                    menu.removeItem(R.id.menu_drink_add_dynamic_shortcut)
                    menu.add(0, R.id.menu_drink_remove_dynamic_shortcut,0, getString(R.string.remove_dynamic_shortcut))
                } else {
                    menu.removeItem(R.id.menu_drink_remove_dynamic_shortcut)
                    menu.add(0, R.id.menu_drink_add_dynamic_shortcut, 0, getString(R.string.add_dynamic_shortcut))
                }
            }
            if (drink.isFavorite()) {
                menu.removeItem(R.id.menu_drink_add_favorite)
                menu.add(0, R.id.menu_drink_remove_favorite, 0, getString(R.string.remove_favorite))
            } else {
                menu.removeItem(R.id.menu_drink_remove_favorite)
                menu.add(0, R.id.menu_drink_add_favorite, 0, getString(R.string.add_favorite))
            }
            menu.add(0, R.id.menu_drink_remove, 0, getString(R.string.remove))

            setOnMenuItemClickListener {
                when (it.itemId) {
                    // відкриває деталізацію напою
                    R.id.menu_drink_open -> {
                        val intent = Intent(this@MainActivity, DrinkDetailActivity::class.java)
                        intent.putExtra(DRINK_ID, drink.getIdDrink())
                        startActivity(intent)
                        true
                    }
                    // додає напій до улюблених
                    R.id.menu_drink_add_favorite -> {
                        drink.setIsFavorite(true)
                        viewModel.saveDrinkIntoDb(drink)
                        Toast.makeText(
                            this@MainActivity,
                            "${drink.getStrDrink()} added to favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    // видаляє напій з улюблених
                    R.id.menu_drink_remove_favorite -> {
                        drink.setIsFavorite(false)
                        viewModel.saveDrinkIntoDb(drink)
                        Toast.makeText(
                            this@MainActivity,
                            "${drink.getStrDrink()} removed from favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.menu_drink_remove -> {
                        viewModel.removeDrink(drink)
                        Toast.makeText(
                            this@MainActivity,
                            "${drink.getStrDrink()} removed from history",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        true
                    }
                    // створює pinned shortcut
                    R.id.menu_drink_pin_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val shortcutManager =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                ) ?: return@setOnMenuItemClickListener false

                            val shortcut = createDrinkShortcut(v, drink, ShortcutType.PINNED_SHORTCUT)

                            shortcutManager.requestPinShortcut(shortcut, null)
                            return@setOnMenuItemClickListener true
                        }
                        false
                    }
                    R.id.menu_drink_remove_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val shortcutManager: ShortcutManager? =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                )

                            shortcutManager!!.pinnedShortcuts.forEach { shortcutInfo: ShortcutInfo ->
                                if (shortcutInfo.shortLabel == drink.getStrDrink()) {
                                    shortcutManager.disableShortcuts(arrayListOf(shortcutInfo.id))
                                }
                            }
                            return@setOnMenuItemClickListener true
                        }
                        false
                    }
                    R.id.menu_drink_add_dynamic_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val shortcutManager =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                ) ?: return@setOnMenuItemClickListener false

                            val shortcut = createDrinkShortcut(v, drink, ShortcutType.DYNAMIC_SHORTCUT)

                            val shorctcuts = shortcutManager.dynamicShortcuts
                            shorctcuts.add(shortcut)
                            shortcutManager.dynamicShortcuts = shorctcuts
                        }
                        true
                    }
                    R.id.menu_drink_remove_dynamic_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val shortcutManager =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                ) ?: return@setOnMenuItemClickListener false

                            shortcutManager.dynamicShortcuts.forEach { shortcutInfo: ShortcutInfo ->
                                if (shortcutInfo.shortLabel == drink.getStrDrink()) {
                                    shortcutManager.removeDynamicShortcuts(arrayListOf(shortcutInfo.id))
                                }
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
            show()
        }
        return true
    }

    private fun shortcutAlreadyExist(shortCut: ShortcutInfo? = null, shortLabel: String? = null, shortcutType: ShortcutType): Boolean {
        var isExist = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager: ShortcutManager? =
                ContextCompat.getSystemService(this, ShortcutManager::class.java)

            var shortcuts: List<ShortcutInfo>
            when (shortcutType) {
                ShortcutType.PINNED_SHORTCUT -> shortcuts = shortcutManager!!.pinnedShortcuts
                ShortcutType.DYNAMIC_SHORTCUT -> shortcuts = shortcutManager!!.dynamicShortcuts
            }
            shortcuts.forEach { shortcutInfo: ShortcutInfo? ->
                val name = shortcutInfo!!.shortLabel
                val ordi = shortcutInfo.extras?.getInt(shortcutType.name, -1)
                if (
                    shortCut?.shortLabel ?: shortLabel == shortcutInfo!!.shortLabel &&
                    shortcutType.ordinal == shortcutInfo.extras?.getInt(shortcutType.name)
                ) {
                    isExist = true
                    return isExist
                }
            }
        }
        return isExist
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDrinkShortcut(view: View, drink: Drink, type: ShortcutType): ShortcutInfo {
        val drinkImageView = view.findViewById<ImageView>(R.id.iv_drink_image)
        val returnedBitmap = Bitmap.createBitmap(
            drinkImageView.getWidth(),
            drinkImageView.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas =
            Canvas(returnedBitmap)
        val bgDrawable = drinkImageView.getBackground()
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(
                Color.WHITE
            )
        }
        drinkImageView.draw(canvas)
        val drinkAdaptiveIcon = returnedBitmap.toAdaptiveIcon()
        val shortcut =
            ShortcutInfo.Builder(this@MainActivity, drink.getStrDrink())
                .setExtras(PersistableBundle().apply {
                    putInt(type.name, type.ordinal)
                })
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
        return shortcut
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
                        intent.putExtra(DRINK_ID, data as Long)
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

