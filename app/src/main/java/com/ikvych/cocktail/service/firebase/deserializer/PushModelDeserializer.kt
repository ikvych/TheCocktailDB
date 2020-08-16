package com.ikvych.cocktail.service.firebase.deserializer

import com.google.gson.*
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.data.network.model.cocktail.LocalizedStringNetModel
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.presentation.model.notification.NotificationType
import com.ikvych.cocktail.util.EMPTY_STRING
import io.devlight.data.network.impl.extension.getIfHasMember
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PushModelDeserializer : JsonDeserializer<NotificationModel> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): NotificationModel {
        return json!!.asJsonObject!!.let { jsonObject ->
            NotificationModel(
                id = jsonObject.getIfHasMember("id")?.asLong ?: 1L,
                title = jsonObject.getIfHasMember("title")?.asString ?: "",
                body = jsonObject.getIfHasMember("body")?.asString ?: "",
                type = NotificationType.values()
                    .firstOrNull { it.key == jsonObject.getIfHasMember("type")?.asString }
                    ?: NotificationType.NOTIFICATION_TYPE_UNDEFINED,
                image = jsonObject.getIfHasMember("image")?.asString,
                cocktailId = jsonObject.getIfHasMember("cocktail_id")?.asLong
            )
        }
    }
}
