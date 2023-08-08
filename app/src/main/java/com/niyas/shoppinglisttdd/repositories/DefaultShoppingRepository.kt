package com.niyas.shoppinglisttdd.repositories

import androidx.lifecycle.LiveData
import com.niyas.shoppinglisttdd.data.local.ShoppingDao
import com.niyas.shoppinglisttdd.data.local.ShoppingItem
import com.niyas.shoppinglisttdd.data.remote.PixabayApi
import com.niyas.shoppinglisttdd.data.remote.imageresponse.ImageResponse
import com.niyas.shoppinglisttdd.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayApi: PixabayApi
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayApi.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error", data = null)
            } else Resource.error("An unknown error", data = null)
        } catch (e: Exception) {
            Resource.error("Could not reach the server, try again later", null)
        }
    }
}