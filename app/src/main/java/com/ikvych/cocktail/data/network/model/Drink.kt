package com.ikvych.cocktail.data.network.model

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TableLayout
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ikvych.cocktail.BR
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.databinding.ItemDrinkIngredientBinding
import java.util.*
import kotlin.collections.HashMap

/*Using getIngredients() method fills the tableLayout in activity_drink_details with ingredients and measure*/
@BindingAdapter("ingredients")
fun getIngredients(
    tableLayout: TableLayout,
    ingredients: Map<String?, String?>? = null
) {
    var count = 1
    if (ingredients == null) {
        return
    }
    for ((key, value) in ingredients) {
        if (key == null) {
            continue
        }
        val binding: ItemDrinkIngredientBinding = DataBindingUtil.inflate(
            LayoutInflater.from(tableLayout.context),
            R.layout.item_drink_ingredient,
            tableLayout,
            false
        )
        val numberedIngredient = "$count. $key"
        binding.tvIngredient.text = numberedIngredient
        binding.tvMeasure.text = value
        tableLayout.addView(
            binding.root,
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        )
        count++
    }
}

@Entity(tableName = Table.DRINK)
class Drink : BaseObservable, Parcelable {
    @SerializedName("idDrink")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id_drink")
    private var idDrink: Long? = null

    @Expose
    @ColumnInfo(name = "created")
    private var created: Date? = null

    @Bindable
    fun getCreated(): Date? {
        if (created == null) {
            created = Date()
        }
        return created
    }

    fun setCreated(created: Date?) {
        this.created = created
        notifyPropertyChanged(BR.created)
    }

    @Expose
    @ColumnInfo(name = "drink_of_day")
    private var drinkOfDay: String? = null

    @Bindable
    fun getDrinkOfDay(): String? {
        return drinkOfDay
    }

    fun setDrinkOfDay(drinkOfDay: String?) {
        this.drinkOfDay = drinkOfDay
        notifyPropertyChanged(BR.drinkOfDay)
    }

    @SerializedName("strDrink")
    @Expose
    @ColumnInfo(name = "str_drink")
    private var strDrink: String? = null

    @ColumnInfo(name = "is_favorite")
    private var isFavorite: Boolean = false

    @SerializedName("strDrinkAlternate")
    @Expose
    @Ignore
    private var strDrinkAlternate: Any? = null

    @SerializedName("strDrinkES")
    @Expose
    @Ignore
    private var strDrinkES: Any? = null

    @SerializedName("strDrinkDE")
    @Expose
    @Ignore
    private var strDrinkDE: Any? = null

    @SerializedName("strDrinkFR")
    @Expose
    @Ignore
    private var strDrinkFR: Any? = null

    @SerializedName("strDrinkZH-HANS")
    @Expose
    @Ignore
    private var strDrinkZHHANS: Any? = null

    @SerializedName("strDrinkZH-HANT")
    @Expose
    @Ignore
    private var strDrinkZHHANT: Any? = null

    @SerializedName("strTags")
    @Expose
    @Ignore
    private var strTags: Any? = null

    @SerializedName("strVideo")
    @Expose
    @Ignore
    private var strVideo: Any? = null

    @SerializedName("strCategory")
    @Expose
    @ColumnInfo(name = "str_category")
    private var strCategory: String? = null

    @SerializedName("strIBA")
    @Expose
    @Ignore
    private var strIBA: Any? = null

    @SerializedName("strAlcoholic")
    @Expose
    @ColumnInfo(name = "str_alcoholic")
    private var strAlcoholic: String? = null

    @SerializedName("strGlass")
    @Expose
    @ColumnInfo(name = "str_glass")
    private var strGlass: String? = null

    @SerializedName("strInstructions")
    @Expose
    @ColumnInfo(name = "str_instructions")
    private var strInstructions: String? = null

    @SerializedName("strInstructionsES")
    @Expose
    @Ignore
    private var strInstructionsES: Any? = null

    @SerializedName("strInstructionsDE")
    @Expose
    @Ignore
    private var strInstructionsDE: String? = null

    @SerializedName("strInstructionsFR")
    @Expose
    @Ignore
    private var strInstructionsFR: Any? = null

    @SerializedName("strInstructionsZH-HANS")
    @Expose
    @Ignore
    private var strInstructionsZHHANS: Any? = null

