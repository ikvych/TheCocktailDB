package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toAdaptiveIcon
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityMainBinding
import com.ikvych.cocktail.listener.MainActivityLifecycleObserver
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.dialog.bottom.ResumeAppBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.*
import com.ikvych.cocktail.presentation.enumeration.ShortcutType
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.presentation.fragment.MainFragment
import com.ikvych.cocktail.presentation.fragment.SettingFragment
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.COCKTAIL_ID
import com.ikvych.cocktail.viewmodel.cocktail.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.notification.NotificationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModelClass: KClass<MainActivityViewModel>
        get() = MainActivityViewModel::class

    private var mainFragment: MainFragment? = null
    private var profileFragment: SettingFragment? = null

    private lateinit var lifecycleObserver: MainActivityLifecycleObserver
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //бібліотека яка дозволяє відслідковувати базу даних через веб браузер у реальному часі
        Stetho.initializeWithDefaults(this)
        super.onCreate(savedInstanceState)
        lifecycleObserver = MainActivityLifecycleObserver(this, notificationViewModel)
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(lifecycleObserver)
        super.onDestroy()
    }

    override fun onResume() {
        viewModel.checkForRemoteConfig()
        super.onResume()
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
                            getString(R.string.resume_app_dialog_description) + "${it.names.defaultName}"
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

        profileFragment =
            supportFragmentManager.findFragmentByTag(SettingFragment::class.java.simpleName)
                    as? SettingFragment
        mainFragment = supportFragmentManager.findFragmentByTag(MainFragment::class.java.simpleName)
                as? MainFragment

        if (profileFragment == null && mainFragment == null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            profileFragment = SettingFragment.newInstance()
            fragmentTransaction.add(
                R.id.fcv_container,
                profileFragment!!,
                SettingFragment::class.java.simpleName
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
                    viewModel.firebase.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundleOf(
                        ANALYTIC_KEY_MAIN_TAB_NAME to ANALYTIC_VALUE_MAIN_TAB
                    ))
                    viewModel.checkForRemoteConfig()
                    true
                }
                R.id.menu_profile_fragment -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.hide(mainFragment!!)
                    ft.show(profileFragment!!)
                    ft.setPrimaryNavigationFragment(profileFragment)
                    ft.commit()
                    viewModel.firebase.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundleOf(
                        ANALYTIC_KEY_MAIN_TAB_NAME to ANALYTIC_VALUE_PROFILE_TAB
                    ))
                    viewModel.checkForRemoteConfig()
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
                    val cocktailId = v.tag as Long
                    val intent = Intent(this, DrinkDetailActivity::class.java)
                    intent.putExtra(COCKTAIL_ID, cocktailId)
                    startActivity(intent)
                }
                R.id.ib_popup_menu -> {
                    showCocktailPopUpMenu(v)
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (v == null) return false
        //v is CardView якій я попередньо прописав tag як id напою
        return when (v.id) {
            R.id.cv_item_drink -> {
                showCocktailPopUpMenu(v)
            }
            else -> false
        }
    }

    private fun showCocktailPopUpMenu(v: View): Boolean {
        val cocktailId = v.tag as Long
        val cocktailLiveData = viewModel.findCocktailById(cocktailId)
        cocktailLiveData.observeOnce {cocktail ->
            if (cocktail == null) return@observeOnce

            PopupMenu(this, v).apply {
                configurePopupMenu(this, cocktail)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        // відкриває деталізацію напою
                        R.id.menu_drink_open -> {
                            val intent =
                                Intent(this@MainActivity, DrinkDetailActivity::class.java)
                            intent.putExtra(COCKTAIL_ID, cocktail.id)
                            startActivity(intent)
                            true
                        }
                        // додає напій до улюблених
                        R.id.menu_drink_add_favorite -> {
                            viewModel.saveFavoriteDrink(cocktail)
                            Toast.makeText(
                                this@MainActivity,
                                "${cocktail.names.defaultName} ${getString(R.string.popup_menu_add_to_favorite)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }
                        // видаляє напій з улюблених
                        R.id.menu_drink_remove_favorite -> {
                            viewModel.saveFavoriteDrink(cocktail)
                            Toast.makeText(
                                this@MainActivity,
                                "${cocktail.names.defaultName} ${getString(R.string.popup_menu_remove_from_favorite)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }
                        R.id.menu_drink_remove -> {
                            viewModel.removeCocktail(cocktail)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val shortcutManager =
                                    ContextCompat.getSystemService(
                                        this@MainActivity,
                                        ShortcutManager::class.java
                                    )
                                shortcutManager?.removeDynamicShortcuts(arrayListOf("${cocktailId}${ShortcutType.DYNAMIC_SHORTCUT}"))
                                shortcutManager?.disableShortcuts(arrayListOf("${cocktailId}${ShortcutType.PINNED_SHORTCUT}"))
                            }
                            Toast.makeText(
                                this@MainActivity,
                                "${cocktail.names.defaultName} ${getString(R.string.popup_menu_remove_from_history)}",
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
                                    createDrinkShortcut(v, cocktail, ShortcutType.PINNED_SHORTCUT)
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
                                    createDrinkShortcut(v, cocktail, ShortcutType.DYNAMIC_SHORTCUT)
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
                                    if (shortcutInfo.shortLabel == cocktail.names.defaultName) {
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
        }
        return true
    }

    private fun configurePopupMenu(menu: PopupMenu, cocktail: CocktailModel) {
        menu.menu.add(0, R.id.menu_drink_open, 0, getString(R.string.open))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (shortcutAlreadyExist(cocktail.id, ShortcutType.PINNED_SHORTCUT)) {
                menu.menu.removeItem(R.id.menu_drink_pin_shortcut)
            } else {
                menu.menu.add(0, R.id.menu_drink_pin_shortcut, 0, getString(R.string.pin_shortcut))
            }
            if (shortcutAlreadyExist(cocktail.id, ShortcutType.DYNAMIC_SHORTCUT)) {
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
        if (cocktail.isFavorite) {
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
    private fun createDrinkShortcut(view: View, cocktail: CocktailModel, type: ShortcutType): ShortcutInfo {
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
        return ShortcutInfo.Builder(this@MainActivity, "${cocktail.id}${type.key}")
            .setShortLabel(cocktail.names.defaultName!!)
            .setLongLabel("${getString(R.string.popup_menu_long_label_prefix)} ${cocktail.id}")
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
                        putExtra(COCKTAIL_ID, cocktail.id)
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

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onDialogFragmentClick(dialog, buttonType, type, data)
        when (type) {
            RatingDialogType -> {
                when (buttonType) {
                    ActionSingleDialogButton -> {
                        val rating = data as Float
                        viewModel.firebase.logEvent(
                            ANALYTIC_KEY_RATE_APP,
                            bundleOf(
                                ANALYTIC_VALUE_RATING to rating
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val ANALYTIC_KEY_MAIN_TAB_NAME = "main_tab_name"
        const val ANALYTIC_KEY_RATE_APP = "rate_app"
        const val ANALYTIC_VALUE_MAIN_TAB = "main"
        const val ANALYTIC_VALUE_PROFILE_TAB = "profile"
        const val ANALYTIC_VALUE_RATING = "rating"
    }
}

