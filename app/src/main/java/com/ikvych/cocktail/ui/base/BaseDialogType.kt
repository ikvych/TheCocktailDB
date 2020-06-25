package com.ikvych.cocktail.ui.base

sealed class DialogType<out ButtonTypeParent: DialogButton>
object RegularDialogType: DialogType<RegularDialogButton>()
object NetworkDialogType: DialogType<SingleDialogButton>()
object DayPickerDialogType: DialogType<SingleDialogButton>()
object DatePickerDialogType: DialogType<SingleDialogButton>()
object AlcoholDrinkType: DialogType<ListDialogButton>()
object CategoryDrinkType: DialogType<ListDialogButton>()
object IngredientDrinkType: DialogType<ListDialogButton>()
object ClinicServiceDialogType: DialogType<ListDialogButton>()
object DoctorSortDialogType: DialogType<ListDialogButton>()
object ClinicSortDialogType: DialogType<ListDialogButton>()
object ClinicDialogType: DialogType<ListDialogButton>()