    @SerializedName("strInstructionsZH-HANT")
    @Expose
    @Ignore
    private var strInstructionsZHHANT: Any? = null

    @SerializedName("strDrinkThumb")
    @Expose
    @ColumnInfo(name = "str_drink_thumb")
    private var strDrinkThumb: String? = null


    @Ignore
    private var ingredients: MutableMap<String?, String?> =
        HashMap()

    /*Fills the map with ingredients and measure*/
    @Bindable
    fun getIngredients(): Map<String?, String?> {
        ingredients[strIngredient1] = strMeasure1
        ingredients[strIngredient2] = strMeasure2
        ingredients[strIngredient3] = strMeasure3
        ingredients[strIngredient4] = strMeasure4
        ingredients[strIngredient5] = strMeasure5
        ingredients[strIngredient6] = strMeasure6
        ingredients[strIngredient7] = strMeasure7
        ingredients[strIngredient8] = strMeasure8
        ingredients[strIngredient9] = strMeasure9
        ingredients[strIngredient10] = strMeasure10
        ingredients[strIngredient11] = strMeasure11
        ingredients[strIngredient12] = strMeasure12
        ingredients[strIngredient13] = strMeasure13
        ingredients[strIngredient14] = strMeasure14
        ingredients[strIngredient15] = strMeasure15
        return ingredients
    }

    fun setIngredients(ingredients: MutableMap<String?, String?>) {
        this.ingredients = ingredients
        notifyPropertyChanged(BR.ingredients)
    }

    @SerializedName("strIngredient1")
    @Expose
    @ColumnInfo(name = "str_ingredient1")
    private var strIngredient1: String? = null

    @SerializedName("strIngredient2")
    @Expose
    @ColumnInfo(name = "str_ingredient2")
    private var strIngredient2: String? = null

    @SerializedName("strIngredient3")
    @Expose
    @ColumnInfo(name = "str_ingredient3")
    private var strIngredient3: String? = null

    @SerializedName("strIngredient4")
    @Expose
    @ColumnInfo(name = "str_ingredient4")
    private var strIngredient4: String? = null

    @SerializedName("strIngredient5")
    @Expose
    @ColumnInfo(name = "str_ingredient5")
    private var strIngredient5: String? = null

    @SerializedName("strIngredient6")
    @Expose
    @ColumnInfo(name = "str_ingredient6")
    private var strIngredient6: String? = null

    @SerializedName("strIngredient7")
    @Expose
    @ColumnInfo(name = "str_ingredient7")
    private var strIngredient7: String? = null

    @SerializedName("strIngredient8")
    @Expose
    @ColumnInfo(name = "str_ingredient8")
    private var strIngredient8: String? = null

    @SerializedName("strIngredient9")
    @Expose
    @ColumnInfo(name = "str_ingredient9")
    private var strIngredient9: String? = null

    @SerializedName("strIngredient10")
    @Expose
    @ColumnInfo(name = "str_ingredient10")
    private var strIngredient10: String? = null

    @SerializedName("strIngredient11")
    @Expose
    @ColumnInfo(name = "str_ingredient11")
    private var strIngredient11: String? = null

    @SerializedName("strIngredient12")
    @Expose
    @ColumnInfo(name = "str_ingredient12")
    private var strIngredient12: String? = null

    @SerializedName("strIngredient13")
    @Expose
    @ColumnInfo(name = "str_ingredient13")
    private var strIngredient13: String? = null

    @SerializedName("strIngredient14")
    @Expose
    @ColumnInfo(name = "str_ingredient14")
    private var strIngredient14: String? = null

    @SerializedName("strIngredient15")
    @Expose
    @ColumnInfo(name = "str_ingredient15")
    private var strIngredient15: String? = null

    @SerializedName("strMeasure1")
    @Expose
    @ColumnInfo(name = "str_measure1")
    private var strMeasure1: String? = null

    @SerializedName("strMeasure2")
    @Expose
    @ColumnInfo(name = "str_measure2")
    private var strMeasure2: String? = null

    @SerializedName("strMeasure3")
    @Expose
    @ColumnInfo(name = "str_measure3")
    private var strMeasure3: String? = null

    @SerializedName("strMeasure4")
    @Expose
    @ColumnInfo(name = "str_measure4")
    private var strMeasure4: String? = null

