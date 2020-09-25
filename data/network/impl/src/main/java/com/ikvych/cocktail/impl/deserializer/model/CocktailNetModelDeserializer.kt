package com.ikvych.cocktail.impl.deserializer.model

import com.google.gson.*
import com.ikvych.cocktail.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.network.model.cocktail.LocalizedStringNetModel
import com.ikvych.cocktail.impl.extension.getIfHasMember
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap

class CocktailNetModelDeserializer : JsonDeserializer<CocktailNetModel> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): CocktailNetModel {
        return json!!.asJsonObject!!.let { jsonObject ->
            CocktailNetModel(
                id = jsonObject.get("idDrink").asLong,
                names = LocalizedStringNetModel(
                    defaultName = jsonObject.get("strDrink").asString,
                    defaultAlternate = jsonObject.getIfHasMember("strDrinkAlternate")?.asString,
                    de = jsonObject.getIfHasMember("strDrinkDE")?.asString,
                    es = jsonObject.getIfHasMember("strDrinkES")?.asString,
                    fr = jsonObject.getIfHasMember("strDrinkFR")?.asString,
                    zhHans = jsonObject.getIfHasMember("strDrinkZH-HANS")?.asString,
                    zhHant = jsonObject.getIfHasMember("strDrinkZH-HANT")?.asString
                ),
                alcoholType = jsonObject.get("strAlcoholic").asString,
                category = jsonObject.get("strCategory").asString,
                glass = jsonObject.get("strGlass").asString,
                image = jsonObject.get("strDrinkThumb").asString,
                instructions = LocalizedStringNetModel(
                    defaultName = jsonObject.get("strInstructions").asString,
                    de = jsonObject.getIfHasMember("strInstructionsDE")?.asString,
                    es = jsonObject.getIfHasMember("strInstructionsES")?.asString,
                    fr = jsonObject.getIfHasMember("strInstructionsFR")?.asString,
                    zhHans = jsonObject.getIfHasMember("strInstructionsZH-HANS")?.asString,
                    zhHant = jsonObject.getIfHasMember("strInstructionsZH-HANT")?.asString
                ),
                ingredientsWithMeasures = createIngredientWithMeasure(jsonObject),
                date = jsonObject.get("dateModified").let { jsonElement ->
                    context.deserialize<Date>(jsonElement, Date::class.java)
                } ?: Date()
            )
        }
    }

    private fun createIngredientWithMeasure(cocktailJsonObject: JsonObject): Map<String, String> {
        val ingredientsWithMeasures = HashMap<String, String>()
        for (i in 1..15) {
            val ingredient =
                cocktailJsonObject.getIfHasMember("strIngredient${i}")?.asString ?: continue
            val measure =
                cocktailJsonObject.getIfHasMember("strMeasure${i}")?.asString ?: ""
            ingredientsWithMeasures[ingredient] = measure
        }
        return ingredientsWithMeasures
    }
}
