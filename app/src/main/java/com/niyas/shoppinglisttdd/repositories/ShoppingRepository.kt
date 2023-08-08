package com.niyas.shoppinglisttdd.repositories

import androidx.lifecycle.LiveData
import com.niyas.shoppinglisttdd.data.local.ShoppingItem
import com.niyas.shoppinglisttdd.data.remote.imageresponse.ImageResponse
import com.niyas.shoppinglisttdd.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItem(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchImage(imageQuery: String): Resource<ImageResponse>
}