    @SerializedName("strMeasure5")
    @Expose
    @ColumnInfo(name = "str_measure5")
    private var strMeasure5: String? = null

    @SerializedName("strMeasure6")
    @Expose
    @ColumnInfo(name = "str_measure6")
    private var strMeasure6: String? = null

    @SerializedName("strMeasure7")
    @Expose
    @ColumnInfo(name = "str_measure7")
    private var strMeasure7: String? = null

    @SerializedName("strMeasure8")
    @Expose
    @ColumnInfo(name = "str_measure8")
    private var strMeasure8: String? = null

    @SerializedName("strMeasure9")
    @Expose
    @ColumnInfo(name = "str_measure9")
    private var strMeasure9: String? = null

    @SerializedName("strMeasure10")
    @Expose
    @ColumnInfo(name = "str_measure10")
    private var strMeasure10: String? = null

    @SerializedName("strMeasure11")
    @Expose
    @ColumnInfo(name = "str_measure11")
    private var strMeasure11: String? = null

    @SerializedName("strMeasure12")
    @Expose
    @ColumnInfo(name = "str_measure12")
    private var strMeasure12: String? = null

    @SerializedName("strMeasure13")
    @Expose
    @ColumnInfo(name = "str_measure13")
    private var strMeasure13: String? = null

    @SerializedName("strMeasure14")
    @Expose
    @ColumnInfo(name = "str_measure14")
    private var strMeasure14: String? = null

    @SerializedName("strMeasure15")
    @Expose
    @ColumnInfo(name = "str_measure15")
    private var strMeasure15: String? = null

    @SerializedName("strCreativeCommonsConfirmed")
    @Expose
    @Ignore
    private var strCreativeCommonsConfirmed: String? = null

    @SerializedName("dateModified")
    @Expose
    @Ignore
    private var dateModified: String? = null

    constructor(`in`: Parcel) {

        idDrink =
            `in`.readValue(String::class.java.classLoader) as Long?
        strDrink =
            `in`.readValue(String::class.java.classLoader) as String?
        strDrinkAlternate = `in`.readValue(Any::class.java.classLoader)
        strDrinkES = `in`.readValue(Any::class.java.classLoader)
        strDrinkDE = `in`.readValue(Any::class.java.classLoader)
        strDrinkFR = `in`.readValue(Any::class.java.classLoader)
        strDrinkZHHANS = `in`.readValue(Any::class.java.classLoader)
        strDrinkZHHANT = `in`.readValue(Any::class.java.classLoader)
        strTags = `in`.readValue(Any::class.java.classLoader)
        strVideo = `in`.readValue(Any::class.java.classLoader)
        strCategory =
            `in`.readValue(String::class.java.classLoader) as String?
        strIBA = `in`.readValue(Any::class.java.classLoader)
        strAlcoholic =
            `in`.readValue(String::class.java.classLoader) as String?
        strGlass =
            `in`.readValue(String::class.java.classLoader) as String?
        strInstructions =
            `in`.readValue(String::class.java.classLoader) as String?
        strInstructionsES = `in`.readValue(Any::class.java.classLoader)
        strInstructionsDE =
            `in`.readValue(String::class.java.classLoader) as String?
        strInstructionsFR = `in`.readValue(Any::class.java.classLoader)
        strInstructionsZHHANS = `in`.readValue(Any::class.java.classLoader)
        strInstructionsZHHANT = `in`.readValue(Any::class.java.classLoader)
        strDrinkThumb =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient1 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient2 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient3 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient4 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient5 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient6 =
            `in`.readValue(String::class.java.classLoader) as String?
        strIngredient7 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient8 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient9 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient10 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient11 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient12 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient13 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient14 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strIngredient15 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure1 =
            `in`.readValue(String::class.java.classLoader) as String?
        strMeasure2 =
            `in`.readValue(String::class.java.classLoader) as String?
        strMeasure3 =
            `in`.readValue(String::class.java.classLoader) as String?
        strMeasure4 =
            `in`.readValue(String::class.java.classLoader) as String?
        strMeasure5 =
            `in`.readValue(String::class.java.classLoader) as String?
        strMeasure6 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure7 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure8 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure9 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure10 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure11 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure12 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure13 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure14 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strMeasure15 =
            `in`.readValue(Any::class.java.classLoader) as String?
        strCreativeCommonsConfirmed =
            `in`.readValue(String::class.java.classLoader) as String?
        dateModified =
            `in`.readValue(String::class.java.classLoader) as String?
    }

