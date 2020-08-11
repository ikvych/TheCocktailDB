package com.ikvych.cocktail.data.network.impl.deserializer.model

import com.google.gson.*
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.data.network.model.cocktail.LocalizedStringNetModel
import com.ikvych.cocktail.util.EMPTY_STRING
import io.devlight.data.network.impl.extension.getIfHasMember
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CocktailNetModelDeserializer : JsonDeserializer<CocktailNetResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): CocktailNetResponse {
        return json!!.asJsonObject!!.let { jsonObject ->
            val cocktailNetResponse = CocktailNetResponse()
            val elements: ArrayList<CocktailNetModel> = arrayListOf()
            val jsonArray: JsonArray = jsonObject.getIfHasMember("drinks")?.asJsonArray ?: return cocktailNetResponse
            jsonArray.forEach {
                val cocktailJsonObject = it.asJsonObject
                elements.add(
                    CocktailNetModel(
                        id = cocktailJsonObject.get("idDrink").asLong,
                        names = LocalizedStringNetModel(
                            defaultName = cocktailJsonObject.get("strDrink").asString,
                            defaultAlternate = cocktailJsonObject.getIfHasMember("strDrinkAlternate")?.asString,
                            de = cocktailJsonObject.getIfHasMember("strDrinkDE")?.asString,
                            es = cocktailJsonObject.getIfHasMember("strDrinkES")?.asString,
                            fr = cocktailJsonObject.getIfHasMember("strDrinkFR")?.asString,
                            zhHans = cocktailJsonObject.getIfHasMember("strDrinkZH-HANS")?.asString,
                            zhHant = cocktailJsonObject.getIfHasMember("strDrinkZH-HANT")?.asString
                        ),
                        alcoholType = cocktailJsonObject.get("strAlcoholic").asString,
                        category = cocktailJsonObject.get("strCategory").asString,
                        glass = cocktailJsonObject.get("strGlass").asString,
                        image = cocktailJsonObject.get("strDrinkThumb").asString,
                        instructions = LocalizedStringNetModel(
                            defaultName = cocktailJsonObject.get("strInstructions").asString,
                            de = cocktailJsonObject.getIfHasMember("strInstructionsDE")?.asString,
                            es = cocktailJsonObject.getIfHasMember("strInstructionsES")?.asString,
                            fr = cocktailJsonObject.getIfHasMember("strInstructionsFR")?.asString,
                            zhHans = cocktailJsonObject.getIfHasMember("strInstructionsZH-HANS")?.asString,
                            zhHant = cocktailJsonObject.getIfHasMember("strInstructionsZH-HANT")?.asString
                        ),
                        ingredientsWithMeasures = createIngredientWithMeasure(cocktailJsonObject),
                        date = cocktailJsonObject.get("dateModified").let {jsonElement ->
                            context.deserialize<Date>(jsonElement, Date::class.java)
                        } ?: Date()
                    )
                )
            }

            cocktailNetResponse.cocktails = elements
            cocktailNetResponse
        }
    }

    private fun createIngredientWithMeasure(cocktailJsonObject: JsonObject): Map<String, String> {
        val ingredientsWithMeasures = HashMap<String, String>()
        for (i in 1..15) {
            val ingredient = cocktailJsonObject.getIfHasMember("strIngredient${i}")?.asString ?: continue
            val measure = cocktailJsonObject.getIfHasMember("strMeasure${i}")?.asString ?: EMPTY_STRING
            ingredientsWithMeasures[ingredient] = measure
        }
        return ingredientsWithMeasures
    }
}
