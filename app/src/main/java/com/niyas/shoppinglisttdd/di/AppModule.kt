package com.niyas.shoppinglisttdd.di

import android.content.Context
import androidx.room.Room
import com.niyas.shoppinglisttdd.data.local.ShoppingDao
import com.niyas.shoppinglisttdd.data.local.ShoppingItemDatabase
import com.niyas.shoppinglisttdd.data.remote.PixabayApi
import com.niyas.shoppinglisttdd.other.Constant.BASE_URL
import com.niyas.shoppinglisttdd.other.Constant.DATABASE_NAME
import com.niyas.shoppinglisttdd.repositories.DefaultShoppingRepository
import com.niyas.shoppinglisttdd.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Singleton
    @Provides
    fun providesPixabayApi(): PixabayApi {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun providesDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixabayApi
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository
}