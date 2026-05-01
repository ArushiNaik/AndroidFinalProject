package com.example.android_finalproject.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android_finalproject.data.local.entity.CartItemEntity
import com.example.android_finalproject.data.local.entity.CommentEntity
import com.example.android_finalproject.data.local.entity.FoodItemEntity
import com.example.android_finalproject.data.local.entity.OrderEntity
import com.example.android_finalproject.data.local.entity.OrderItemEntity
import com.example.android_finalproject.data.local.entity.RestaurantEntity
import com.example.android_finalproject.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun observeAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)
}

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    fun observeAll(): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RestaurantEntity>)
}

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items")
    fun observeAll(): Flow<List<FoodItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FoodItemEntity>)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun observeByUser(userId: UUID): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearUserCart(userId: UUID)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders")
    fun observeAll(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: OrderEntity)

    @Update
    suspend fun update(item: OrderEntity)

    @Delete
    suspend fun delete(item: OrderEntity)

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun findById(id: UUID): OrderEntity?
}

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun observeByOrder(orderId: UUID): Flow<List<OrderItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: OrderItemEntity)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments")
    fun observeAll(): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CommentEntity)
}
