package com.ikvych.cocktail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DrinkApiResponse private constructor(`in`: Parcel) : Parcelable {
    @SerializedName("drinks")
    @Expose
    var drinks: List<Drink> = ArrayList()

    init {
        `in`.readList(drinks, Drink::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(drinks)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<DrinkApiResponse> {
        override fun createFromParcel(`in`: Parcel): DrinkApiResponse {
            return DrinkApiResponse(`in`)
        }

        override fun newArray(size: Int): Array<DrinkApiResponse?> {
            return arrayOfNulls(size)
        }
    }

}