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
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.ui.dialog.ResumeAppBottomSheetDialogFragment
import com.ikvych.cocktail.ui.dialog.base.type.*
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.viewmodel.MainActivityViewModel

class MainActivity : BaseActivity<MainActivityViewModel>() {

    override var contentLayoutResId: Int = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView
    private var mainFragment: MainFragment? = null
    private var profileFragment: ProfileFragment? = null

    override fun configureView(savedInstanceState: Bundle?) {
        //Відслідковуємо напій дня, якщо він не дорівнює null значить потрібно показати діалог
        viewModel.drinkOfTheDayLiveData.observe(this, Observer {
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
                    //обнуляю liveData для того щоб на перестворенні актівіті не спамилось
                    viewModel.drinkOfTheDayLiveData.value = null
                }
            }
        })

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
    }

    override fun onClick(v: View?) {

        // відкриває деталізацію коктейлю
        if (v is CardView) {
            val view = v.findViewById<TextView>(R.id.tv_drink_name)
            val drinkName = view?.text
            if (drinkName != null) {
                val drink = viewModel.findDrinkByName(drinkName.toString())
                val intent = Intent(this, DrinkDetailActivity::class.java)
                intent.putExtra(DRINK_ID, drink!!.getIdDrink())
                startActivity(intent)
            }
        }
        // додає і видаляє з улюблених
        if (v is CheckBox) {
            val drinkName = v.tag as String
            val drink = viewModel.findDrinkByName(drinkName) ?: return
            if (v.isChecked) {
                drink.setIsFavorite(true)
                viewModel.saveDrinkIntoDb(drink)
            } else {
                drink.setIsFavorite(false)
                viewModel.saveDrinkIntoDb(drink)
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener {
                val view = v?.findViewById<TextView>(R.id.tv_drink_name)
                when (it.itemId) {
                    // відкриває деталізацію напою
                    R.id.menu_drink_open -> {

                        val drinkName = view?.text ?: ""
                        val drink = viewModel.findDrinkByName(drinkName.toString())

                        val intent = Intent(this@MainActivity, DrinkDetailActivity::class.java)
                        intent.putExtra(DRINK, drink)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_drink_add_favorite -> {

                        val drinkName = view?.text ?: ""
                        val drink = viewModel.findDrinkByName(drinkName.toString()) ?: return@setOnMenuItemClickListener false

                        if (!drink.isFavorite()) {
                            drink.setIsFavorite(true)
                            viewModel.saveDrinkIntoDb(drink)
                            Toast.makeText(
                                this@MainActivity,
                                "Added to favorite",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Already added to favorite",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                    R.id.menu_drink_remove_favorite -> {

                        val drinkName = view?.text ?: ""
                        val drink = viewModel.findDrinkByName(drinkName.toString()) ?: return@setOnMenuItemClickListener false
                        if (drink.isFavorite()) {
                            drink.setIsFavorite(false)
                            viewModel.saveDrinkIntoDb(drink)
                            Toast.makeText(
                                this@MainActivity,
                                "Removed from favorite",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Not added yet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                    R.id.menu_drink_remove -> {

                        val drinkName = view?.text ?: ""
                        val drink = viewModel.findDrinkByName(drinkName.toString()) ?: return@setOnMenuItemClickListener false

                        viewModel.removeDrink(drink)
                        Toast.makeText(this@MainActivity, "Drink removed", Toast.LENGTH_SHORT).show()
                        true
                    }
                    // створює pinned shortcut
                    R.id.menu_drink_pin_shortcut -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val textViewDrinkName =
                                v?.findViewById<TextView>(R.id.tv_drink_name)
                            val drinkName = textViewDrinkName?.text ?: ""
                            val drink = viewModel.findDrinkByName(drinkName.toString())
                                ?: return@setOnMenuItemClickListener false
                            val shortcutManager: ShortcutManager? =
                                ContextCompat.getSystemService(
                                    this@MainActivity,
                                    ShortcutManager::class.java
                                )
                            val drinkImageView =
                                v!!.findViewById<ImageView>(R.id.iv_drink_image)
                            val drinkAdaptiveIcon =
                                drinkImageView.drawToBitmap().toAdaptiveIcon()
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

