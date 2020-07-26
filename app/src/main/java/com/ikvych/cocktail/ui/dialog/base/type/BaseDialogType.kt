package com.ikvych.cocktail.ui.dialog.base.type

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object ResumeApplicationDialogType: DialogType<RegularDialogButton>()
object NotificationDialogType: DialogType<SingleDialogButton>()
object FilterDrinkDialogType: DialogType<ListDialogButton>()
object FilterListDrinkDialogType: DialogType<ListDialogButton>()
object SortDrinkDrinkDialogType: DialogType<ListDialogButton>()