    constructor()

    @Bindable
    fun getIdDrink(): Long? {
        return idDrink
    }

    fun setIdDrink(idDrink: Long?) {
        this.idDrink = idDrink
        notifyPropertyChanged(BR.idDrink)
    }

    @Bindable
    fun getStrDrink(): String? {
        return strDrink
    }

    fun setStrDrink(strDrink: String?) {
        this.strDrink = strDrink
        notifyPropertyChanged(BR.strDrink)
    }

    @Bindable
    fun getStrDrinkAlternate(): Any? {
        return strDrinkAlternate
    }

    fun setStrDrinkAlternate(strDrinkAlternate: Any?) {
        this.strDrinkAlternate = strDrinkAlternate
        notifyPropertyChanged(BR.strDrinkAlternate)
    }

    @Bindable
    fun isFavorite(): Boolean {
        return isFavorite
    }

    fun setIsFavorite(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        notifyPropertyChanged(BR.favorite)
    }

    @Bindable
    fun getStrDrinkES(): Any? {
        return strDrinkES
    }

    fun setStrDrinkES(strDrinkES: Any?) {
        this.strDrinkES = strDrinkES
        notifyPropertyChanged(BR.strDrinkES)
    }

    @Bindable
    fun getStrDrinkDE(): Any? {
        return strDrinkDE
    }

    fun setStrDrinkDE(strDrinkDE: Any?) {
        this.strDrinkDE = strDrinkDE
        notifyPropertyChanged(BR.strDrinkDE)
    }

    @Bindable
    fun getStrDrinkFR(): Any? {
        return strDrinkFR
    }

    fun setStrDrinkFR(strDrinkFR: Any?) {
        this.strDrinkFR = strDrinkFR
        notifyPropertyChanged(BR.strDrinkFR)
    }

    @Bindable
    fun getStrDrinkZHHANS(): Any? {
        return strDrinkZHHANS
    }

    fun setStrDrinkZHHANS(strDrinkZHHANS: Any?) {
        this.strDrinkZHHANS = strDrinkZHHANS
        notifyPropertyChanged(BR.strDrinkZHHANS)
    }

    @Bindable
    fun getStrDrinkZHHANT(): Any? {
        return strDrinkZHHANT
    }

    fun setStrDrinkZHHANT(strDrinkZHHANT: Any?) {
        this.strDrinkZHHANT = strDrinkZHHANT
        notifyPropertyChanged(BR.strDrinkZHHANT)
    }

    @Bindable
    fun getStrTags(): Any? {
        return strTags
    }

    fun setStrTags(strTags: Any?) {
        this.strTags = strTags
        notifyPropertyChanged(BR.strTags)
    }

    @Bindable
    fun getStrVideo(): Any? {
        return strVideo
    }

    fun setStrVideo(strVideo: Any?) {
        this.strVideo = strVideo
        notifyPropertyChanged(BR.strVideo)
    }

    @Bindable
    fun getStrCategory(): String? {
        return strCategory
    }

    fun setStrCategory(strCategory: String?) {
        this.strCategory = strCategory
        notifyPropertyChanged(BR.strCategory)
    }

    @Bindable
    fun getStrIBA(): Any? {
        return strIBA
    }

    fun setStrIBA(strIBA: Any?) {
        this.strIBA = strIBA
        notifyPropertyChanged(BR.strIBA)
    }

    @Bindable
    fun getStrAlcoholic(): String? {
        return strAlcoholic
    }

    fun setStrAlcoholic(strAlcoholic: String?) {
        this.strAlcoholic = strAlcoholic
        notifyPropertyChanged(BR.strAlcoholic)
    }

    @Bindable
    fun getStrGlass(): String? {
        return strGlass
    }

    fun setStrGlass(strGlass: String?) {
        this.strGlass = strGlass
        notifyPropertyChanged(BR.strGlass)
    }

    @Bindable
    fun getStrInstructions(): String? {
        return strInstructions
    }

    fun setStrInstructions(strInstructions: String?) {
        this.strInstructions = strInstructions
        notifyPropertyChanged(BR.strInstructions)
    }

