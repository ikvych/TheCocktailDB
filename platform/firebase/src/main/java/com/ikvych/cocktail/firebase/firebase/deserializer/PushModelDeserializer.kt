package com.ikvych.cocktail.firebase.firebase.deserializer

import com.ikvych.cocktail.firebase.firebase.model.NotificationModel
import com.ikvych.cocktail.firebase.firebase.model.NotificationType
import java.lang.reflect.Type

/*class PushModelDeserializer : JsonDeserializer<NotificationModel> {

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
}*/
