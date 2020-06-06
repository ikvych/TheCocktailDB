package com.ikvych.cocktail.data.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ikvych.cocktail.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "drink")
public class Drink extends BaseObservable implements Parcelable{

    @SerializedName("idDrink")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_drink")
    private Long idDrink;

    @ColumnInfo(name = "created")
    private Date createdAt;

    @SerializedName("strDrink")
    @Expose
    @ColumnInfo(name = "str_drink")
    private String strDrink;

    @SerializedName("strDrinkAlternate")
    @Expose
    @Ignore
    private Object strDrinkAlternate;

    @SerializedName("strDrinkES")
    @Expose
    @Ignore
    private Object strDrinkES;

    @SerializedName("strDrinkDE")
    @Expose
    @Ignore
    private Object strDrinkDE;

    @SerializedName("strDrinkFR")
    @Expose
    @Ignore
    private Object strDrinkFR;

    @SerializedName("strDrinkZH-HANS")
    @Expose
    @Ignore
    private Object strDrinkZHHANS;

    @SerializedName("strDrinkZH-HANT")
    @Expose
    @Ignore
    private Object strDrinkZHHANT;

    @SerializedName("strTags")
    @Expose
    @Ignore
    private Object strTags;

    @SerializedName("strVideo")
    @Expose
    @Ignore
    private Object strVideo;

    @SerializedName("strCategory")
    @Expose
    @Ignore
    private String strCategory;

    @SerializedName("strIBA")
    @Expose
    @Ignore
    private Object strIBA;

    @SerializedName("strAlcoholic")
    @Expose
    @ColumnInfo(name = "str_alcoholic")
    private String strAlcoholic;

    @SerializedName("strGlass")
    @Expose
    @ColumnInfo(name = "str_glass")
    private String strGlass;

    @SerializedName("strInstructions")
    @Expose
    @ColumnInfo(name = "str_instructions")
    private String strInstructions;

    @SerializedName("strInstructionsES")
    @Expose
    @Ignore
    private Object strInstructionsES;

    @SerializedName("strInstructionsDE")
    @Expose
    @Ignore
    private String strInstructionsDE;

    @SerializedName("strInstructionsFR")
    @Expose
    @Ignore
    private Object strInstructionsFR;

    @SerializedName("strInstructionsZH-HANS")
    @Expose
    @Ignore
    private Object strInstructionsZHHANS;

    @SerializedName("strInstructionsZH-HANT")
    @Expose
    @Ignore
    private Object strInstructionsZHHANT;

    @SerializedName("strDrinkThumb")
    @Expose
    @ColumnInfo(name = "str_drink_thumb")
    private String strDrinkThumb;

    @Ignore
    private Map<String, String> ingredients = new HashMap<>();

    @Bindable
    public Map<String, String> getIngredients() {
        ingredients.put(strIngredient1, strMeasure1);
        ingredients.put(strIngredient2, strMeasure2);
        ingredients.put(strIngredient3, strMeasure3);
        ingredients.put(strIngredient4, strMeasure4);
        ingredients.put(strIngredient5, strMeasure5);
        ingredients.put(strIngredient6, strMeasure6);
        ingredients.put(strIngredient7, strMeasure7);
        ingredients.put(strIngredient8, strMeasure8);
        ingredients.put(strIngredient9, strMeasure9);
        ingredients.put(strIngredient10, strMeasure10);
        ingredients.put(strIngredient11, strMeasure11);
        ingredients.put(strIngredient12, strMeasure12);
        ingredients.put(strIngredient13, strMeasure13);
        ingredients.put(strIngredient14, strMeasure14);
        ingredients.put(strIngredient15, strMeasure15);
        return ingredients;
    }

    public void setIngredients(Map<String, String> ingredients) {
        this.ingredients = ingredients;
        notifyPropertyChanged(BR.ingredients);
    }