    @Bindable
    fun getStrInstructionsES(): Any? {
        return strInstructionsES
    }

    fun setStrInstructionsES(strInstructionsES: Any?) {
        this.strInstructionsES = strInstructionsES
        notifyPropertyChanged(BR.strInstructionsES)
    }

    @Bindable
    fun getStrInstructionsDE(): String? {
        return strInstructionsDE
    }

    fun setStrInstructionsDE(strInstructionsDE: String?) {
        this.strInstructionsDE = strInstructionsDE
        notifyPropertyChanged(BR.strInstructionsDE)
    }

    @Bindable
    fun getStrInstructionsFR(): Any? {
        return strInstructionsFR
    }

    fun setStrInstructionsFR(strInstructionsFR: Any?) {
        this.strInstructionsFR = strInstructionsFR
        notifyPropertyChanged(BR.strInstructionsFR)
    }

    @Bindable
    fun getStrInstructionsZHHANS(): Any? {
        return strInstructionsZHHANS
    }

    fun setStrInstructionsZHHANS(strInstructionsZHHANS: Any?) {
        this.strInstructionsZHHANS = strInstructionsZHHANS
        notifyPropertyChanged(BR.strInstructionsZHHANS)
    }

    @Bindable
    fun getStrInstructionsZHHANT(): Any? {
        return strInstructionsZHHANT
    }

    fun setStrInstructionsZHHANT(strInstructionsZHHANT: Any?) {
        this.strInstructionsZHHANT = strInstructionsZHHANT
        notifyPropertyChanged(BR.strInstructionsZHHANT)
    }

    @Bindable
    fun getStrDrinkThumb(): String? {
        return strDrinkThumb
    }

    fun setStrDrinkThumb(strDrinkThumb: String?) {
        this.strDrinkThumb = strDrinkThumb
        notifyPropertyChanged(BR.strDrinkThumb)
    }

    @Bindable
    fun getStrIngredient1(): String? {
        return strIngredient1
    }

    fun setStrIngredient1(strIngredient1: String?) {
        this.strIngredient1 = strIngredient1
        notifyPropertyChanged(BR.strIngredient1)
    }

    @Bindable
    fun getStrIngredient2(): String? {
        return strIngredient2
    }

    fun setStrIngredient2(strIngredient2: String?) {
        this.strIngredient2 = strIngredient2
        notifyPropertyChanged(BR.strIngredient2)
    }

    @Bindable
    fun getStrIngredient3(): String? {
        return strIngredient3
    }

    fun setStrIngredient3(strIngredient3: String?) {
        this.strIngredient3 = strIngredient3
        notifyPropertyChanged(BR.strIngredient3)
    }

    @Bindable
    fun getStrIngredient4(): String? {
        return strIngredient4
    }

    fun setStrIngredient4(strIngredient4: String?) {
        this.strIngredient4 = strIngredient4
        notifyPropertyChanged(BR.strIngredient4)
    }

    @Bindable
    fun getStrIngredient5(): String? {
        return strIngredient5
    }

    fun setStrIngredient5(strIngredient5: String?) {
        this.strIngredient5 = strIngredient5
        notifyPropertyChanged(BR.strIngredient5)
    }

    @Bindable
    fun getStrIngredient6(): String? {
        return strIngredient6
    }

    fun setStrIngredient6(strIngredient6: String?) {
        this.strIngredient6 = strIngredient6
        notifyPropertyChanged(BR.strIngredient6)
    }

    @Bindable
    fun getStrIngredient7(): String? {
        return strIngredient7
    }

    fun setStrIngredient7(strIngredient7: String?) {
        this.strIngredient7 = strIngredient7
        notifyPropertyChanged(BR.strIngredient7)
    }

    @Bindable
    fun getStrIngredient8(): String? {
        return strIngredient8
    }

    fun setStrIngredient8(strIngredient8: String?) {
        this.strIngredient8 = strIngredient8
        notifyPropertyChanged(BR.strIngredient8)
    }

    @Bindable
    fun getStrIngredient9(): String? {
        return strIngredient9
    }

    fun setStrIngredient9(strIngredient9: String?) {
        this.strIngredient9 = strIngredient9
        notifyPropertyChanged(BR.strIngredient9)
    }

    @Bindable
    fun getStrIngredient10(): String? {
        return strIngredient10
    }

