package com.example.android_finalproject.repository

import com.example.android_finalproject.data.local.dao.CommentDao
import com.example.android_finalproject.data.local.dao.FoodDao
import com.example.android_finalproject.data.local.dao.OrderDao
import com.example.android_finalproject.data.local.dao.RestaurantDao
import com.example.android_finalproject.data.local.entity.CommentEntity
import com.example.android_finalproject.data.local.entity.FoodItemEntity
import com.example.android_finalproject.data.local.entity.OrderEntity
import com.example.android_finalproject.data.local.entity.RestaurantEntity
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.model.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val foodDao: FoodDao,
    private val orderDao: OrderDao,
    private val commentDao: CommentDao,
) {
    fun dashboard(search: String): Flow<DashboardData> = combine(
        restaurantDao.observeAll(),
        foodDao.observeAll(),
        orderDao.observeAll(),
        commentDao.observeAll(),
    ) { restaurants, foods, orders, comments ->
        DashboardData(
            restaurants = restaurants.filter { it.name.contains(search, ignoreCase = true) },
            foodItems = foods.filter { it.name.contains(search, ignoreCase = true) || it.category.contains(search, ignoreCase = true) },
            orders = orders,
            comments = comments,
        )
    }

    suspend fun addComment(restaurantId: UUID, author: String, message: String) {
        commentDao.insert(CommentEntity(UUID.randomUUID(), restaurantId, author, message))
    }

    suspend fun placeOrder(foodItemId: UUID, mode: OrderMode) {
        orderDao.upsert(OrderEntity(UUID.randomUUID(), foodItemId, mode, OrderStatus.PENDING))
    }

    suspend fun advanceOrderStatus(orderId: UUID) {
        val order = orderDao.findById(orderId) ?: return
        val next = when (order.status) {
            OrderStatus.PENDING -> OrderStatus.PREPARING
            OrderStatus.PREPARING -> OrderStatus.ON_THE_WAY
            OrderStatus.ON_THE_WAY -> OrderStatus.COMPLETE
            OrderStatus.COMPLETE -> OrderStatus.COMPLETE
            OrderStatus.CANCELLED -> OrderStatus.CANCELLED
        }
        orderDao.update(order.copy(status = next))
    }

    suspend fun cancelOrder(orderId: UUID) {
        orderDao.findById(orderId)?.let { orderDao.delete(it) }
    }

    suspend fun seedIfEmpty() {
        if (restaurantDao.observeAll().first().isNotEmpty()) return
        val restaurants = listOf(
            RestaurantEntity(UUID.randomUUID(), "Subway", "08:00", "20:00"),
            RestaurantEntity(UUID.randomUUID(), "Tim Hortons", "07:00", "22:00"),
            RestaurantEntity(UUID.randomUUID(), "McDonald's", "09:00", "23:00"),
            RestaurantEntity(UUID.randomUUID(), "A&W", "10:00", "21:00"),
            RestaurantEntity(UUID.randomUUID(), "Pizza Pizza", "10:00", "23:00"),
            RestaurantEntity(UUID.randomUUID(), "Campus Cafe", "07:30", "18:00"),
            RestaurantEntity(UUID.randomUUID(), "Freshii", "09:00", "20:00"),
            RestaurantEntity(UUID.randomUUID(), "Sushi Go", "11:00", "22:00"),
            RestaurantEntity(UUID.randomUUID(), "Pita Pit", "10:00", "21:00"),
            RestaurantEntity(UUID.randomUUID(), "Burger King", "09:00", "22:00"),
        )
        restaurantDao.insertAll(restaurants)
        val foods = restaurants.flatMap { r ->
            listOf(
                FoodItemEntity(UUID.randomUUID(), r.id, "Signature Combo - ${r.name}", "Popular", 11.99),
                FoodItemEntity(UUID.randomUUID(), r.id, "Chicken Bowl - ${r.name}", "Healthy", 9.49),
                FoodItemEntity(UUID.randomUUID(), r.id, "Student Deal - ${r.name}", "Deals", 7.99),
            )
        }
        foodDao.insertAll(foods)
        commentDao.insert(CommentEntity(UUID.randomUUID(), restaurants.first().id, "Student A", "Fast and affordable."))
    }
}

data class DashboardData(
    val restaurants: List<RestaurantEntity>,
    val foodItems: List<FoodItemEntity>,
    val orders: List<OrderEntity>,
    val comments: List<CommentEntity>,
)
