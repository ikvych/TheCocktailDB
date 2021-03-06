package com.ikvych.cocktail.presentation.model.cocktail

import android.os.Parcel
import android.os.Parcelable
import com.ikvych.cocktail.presentation.filter.type.*
import java.util.*

data class CocktailModel(
    val id: Long = -1L,
    val names: LocalizedStringModel = LocalizedStringModel(),
    val category: CategoryDrinkFilter = CategoryDrinkFilter.NONE,
    val alcoholType: AlcoholDrinkFilter = AlcoholDrinkFilter.NONE,
    val glass: GlassDrinkFilter = GlassDrinkFilter.NONE,
    val image: String = "",
    val instructions: LocalizedStringModel = LocalizedStringModel(),
    val ingredients: List<IngredientModel> = emptyList(),
    val measures: List<String> = emptyList(),
    val cocktailOfTheDay: String = "",
    var isFavorite: Boolean = false,
    val dateModified: Date = Date(),
    val dateSaved: Date? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        LocalizedStringModel(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ),
        CategoryDrinkFilter.values()[parcel.readInt()],
        AlcoholDrinkFilter.values()[parcel.readInt()],
        GlassDrinkFilter.values()[parcel.readInt()],
        parcel.readString()!!,
        LocalizedStringModel(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ),
        parcel.createStringArray()!!.map { IngredientModel(DrinkFilterType.INGREDIENT, it) },
        parcel.createStringArrayList()!!.toList(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong()),
        Date(parcel.readLong())
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(names.defaultName)
        parcel.writeString(names.defaultAlternate)
        parcel.writeString(names.es)
        parcel.writeString(names.de)
        parcel.writeString(names.fr)
        parcel.writeString(names.zhHans)
        parcel.writeString(names.zhHant)
        parcel.writeInt(category.ordinal)
        parcel.writeInt(alcoholType.ordinal)
        parcel.writeInt(glass.ordinal)
        parcel.writeString(image)
        parcel.writeString(instructions.defaultName)
        parcel.writeString(instructions.defaultAlternate)
        parcel.writeString(instructions.es)
        parcel.writeString(instructions.de)
        parcel.writeString(instructions.fr)
        parcel.writeString(instructions.zhHans)
        parcel.writeString(instructions.zhHant)
        val stringArray: Array<String> = Array(ingredients.size) {
            ingredients[it].key
        }
        parcel.writeStringArray(stringArray)
        parcel.writeStringList(measures)
        parcel.writeString(cocktailOfTheDay)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeLong(dateModified.time)
        parcel.writeLong(dateSaved?.time ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CocktailModel> {
        override fun createFromParcel(parcel: Parcel): CocktailModel {
            return CocktailModel(parcel)
        }

        override fun newArray(size: Int): Array<CocktailModel?> {
            return arrayOfNulls(size)
        }
    }
}