    fun setStrIngredient10(strIngredient10: String?) {
        this.strIngredient10 = strIngredient10
        notifyPropertyChanged(BR.strIngredient10)
    }

    @Bindable
    fun getStrIngredient11(): String? {
        return strIngredient11
    }

    fun setStrIngredient11(strIngredient11: String?) {
        this.strIngredient11 = strIngredient11
        notifyPropertyChanged(BR.strIngredient11)
    }

    @Bindable
    fun getStrIngredient12(): String? {
        return strIngredient12
    }

    fun setStrIngredient12(strIngredient12: String?) {
        this.strIngredient12 = strIngredient12
        notifyPropertyChanged(BR.strIngredient12)
    }

    @Bindable
    fun getStrIngredient13(): String? {
        return strIngredient13
    }

    fun setStrIngredient13(strIngredient13: String?) {
        this.strIngredient13 = strIngredient13
        notifyPropertyChanged(BR.strIngredient13)
    }

    @Bindable
    fun getStrIngredient14(): String? {
        return strIngredient14
    }

    fun setStrIngredient14(strIngredient14: String?) {
        this.strIngredient14 = strIngredient14
        notifyPropertyChanged(BR.strIngredient14)
    }

    @Bindable
    fun getStrIngredient15(): String? {
        return strIngredient15
    }

    fun setStrIngredient15(strIngredient15: String?) {
        this.strIngredient15 = strIngredient15
        notifyPropertyChanged(BR.strIngredient15)
    }

    @Bindable
    fun getStrMeasure1(): String? {
        return strMeasure1
    }

    fun setStrMeasure1(strMeasure1: String?) {
        this.strMeasure1 = strMeasure1
        notifyPropertyChanged(BR.strMeasure1)
    }

    @Bindable
    fun getStrMeasure2(): String? {
        return strMeasure2
    }

    fun setStrMeasure2(strMeasure2: String?) {
        this.strMeasure2 = strMeasure2
        notifyPropertyChanged(BR.strMeasure2)
    }

    @Bindable
    fun getStrMeasure3(): String? {
        return strMeasure3
    }

    fun setStrMeasure3(strMeasure3: String?) {
        this.strMeasure3 = strMeasure3
        notifyPropertyChanged(BR.strMeasure3)
    }

    @Bindable
    fun getStrMeasure4(): String? {
        return strMeasure4
    }

    fun setStrMeasure4(strMeasure4: String?) {
        this.strMeasure4 = strMeasure4
        notifyPropertyChanged(BR.strMeasure4)
    }

    @Bindable
    fun getStrMeasure5(): String? {
        return strMeasure5
    }

    fun setStrMeasure5(strMeasure5: String?) {
        this.strMeasure5 = strMeasure5
        notifyPropertyChanged(BR.strMeasure5)
    }

    @Bindable
    fun getStrMeasure6(): String? {
        return strMeasure6
    }

    fun setStrMeasure6(strMeasure6: String?) {
        this.strMeasure6 = strMeasure6
        notifyPropertyChanged(BR.strMeasure6)
    }

    @Bindable
    fun getStrMeasure7(): String? {
        return strMeasure7
    }

    fun setStrMeasure7(strMeasure7: String?) {
        this.strMeasure7 = strMeasure7
        notifyPropertyChanged(BR.strMeasure7)
    }

    @Bindable
    fun getStrMeasure8(): String? {
        return strMeasure8
    }

    fun setStrMeasure8(strMeasure8: String?) {
        this.strMeasure8 = strMeasure8
        notifyPropertyChanged(BR.strMeasure8)
    }

    @Bindable
    fun getStrMeasure9(): String? {
        return strMeasure9
    }

    fun setStrMeasure9(strMeasure9: String?) {
        this.strMeasure9 = strMeasure9
        notifyPropertyChanged(BR.strMeasure9)
    }

    @Bindable
    fun getStrMeasure10(): String? {
        return strMeasure10
    }

    fun setStrMeasure10(strMeasure10: String?) {
        this.strMeasure10 = strMeasure10
        notifyPropertyChanged(BR.strMeasure10)
    }

    @Bindable
    fun getStrMeasure11(): String? {
        return strMeasure11
    }

    fun setStrMeasure11(strMeasure11: String?) {
        this.strMeasure11 = strMeasure11
        notifyPropertyChanged(BR.strMeasure11)
    }

