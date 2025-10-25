package com.effatheresoft.mindlesslyhiragana.data.local

import androidx.room.TypeConverter
import com.effatheresoft.mindlesslyhiragana.data.UserInteraction
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromUserInteractionList(value: List<UserInteraction>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toUserInteractionList(value: String): List<UserInteraction> {
        return Json.decodeFromString<List<UserInteraction>>(value)
    }
}
