package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK_ID
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ActivityMainBinding
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.ResumeAppBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.type.*
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.util.ShortcutType
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModels()

    private var mainFragment: MainFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun configureView(savedInstanceState: Bundle?) {
        //Відслідковуємо напій дня, якщо він не дорівнює null значить потрібно показати діалог
        viewModel.drinkOfTheDayLiveData.observe(this, Observer {
            if (it != null) {
                //Якщо діалог фрагмент являється ResumeAppBottomSheetDialogFragment значить попердній фрагмент ще не закрили і показувати новий не потрібно
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

    override fun configureDataBinding(binding: ActivityMainBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        } else {
            when (v.id) {
                // відкриває деталізацію коктейлю
                //v is CardView якій я попередньо прописав tag як id напою
                R.id.cv_item_drink -> {
                    val drinkId = v.tag as Long
                    val intent = Intent(this, DrinkDetailActivity::class.java)
                    intent.putExtra(DRINK_ID, drinkId)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v == null) return false
        //v is CardView якій я попередньо прописав tag як id напою
        return when (v.id) {
            R.id.cv_item_drink -> {
                val drinkId = v.tag as Long
                val drink = viewModel.findDrinkById(drinkId) ?: return false

                PopupMenu(this, v).apply {
                    configurePopupMenu(this, drink)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            // відкриває деталізацію напою
                            R.id.menu_drink_open -> {
                                val intent =
                                    Intent(this@MainActivity, DrinkDetailActivity::class.java)
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
                                    "${drink.getStrDrink()} ${getString(R.string.popup_menu_add_to_favorite)}",
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
                                    "${drink.getStrDrink()} ${getString(R.string.popup_menu_remove_from_favorite)}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            }
                            R.id.menu_drink_remove -> {
                                viewModel.removeDrink(drink)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val shortcutManager =
                                        ContextCompat.getSystemService(
                                            this@MainActivity,
                                            ShortcutManager::class.java
                                        )
                                    shortcutManager?.removeDynamicShortcuts(arrayListOf("${drinkId}${ShortcutType.DYNAMIC_SHORTCUT}"))
                                    shortcutManager?.disableShortcuts(arrayListOf("${drinkId}${ShortcutType.PINNED_SHORTCUT}"))
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    "${drink.getStrDrink()} ${getString(R.string.popup_menu_remove_from_history)}",
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

                                    val shortcut =
                                        createDrinkShortcut(v, drink, ShortcutType.PINNED_SHORTCUT)
                                    shortcutManager.requestPinShortcut(shortcut, null)
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

                                    val shortcut =
                                        createDrinkShortcut(v, drink, ShortcutType.DYNAMIC_SHORTCUT)
                                    shortcutManager.addDynamicShortcuts(mutableListOf(shortcut))
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.popup_menu_add_dynamic_shortcut),
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                            shortcutManager.removeDynamicShortcuts(
                                                arrayListOf(
                                                    shortcutInfo.id
                                                )
                                            )
                                            Toast.makeText(
                                                this@MainActivity,
                                                getString(R.string.popup_menu_remove_dynamic_shortcut),
                                                Toast.LENGTH_SHORT
                                            ).show()
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
            else -> false
        }
    }

    private fun configurePopupMenu(menu: PopupMenu, drink: Drink) {
        menu.menu.add(0, R.id.menu_drink_open, 0, getString(R.string.open))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutAlreadyExist(drink.getIdDrink()!!, ShortcutType.PINNED_SHORTCUT)) {
                menu.menu.removeItem(R.id.menu_drink_pin_shortcut)
            } else {
                menu.menu.add(0, R.id.menu_drink_pin_shortcut, 0, getString(R.string.pin_shortcut))
            }
            if (shortcutAlreadyExist(drink.getIdDrink()!!, ShortcutType.DYNAMIC_SHORTCUT)) {
                menu.menu.removeItem(R.id.menu_drink_add_dynamic_shortcut)
                menu.menu.add(
                    0,
                    R.id.menu_drink_remove_dynamic_shortcut,
                    0,
                    getString(R.string.remove_dynamic_shortcut)
                )
            } else {
                menu.menu.removeItem(R.id.menu_drink_remove_dynamic_shortcut)
                menu.menu.add(
                    0,
                    R.id.menu_drink_add_dynamic_shortcut,
                    0,
                    getString(R.string.add_dynamic_shortcut)
                )
            }
        }
        if (drink.isFavorite()) {
            menu.menu.removeItem(R.id.menu_drink_add_favorite)
            menu.menu.add(
                0,
                R.id.menu_drink_remove_favorite,
                0,
                getString(R.string.remove_favorite)
            )
        } else {
            menu.menu.removeItem(R.id.menu_drink_remove_favorite)
            menu.menu.add(0, R.id.menu_drink_add_favorite, 0, getString(R.string.add_favorite))
        }
        menu.menu.add(0, R.id.menu_drink_remove, 0, getString(R.string.remove))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDrinkShortcut(view: View, drink: Drink, type: ShortcutType): ShortcutInfo {
        val drinkImageView = view.findViewById(R.id.iv_drink_image) as ImageView
        val drinkImageBitmap: Bitmap = Bitmap.createBitmap(
            drinkImageView.width,
            drinkImageView.height,
            Bitmap.Config.ARGB_8888
        )
        val drinkImageCanvas =
            Canvas(drinkImageBitmap)
        val bgDrawable = drinkImageView.background
        if (bgDrawable != null) {
            bgDrawable.draw(drinkImageCanvas)
        } else {
            drinkImageCanvas.drawColor(
                Color.WHITE
            )
        }
        drinkImageView.draw(drinkImageCanvas)
        val drinkAdaptiveIcon = drinkImageBitmap.toAdaptiveIcon()
        //id для shortcut виставлено як id самого напою + ShortcutType, для того щоб розрізняти
        //динамічні і закріплені shortcut
        return ShortcutInfo.Builder(this@MainActivity, "${drink.getIdDrink()}${type.key}")
            .setShortLabel(drink.getStrDrink()!!)
            .setLongLabel("${getString(R.string.popup_menu_long_label_prefix)} ${drink.getStrDrink()}")
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
    }


    private fun shortcutAlreadyExist(drinkId: Long, shortcutType: ShortcutType): Boolean {
        var isExist = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager: ShortcutManager? =
                ContextCompat.getSystemService(this, ShortcutManager::class.java)

            val shortcuts: List<ShortcutInfo>
            shortcuts = when (shortcutType) {
                ShortcutType.PINNED_SHORTCUT -> shortcutManager!!.pinnedShortcuts
                ShortcutType.DYNAMIC_SHORTCUT -> shortcutManager!!.dynamicShortcuts
            }
            shortcuts.forEach { shortcutInfo: ShortcutInfo? ->
                //id для shortcut виставлено як id самого напою + ShortcutType, для того щоб розрізняти
                //динамічні і закріплені shortcut
                if (shortcutInfo?.id == "${drinkId}${shortcutType.key}") {
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