    @Bindable
    fun getStrMeasure12(): String? {
        return strMeasure12
    }

    fun setStrMeasure12(strMeasure12: String?) {
        this.strMeasure12 = strMeasure12
        notifyPropertyChanged(BR.strMeasure12)
    }

    @Bindable
    fun getStrMeasure13(): String? {
        return strMeasure13
    }

    fun setStrMeasure13(strMeasure13: String?) {
        this.strMeasure13 = strMeasure13
        notifyPropertyChanged(BR.strMeasure13)
    }

    @Bindable
    fun getStrMeasure14(): String? {
        return strMeasure14
    }

    fun setStrMeasure14(strMeasure14: String?) {
        this.strMeasure14 = strMeasure14
        notifyPropertyChanged(BR.strMeasure14)
    }

    @Bindable
    fun getStrMeasure15(): String? {
        return strMeasure15
    }

    fun setStrMeasure15(strMeasure15: String?) {
        this.strMeasure15 = strMeasure15
        notifyPropertyChanged(BR.strMeasure15)
    }

    @Bindable
    fun getStrCreativeCommonsConfirmed(): String? {
        return strCreativeCommonsConfirmed
    }

    fun setStrCreativeCommonsConfirmed(strCreativeCommonsConfirmed: String?) {
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed
        notifyPropertyChanged(BR.strCreativeCommonsConfirmed)
    }

    @Bindable
    fun getDateModified(): String? {
        return dateModified
    }

    fun setDateModified(dateModified: String?) {
        this.dateModified = dateModified
        notifyPropertyChanged(BR.dateModified)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(idDrink)
        dest.writeValue(strDrink)
        dest.writeValue(strDrinkAlternate)
        dest.writeValue(strDrinkES)
        dest.writeValue(strDrinkDE)
        dest.writeValue(strDrinkFR)
        dest.writeValue(strDrinkZHHANS)
        dest.writeValue(strDrinkZHHANT)
        dest.writeValue(strTags)
        dest.writeValue(strVideo)
        dest.writeValue(strCategory)
        dest.writeValue(strIBA)
        dest.writeValue(strAlcoholic)
        dest.writeValue(strGlass)
        dest.writeValue(strInstructions)
        dest.writeValue(strInstructionsES)
        dest.writeValue(strInstructionsDE)
        dest.writeValue(strInstructionsFR)
        dest.writeValue(strInstructionsZHHANS)
        dest.writeValue(strInstructionsZHHANT)
        dest.writeValue(strDrinkThumb)
        dest.writeValue(strIngredient1)
        dest.writeValue(strIngredient2)
        dest.writeValue(strIngredient3)
        dest.writeValue(strIngredient4)
        dest.writeValue(strIngredient5)
        dest.writeValue(strIngredient6)
        dest.writeValue(strIngredient7)
        dest.writeValue(strIngredient8)
        dest.writeValue(strIngredient9)
        dest.writeValue(strIngredient10)
        dest.writeValue(strIngredient11)
        dest.writeValue(strIngredient12)
        dest.writeValue(strIngredient13)
        dest.writeValue(strIngredient14)
        dest.writeValue(strIngredient15)
        dest.writeValue(strMeasure1)
        dest.writeValue(strMeasure2)
        dest.writeValue(strMeasure3)
        dest.writeValue(strMeasure4)
        dest.writeValue(strMeasure5)
        dest.writeValue(strMeasure6)
        dest.writeValue(strMeasure7)
        dest.writeValue(strMeasure8)
        dest.writeValue(strMeasure9)
        dest.writeValue(strMeasure10)
        dest.writeValue(strMeasure11)
        dest.writeValue(strMeasure12)
        dest.writeValue(strMeasure13)
        dest.writeValue(strMeasure14)
        dest.writeValue(strMeasure15)
        dest.writeValue(strCreativeCommonsConfirmed)
        dest.writeValue(dateModified)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Drink> {
        override fun createFromParcel(`in`: Parcel): Drink {
            return Drink(`in`)
        }

        override fun newArray(size: Int): Array<Drink?> {
            return arrayOfNulls(size)
        }
    }

    object CurrencyBindingAdapter {
        @BindingAdapter("strDrinkThumb")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.default_icon)
                .into(imageView)
        }
    }

}