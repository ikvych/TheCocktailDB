package com.ikvych.cocktail.ui.dialog.base

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object NotificationDialogType: DialogType<SingleDialogButton>()
object AlcoholDrinkType: DialogType<ListDialogButton>()
object CategoryDrinkType: DialogType<ListDialogButton>()
object IngredientDrinkType: DialogType<ListDialogButton>()
