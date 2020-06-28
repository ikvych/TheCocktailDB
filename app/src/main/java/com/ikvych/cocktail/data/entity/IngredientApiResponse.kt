package com.ikvych.cocktail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class IngredientApiResponse private constructor(`in`: Parcel) : Parcelable {
    @SerializedName("drinks")
    @Expose
    var ingredients: List<Ingredient> = ArrayList()

    init {
        `in`.readList(ingredients, Drink::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(ingredients)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<IngredientApiResponse> {
        override fun createFromParcel(`in`: Parcel): IngredientApiResponse {
            return IngredientApiResponse(`in`)
        }

        override fun newArray(size: Int): Array<IngredientApiResponse?> {
            return arrayOfNulls(size)
        }
    }

}