package com.example.android_finalproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_finalproject.data.local.converter.Converters
import com.example.android_finalproject.data.local.dao.CartDao
import com.example.android_finalproject.data.local.dao.CommentDao
import com.example.android_finalproject.data.local.dao.FoodDao
import com.example.android_finalproject.data.local.dao.OrderDao
import com.example.android_finalproject.data.local.dao.OrderItemDao
import com.example.android_finalproject.data.local.dao.RestaurantDao
import com.example.android_finalproject.data.local.dao.UserDao
import com.example.android_finalproject.data.local.entity.CartItemEntity
import com.example.android_finalproject.data.local.entity.CommentEntity
import com.example.android_finalproject.data.local.entity.FoodItemEntity
import com.example.android_finalproject.data.local.entity.OrderEntity
import com.example.android_finalproject.data.local.entity.OrderItemEntity
import com.example.android_finalproject.data.local.entity.RestaurantEntity
import com.example.android_finalproject.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RestaurantEntity::class,
        FoodItemEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CommentEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun foodDao(): FoodDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun commentDao(): CommentDao
}
