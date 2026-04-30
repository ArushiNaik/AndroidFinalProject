package com.example.android_finalproject.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_finalproject.data.local.AppDatabase
import com.example.android_finalproject.data.local.dao.CartDao
import com.example.android_finalproject.data.local.dao.CommentDao
import com.example.android_finalproject.data.local.dao.FoodDao
import com.example.android_finalproject.data.local.dao.OrderDao
import com.example.android_finalproject.data.local.dao.OrderItemDao
import com.example.android_finalproject.data.local.dao.RestaurantDao
import com.example.android_finalproject.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "champlain.db")
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()

    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideRestaurantDao(db: AppDatabase): RestaurantDao = db.restaurantDao()
    @Provides fun provideFoodDao(db: AppDatabase): FoodDao = db.foodDao()
    @Provides fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()
    @Provides fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()
    @Provides fun provideOrderItemDao(db: AppDatabase): OrderItemDao = db.orderItemDao()
    @Provides fun provideCommentDao(db: AppDatabase): CommentDao = db.commentDao()
}