    @BindingAdapter({"ingredients"})
    public static void getIngredients(TableLayout tableLayout, Map<String, String> ingredients) {
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            TableRow tr = (TableRow) LayoutInflater.from(tableLayout.getContext())
                    .inflate(R.layout.item_ingredient_list, tableLayout, false);

            TextView ingredient = tr.findViewById(R.id.ingredients);
            ingredient.setText(entry.getKey());

            TextView measure = tr.findViewById(R.id.measure);
            measure.setText(entry.getValue());

            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    @BindingAdapter({"strDrinkThumb"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_icon)
                .into(imageView);
    }

    @SerializedName("strIngredient1")
    @Expose
    @ColumnInfo(name = "str_ingredient1")
    private String strIngredient1;

    @SerializedName("strIngredient2")
    @Expose
    @ColumnInfo(name = "str_ingredient2")
    private String strIngredient2;

    @SerializedName("strIngredient3")
    @Expose
    @ColumnInfo(name = "str_ingredient3")
    private String strIngredient3;

    @SerializedName("strIngredient4")
    @Expose
    @ColumnInfo(name = "str_ingredient4")
    private String strIngredient4;

    @SerializedName("strIngredient5")
    @Expose
    @ColumnInfo(name = "str_ingredient5")
    private String strIngredient5;

    @SerializedName("strIngredient6")
    @Expose
    @ColumnInfo(name = "str_ingredient6")
    private String strIngredient6;

    @SerializedName("strIngredient7")
    @Expose
    @ColumnInfo(name = "str_ingredient7")
    private String strIngredient7;

    @SerializedName("strIngredient8")
    @Expose
    @ColumnInfo(name = "str_ingredient8")
    private String strIngredient8;

    @SerializedName("strIngredient9")
    @Expose
    @ColumnInfo(name = "str_ingredient9")
    private String strIngredient9;

    @SerializedName("strIngredient10")
    @Expose
    @ColumnInfo(name = "str_ingredient10")
    private String strIngredient10;

    @SerializedName("strIngredient11")
    @Expose
    @ColumnInfo(name = "str_ingredient11")
    private String strIngredient11;

    @SerializedName("strIngredient12")
    @Expose
    @ColumnInfo(name = "str_ingredient12")
    private String strIngredient12;

    @SerializedName("strIngredient13")
    @Expose
    @ColumnInfo(name = "str_ingredient13")
    private String strIngredient13;

    @SerializedName("strIngredient14")
    @Expose
    @ColumnInfo(name = "str_ingredient14")
    private String strIngredient14;

    @SerializedName("strIngredient15")
    @Expose
    @ColumnInfo(name = "str_ingredient15")
    private String strIngredient15;

    @SerializedName("strMeasure1")
    @Expose
    @ColumnInfo(name = "str_measure1")
    private String strMeasure1;

    @SerializedName("strMeasure2")
    @Expose
    @ColumnInfo(name = "str_measure2")
    private String strMeasure2;

    @SerializedName("strMeasure3")
    @Expose
    @ColumnInfo(name = "str_measure3")
    private String strMeasure3;

    @SerializedName("strMeasure4")
    @Expose
    @ColumnInfo(name = "str_measure4")
    private String strMeasure4;

    @SerializedName("strMeasure5")
    @Expose
    @ColumnInfo(name = "str_measure5")
    private String strMeasure5;

    @SerializedName("strMeasure6")
    @Expose
    @ColumnInfo(name = "str_measure6")
    private String strMeasure6;

    @SerializedName("strMeasure7")
    @Expose
    @ColumnInfo(name = "str_measure7")
    private String strMeasure7;

    @SerializedName("strMeasure8")
    @Expose
    @ColumnInfo(name = "str_measure8")
    private String strMeasure8;

    @SerializedName("strMeasure9")
    @Expose
    @ColumnInfo(name = "str_measure9")
    private String strMeasure9;

    @SerializedName("strMeasure10")
    @Expose
    @ColumnInfo(name = "str_measure10")
    private String strMeasure10;

    @SerializedName("strMeasure11")
    @Expose
    @ColumnInfo(name = "str_measure11")
    private String strMeasure11;

    @SerializedName("strMeasure12")
    @Expose
    @ColumnInfo(name = "str_measure12")
    private String strMeasure12;

    @SerializedName("strMeasure13")
    @Expose
    @ColumnInfo(name = "str_measure13")
    private String strMeasure13;

    @SerializedName("strMeasure14")
    @Expose
    @ColumnInfo(name = "str_measure14")
    private String strMeasure14;

    @SerializedName("strMeasure15")
    @Expose
    @ColumnInfo(name = "str_measure15")
    private String strMeasure15;

    @SerializedName("strCreativeCommonsConfirmed")
    @Expose
    @Ignore
    private String strCreativeCommonsConfirmed;

    @SerializedName("dateModified")
    @Expose
    @Ignore
    private String dateModified;


    public final static Parcelable.Creator<Drink> CREATOR = new Creator<Drink>() {


        @SuppressWarnings({"unchecked"})
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        public Drink[] newArray(int size) {
            return (new Drink[size]);
        }

    };

    protected Drink(Parcel in) {
        this.idDrink = ((Long) in.readValue((String.class.getClassLoader())));
        this.strDrink = ((String) in.readValue((String.class.getClassLoader())));
        this.strDrinkAlternate = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkES = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkDE = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkFR = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkZHHANS = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkZHHANT = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strTags = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strVideo = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strCategory = ((String) in.readValue((String.class.getClassLoader())));
        this.strIBA = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strAlcoholic = ((String) in.readValue((String.class.getClassLoader())));
        this.strGlass = ((String) in.readValue((String.class.getClassLoader())));
        this.strInstructions = ((String) in.readValue((String.class.getClassLoader())));
        this.strInstructionsES = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strInstructionsDE = ((String) in.readValue((String.class.getClassLoader())));
        this.strInstructionsFR = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strInstructionsZHHANS = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strInstructionsZHHANT = ((Object) in.readValue((Object.class.getClassLoader())));
        this.strDrinkThumb = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient1 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient2 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient3 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient4 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient5 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient6 = ((String) in.readValue((String.class.getClassLoader())));
        this.strIngredient7 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient8 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient9 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient10 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient11 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient12 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient13 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient14 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strIngredient15 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure1 = ((String) in.readValue((String.class.getClassLoader())));
        this.strMeasure2 = ((String) in.readValue((String.class.getClassLoader())));
        this.strMeasure3 = ((String) in.readValue((String.class.getClassLoader())));
        this.strMeasure4 = ((String) in.readValue((String.class.getClassLoader())));
        this.strMeasure5 = ((String) in.readValue((String.class.getClassLoader())));
        this.strMeasure6 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure7 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure8 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure9 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure10 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure11 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure12 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure13 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure14 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strMeasure15 = ((String) in.readValue((Object.class.getClassLoader())));
        this.strCreativeCommonsConfirmed = ((String) in.readValue((String.class.getClassLoader())));
        this.dateModified = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Drink() {
    }

    @Bindable
    public Long getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(Long idDrink) {
        this.idDrink = idDrink;
        notifyPropertyChanged(BR.idDrink);
    }

    @Bindable
    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
        notifyPropertyChanged(BR.strDrink);
    }

    @Bindable
    public Object getStrDrinkAlternate() {
        return strDrinkAlternate;
    }

    public void setStrDrinkAlternate(Object strDrinkAlternate) {
        this.strDrinkAlternate = strDrinkAlternate;
        notifyPropertyChanged(BR.strDrinkAlternate);
    }

    @Bindable
    public Object getStrDrinkES() {
        return strDrinkES;
    }

    public void setStrDrinkES(Object strDrinkES) {
        this.strDrinkES = strDrinkES;
        notifyPropertyChanged(BR.strDrinkES);
    }

    @Bindable
    public Object getStrDrinkDE() {
        return strDrinkDE;
    }

    public void setStrDrinkDE(Object strDrinkDE) {
        this.strDrinkDE = strDrinkDE;
        notifyPropertyChanged(BR.strDrinkDE);
    }

    @Bindable
    public Object getStrDrinkFR() {
        return strDrinkFR;
    }

    public void setStrDrinkFR(Object strDrinkFR) {
        this.strDrinkFR = strDrinkFR;
        notifyPropertyChanged(BR.strDrinkFR);
    }

    @Bindable
    public Object getStrDrinkZHHANS() {
        return strDrinkZHHANS;
    }

    public void setStrDrinkZHHANS(Object strDrinkZHHANS) {
        this.strDrinkZHHANS = strDrinkZHHANS;
        notifyPropertyChanged(BR.strDrinkZHHANS);
    }

    @Bindable
    public Object getStrDrinkZHHANT() {
        return strDrinkZHHANT;
    }

    public void setStrDrinkZHHANT(Object strDrinkZHHANT) {
        this.strDrinkZHHANT = strDrinkZHHANT;
        notifyPropertyChanged(BR.strDrinkZHHANT);
    }

    @Bindable
    public Object getStrTags() {
        return strTags;
    }

    public void setStrTags(Object strTags) {
        this.strTags = strTags;
        notifyPropertyChanged(BR.strTags);
    }

    @Bindable
    public Object getStrVideo() {
        return strVideo;
    }

    public void setStrVideo(Object strVideo) {
        this.strVideo = strVideo;
        notifyPropertyChanged(BR.strVideo);
    }

    @Bindable
    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
        notifyPropertyChanged(BR.strCategory);
    }

