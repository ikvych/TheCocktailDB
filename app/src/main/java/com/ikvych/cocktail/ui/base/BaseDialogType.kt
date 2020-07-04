package com.ikvych.cocktail.ui.base

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object ResumeApplicationDialogType: DialogType<RegularDialogButton>()
object NotificationDialogType: DialogType<SingleDialogButton>()
object AlcoholDrinkDialogType: DialogType<ListDialogButton>()
object CategoryDrinkDialogType: DialogType<ListDialogButton>()
object IngredientDrinkDialogType: DialogType<ListDialogButton>()
object SortDrinkDrinkDialogType: DialogType<ListDialogButton>()
