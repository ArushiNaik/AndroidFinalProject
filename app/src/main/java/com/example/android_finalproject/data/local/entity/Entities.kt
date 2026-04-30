package com.example.android_finalproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.model.OrderStatus
import java.time.LocalTime
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: UUID,
    val fullName: String,
    val email: String,
    val studentDiscountEnabled: Boolean,
)

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val opensAt: LocalTime,
    val closesAt: LocalTime,
)

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey val id: UUID,
    val restaurantId: UUID,
    val name: String,
    val category: String,
    val price: Double,
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: UUID,
    val userId: UUID,
    val foodItemId: UUID,
    val quantity: Int,
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: UUID,
    val foodItemId: UUID,
    val mode: OrderMode,
    val status: OrderStatus,
)

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey val id: UUID,
    val orderId: UUID,
    val foodItemId: UUID,
    val quantity: Int,
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: UUID,
    val restaurantId: UUID,
    val author: String,
    val message: String,
)
