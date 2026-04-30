package com.example.android_finalproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_finalproject.data.local.converter.Converters
import com.example.android_finalproject.data.local.dao.CommentDao
import com.example.android_finalproject.data.local.dao.FoodDao
import com.example.android_finalproject.data.local.dao.OrderDao
import com.example.android_finalproject.data.local.dao.RestaurantDao
import com.example.android_finalproject.data.local.entity.CommentEntity
import com.example.android_finalproject.data.local.entity.FoodItemEntity
import com.example.android_finalproject.data.local.entity.OrderEntity
import com.example.android_finalproject.data.local.entity.RestaurantEntity

@Database(entities = [RestaurantEntity::class, FoodItemEntity::class, OrderEntity::class, CommentEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun foodDao(): FoodDao
    abstract fun orderDao(): OrderDao
    abstract fun commentDao(): CommentDao
}
