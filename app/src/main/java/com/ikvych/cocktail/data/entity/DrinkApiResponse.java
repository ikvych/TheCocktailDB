package com.ikvych.cocktail.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DrinkApiResponse implements Parcelable {

    @SerializedName("drinks")
    @Expose
    private List<Drink> drinks = new ArrayList<>();

    public final static Parcelable.Creator<DrinkApiResponse> CREATOR = new Creator<DrinkApiResponse>() {

        public DrinkApiResponse createFromParcel(Parcel in) {
            return new DrinkApiResponse(in);
        }

        public DrinkApiResponse[] newArray(int size) {
            return (new DrinkApiResponse[size]);
        }

    };

    private DrinkApiResponse(Parcel in) {
        in.readList(this.drinks, (Drink.class.getClassLoader()));
    }

    public DrinkApiResponse() {
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(drinks);
    }

    public int describeContents() {
        return 0;
    }

}
