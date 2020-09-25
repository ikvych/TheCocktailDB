package com.ikvych.cocktail.prresentation.service.firebase.deserializer

import com.google.gson.*
import java.lang.reflect.Type

/*class PushModelDeserializer : JsonDeserializer<com.ikvych.cocktail.firebase.firebase.model.NotificationModel> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?, typeOfT: Type,
        context: JsonDeserializationContext
    ): com.ikvych.cocktail.firebase.firebase.model.NotificationModel {
        return json!!.asJsonObject!!.let { jsonObject ->
            com.ikvych.cocktail.firebase.firebase.model.NotificationModel(
                id = jsonObject.getIfHasMember("id")?.asLong ?: 1L,
                title = jsonObject.getIfHasMember("title")?.asString ?: "",
                body = jsonObject.getIfHasMember("body")?.asString ?: "",
                type = com.ikvych.cocktail.firebase.firebase.model.NotificationType.values()
                    .firstOrNull { it.key == jsonObject.getIfHasMember("type")?.asString }
                    ?: com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_UNDEFINED,
                image = jsonObject.getIfHasMember("image")?.asString,
                cocktailId = jsonObject.getIfHasMember("cocktail_id")?.asLong
            )
        }
    }
}*/