    @Bindable
    public Object getStrIBA() {
        return strIBA;
    }

    public void setStrIBA(Object strIBA) {
        this.strIBA = strIBA;
        notifyPropertyChanged(BR.strIBA);
    }

    @Bindable
    public String getStrAlcoholic() {
        return strAlcoholic;
    }

    public void setStrAlcoholic(String strAlcoholic) {
        this.strAlcoholic = strAlcoholic;
        notifyPropertyChanged(BR.strAlcoholic);
    }

    @Bindable
    public String getStrGlass() {
        return strGlass;
    }

    public void setStrGlass(String strGlass) {
        this.strGlass = strGlass;
        notifyPropertyChanged(BR.strGlass);
    }

    @Bindable
    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
        notifyPropertyChanged(BR.strInstructions);
    }

    @Bindable
    public Object getStrInstructionsES() {
        return strInstructionsES;
    }

    public void setStrInstructionsES(Object strInstructionsES) {
        this.strInstructionsES = strInstructionsES;
        notifyPropertyChanged(BR.strInstructionsES);
    }

    @Bindable
    public String getStrInstructionsDE() {
        return strInstructionsDE;
    }

    public void setStrInstructionsDE(String strInstructionsDE) {
        this.strInstructionsDE = strInstructionsDE;
        notifyPropertyChanged(BR.strInstructionsDE);
    }

    @Bindable
    public Object getStrInstructionsFR() {
        return strInstructionsFR;
    }

    public void setStrInstructionsFR(Object strInstructionsFR) {
        this.strInstructionsFR = strInstructionsFR;
        notifyPropertyChanged(BR.strInstructionsFR);
    }

    @Bindable
    public Object getStrInstructionsZHHANS() {
        return strInstructionsZHHANS;
    }

    public void setStrInstructionsZHHANS(Object strInstructionsZHHANS) {
        this.strInstructionsZHHANS = strInstructionsZHHANS;
        notifyPropertyChanged(BR.strInstructionsZHHANS);
    }

    @Bindable
    public Object getStrInstructionsZHHANT() {
        return strInstructionsZHHANT;
    }

    public void setStrInstructionsZHHANT(Object strInstructionsZHHANT) {
        this.strInstructionsZHHANT = strInstructionsZHHANT;
        notifyPropertyChanged(BR.strInstructionsZHHANT);
    }

    @Bindable
    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
        notifyPropertyChanged(BR.strDrinkThumb);
    }

    @Bindable
    public String getStrIngredient1() {
        return strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
        notifyPropertyChanged(BR.strIngredient1);
    }

    @Bindable
    public String getStrIngredient2() {
        return strIngredient2;
    }

    public void setStrIngredient2(String strIngredient2) {
        this.strIngredient2 = strIngredient2;
        notifyPropertyChanged(BR.strIngredient2);
    }

    @Bindable
    public String getStrIngredient3() {
        return strIngredient3;
    }

    public void setStrIngredient3(String strIngredient3) {
        this.strIngredient3 = strIngredient3;
        notifyPropertyChanged(BR.strIngredient3);
    }

    @Bindable
    public String getStrIngredient4() {
        return strIngredient4;
    }

    public void setStrIngredient4(String strIngredient4) {
        this.strIngredient4 = strIngredient4;
        notifyPropertyChanged(BR.strIngredient4);
    }

    @Bindable
    public String getStrIngredient5() {
        return strIngredient5;
    }

    public void setStrIngredient5(String strIngredient5) {
        this.strIngredient5 = strIngredient5;
        notifyPropertyChanged(BR.strIngredient5);
    }

    @Bindable
    public String getStrIngredient6() {
        return strIngredient6;
    }

    public void setStrIngredient6(String strIngredient6) {
        this.strIngredient6 = strIngredient6;
        notifyPropertyChanged(BR.strIngredient6);
    }

    @Bindable
    public String getStrIngredient7() {
        return strIngredient7;
    }

    public void setStrIngredient7(String strIngredient7) {
        this.strIngredient7 = strIngredient7;
        notifyPropertyChanged(BR.strIngredient7);
    }

    @Bindable
    public String getStrIngredient8() {
        return strIngredient8;
    }

    public void setStrIngredient8(String strIngredient8) {
        this.strIngredient8 = strIngredient8;
        notifyPropertyChanged(BR.strIngredient8);
    }

    @Bindable
    public String getStrIngredient9() {
        return strIngredient9;
    }

    public void setStrIngredient9(String strIngredient9) {
        this.strIngredient9 = strIngredient9;
        notifyPropertyChanged(BR.strIngredient9);
    }

    @Bindable
    public String getStrIngredient10() {
        return strIngredient10;
    }

    public void setStrIngredient10(String strIngredient10) {
        this.strIngredient10 = strIngredient10;
        notifyPropertyChanged(BR.strIngredient10);
    }

    @Bindable
    public String getStrIngredient11() {
        return strIngredient11;
    }

    public void setStrIngredient11(String strIngredient11) {
        this.strIngredient11 = strIngredient11;
        notifyPropertyChanged(BR.strIngredient11);
    }

    @Bindable
    public String getStrIngredient12() {
        return strIngredient12;
    }

    public void setStrIngredient12(String strIngredient12) {
        this.strIngredient12 = strIngredient12;
        notifyPropertyChanged(BR.strIngredient12);
    }

    @Bindable
    public String getStrIngredient13() {
        return strIngredient13;
    }

    public void setStrIngredient13(String strIngredient13) {
        this.strIngredient13 = strIngredient13;
        notifyPropertyChanged(BR.strIngredient13);
    }

    @Bindable
    public String getStrIngredient14() {
        return strIngredient14;
    }

    public void setStrIngredient14(String strIngredient14) {
        this.strIngredient14 = strIngredient14;
        notifyPropertyChanged(BR.strIngredient14);
    }

    @Bindable
    public String getStrIngredient15() {
        return strIngredient15;
    }

    public void setStrIngredient15(String strIngredient15) {
        this.strIngredient15 = strIngredient15;
        notifyPropertyChanged(BR.strIngredient15);
    }

    @Bindable
    public String getStrMeasure1() {
        return strMeasure1;
    }

    public void setStrMeasure1(String strMeasure1) {
        this.strMeasure1 = strMeasure1;
        notifyPropertyChanged(BR.strMeasure1);
    }

    @Bindable
    public String getStrMeasure2() {
        return strMeasure2;
    }

    public void setStrMeasure2(String strMeasure2) {
        this.strMeasure2 = strMeasure2;
        notifyPropertyChanged(BR.strMeasure2);
    }

    @Bindable
    public String getStrMeasure3() {
        return strMeasure3;
    }

    public void setStrMeasure3(String strMeasure3) {
        this.strMeasure3 = strMeasure3;
        notifyPropertyChanged(BR.strMeasure3);
    }

    @Bindable
    public String getStrMeasure4() {
        return strMeasure4;
    }

    public void setStrMeasure4(String strMeasure4) {
        this.strMeasure4 = strMeasure4;
        notifyPropertyChanged(BR.strMeasure4);
    }

    @Bindable
    public String getStrMeasure5() {
        return strMeasure5;
    }

    public void setStrMeasure5(String strMeasure5) {
        this.strMeasure5 = strMeasure5;
        notifyPropertyChanged(BR.strMeasure5);
    }

    @Bindable
    public String getStrMeasure6() {
        return strMeasure6;
    }

    public void setStrMeasure6(String strMeasure6) {
        this.strMeasure6 = strMeasure6;
        notifyPropertyChanged(BR.strMeasure6);
    }

    @Bindable
    public String getStrMeasure7() {
        return strMeasure7;
    }

    public void setStrMeasure7(String strMeasure7) {
        this.strMeasure7 = strMeasure7;
        notifyPropertyChanged(BR.strMeasure7);
    }

    @Bindable
    public String getStrMeasure8() {
        return strMeasure8;
    }

    public void setStrMeasure8(String strMeasure8) {
        this.strMeasure8 = strMeasure8;
        notifyPropertyChanged(BR.strMeasure8);
    }

    @Bindable
    public String getStrMeasure9() {
        return strMeasure9;
    }

    public void setStrMeasure9(String strMeasure9) {
        this.strMeasure9 = strMeasure9;
        notifyPropertyChanged(BR.strMeasure9);
    }

    @Bindable
    public String getStrMeasure10() {
        return strMeasure10;
    }

    public void setStrMeasure10(String strMeasure10) {
        this.strMeasure10 = strMeasure10;
        notifyPropertyChanged(BR.strMeasure10);
    }

    @Bindable
    public String getStrMeasure11() {
        return strMeasure11;
    }

    public void setStrMeasure11(String strMeasure11) {
        this.strMeasure11 = strMeasure11;
        notifyPropertyChanged(BR.strMeasure11);
    }

    @Bindable
    public String getStrMeasure12() {
        return strMeasure12;
    }

    public void setStrMeasure12(String strMeasure12) {
        this.strMeasure12 = strMeasure12;
        notifyPropertyChanged(BR.strMeasure12);
    }

    @Bindable
    public String getStrMeasure13() {
        return strMeasure13;
    }

    public void setStrMeasure13(String strMeasure13) {
        this.strMeasure13 = strMeasure13;
        notifyPropertyChanged(BR.strMeasure13);
    }

    @Bindable
    public String getStrMeasure14() {
        return strMeasure14;
    }

    public void setStrMeasure14(String strMeasure14) {
        this.strMeasure14 = strMeasure14;
        notifyPropertyChanged(BR.strMeasure14);
    }

    @Bindable
    public String getStrMeasure15() {
        return strMeasure15;
    }

    public void setStrMeasure15(String strMeasure15) {
        this.strMeasure15 = strMeasure15;
        notifyPropertyChanged(BR.strMeasure15);
    }

    @Bindable
    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public void setStrCreativeCommonsConfirmed(String strCreativeCommonsConfirmed) {
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
        notifyPropertyChanged(BR.strCreativeCommonsConfirmed);
    }

    @Bindable
    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
        notifyPropertyChanged(BR.dateModified);
    }


    public Date getCreatedAt() {
        return new Date();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(idDrink);
        dest.writeValue(strDrink);
        dest.writeValue(strDrinkAlternate);
        dest.writeValue(strDrinkES);
        dest.writeValue(strDrinkDE);
        dest.writeValue(strDrinkFR);
        dest.writeValue(strDrinkZHHANS);
        dest.writeValue(strDrinkZHHANT);
        dest.writeValue(strTags);
        dest.writeValue(strVideo);
        dest.writeValue(strCategory);
        dest.writeValue(strIBA);
        dest.writeValue(strAlcoholic);
        dest.writeValue(strGlass);
        dest.writeValue(strInstructions);
        dest.writeValue(strInstructionsES);
        dest.writeValue(strInstructionsDE);
        dest.writeValue(strInstructionsFR);
        dest.writeValue(strInstructionsZHHANS);
        dest.writeValue(strInstructionsZHHANT);
        dest.writeValue(strDrinkThumb);
        dest.writeValue(strIngredient1);
        dest.writeValue(strIngredient2);
        dest.writeValue(strIngredient3);
        dest.writeValue(strIngredient4);
        dest.writeValue(strIngredient5);
        dest.writeValue(strIngredient6);
        dest.writeValue(strIngredient7);
        dest.writeValue(strIngredient8);
        dest.writeValue(strIngredient9);
        dest.writeValue(strIngredient10);
        dest.writeValue(strIngredient11);
        dest.writeValue(strIngredient12);
        dest.writeValue(strIngredient13);
        dest.writeValue(strIngredient14);
        dest.writeValue(strIngredient15);
        dest.writeValue(strMeasure1);
        dest.writeValue(strMeasure2);
        dest.writeValue(strMeasure3);
        dest.writeValue(strMeasure4);
        dest.writeValue(strMeasure5);
        dest.writeValue(strMeasure6);
        dest.writeValue(strMeasure7);
        dest.writeValue(strMeasure8);
        dest.writeValue(strMeasure9);
        dest.writeValue(strMeasure10);
        dest.writeValue(strMeasure11);
        dest.writeValue(strMeasure12);
        dest.writeValue(strMeasure13);
        dest.writeValue(strMeasure14);
        dest.writeValue(strMeasure15);
        dest.writeValue(strCreativeCommonsConfirmed);
        dest.writeValue(dateModified);
    }

    public int describeContents() {
        return 0;
    }

}
