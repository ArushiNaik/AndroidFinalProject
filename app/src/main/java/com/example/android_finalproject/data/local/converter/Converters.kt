package com.example.android_finalproject.data.local.converter

import androidx.room.TypeConverter
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.model.OrderStatus
import java.util.UUID

class Converters {
    @TypeConverter fun fromUuid(value: UUID): String = value.toString()
    @TypeConverter fun toUuid(value: String): UUID = UUID.fromString(value)
    @TypeConverter fun fromOrderMode(mode: OrderMode): String = mode.name
    @TypeConverter fun toOrderMode(value: String): OrderMode = OrderMode.valueOf(value)
    @TypeConverter fun fromOrderStatus(status: OrderStatus): String = status.name
    @TypeConverter fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)
}
