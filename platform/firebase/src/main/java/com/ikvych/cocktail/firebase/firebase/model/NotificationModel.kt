package com.ikvych.cocktail.firebase.firebase.model

import android.os.Parcel
import android.os.Parcelable

data class NotificationModel(
    val id: Long = 1L,
    val title: String? = "",
    val body: String? = "",
    val type: NotificationType = NotificationType.NOTIFICATION_TYPE_UNDEFINED,
    val image: String? = null,
    val cocktailId: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        NotificationType.valueOf(parcel.readString() ?: "undefined"),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(type.name)
        parcel.writeString(image)
        parcel.writeValue(cocktailId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationModel> {
        override fun createFromParcel(parcel: Parcel): NotificationModel {
            return NotificationModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<NotificationModel?> {
            return arrayOfNulls(size)
        }
    }

}