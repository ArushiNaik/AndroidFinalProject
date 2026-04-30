package com.example.android_finalproject.model

import java.util.UUID

data class Restaurant(val id: UUID, val name: String, val opensAt: String, val closesAt: String)
data class FoodItem(val id: UUID, val restaurantId: UUID, val name: String, val category: String, val price: Double)
data class Order(val id: UUID, val foodItemId: UUID, val mode: OrderMode, val status: OrderStatus)
data class Comment(val id: UUID, val restaurantId: UUID, val author: String, val message: String)

enum class OrderMode { PICKUP, DELIVERY }
enum class OrderStatus { PENDING, PREPARING, ON_THE_WAY, COMPLETE, CANCELLED }
