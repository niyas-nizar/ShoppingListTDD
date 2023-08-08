package com.niyas.shoppinglisttdd.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niyas.shoppinglisttdd.data.local.ShoppingItem
import com.niyas.shoppinglisttdd.data.remote.imageresponse.ImageResponse
import com.niyas.shoppinglisttdd.other.Constant
import com.niyas.shoppinglisttdd.other.Event
import com.niyas.shoppinglisttdd.other.Resource
import com.niyas.shoppinglisttdd.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemsStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemsStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemsStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amount: String, price: String) {
        if (name.isEmpty() || amount.isEmpty() || price.isEmpty()) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "Fields must not be empty",
                        data = null
                    )
                )
            )
            return
        }
        if (name.length > Constant.MAX_NAME_LENGTH) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item must not exceed ${Constant.MAX_NAME_LENGTH} characters",
                        data = null
                    )
                )
            )
            return
        }
        if (price.length > Constant.MAX_PRICE_LENGTH) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "The price of the item must not exceed ${Constant.MAX_NAME_LENGTH} digits",
                        data = null
                    )
                )
            )
            return
        }
        val amountInt = try {
            amount.toInt()
        } catch (e: Exception) {
            _insertShoppingItemsStatus.postValue(
                Event(
                    Resource.error(
                        "Please enter a valid amount",
                        data = null
                    )
                )
            )
            return
        }
        val shoppingItem = ShoppingItem(name, amount, price.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItem(shoppingItem)
        setCurrentImageUrl("")
        _insertShoppingItemsStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImage(searchQuery: String) {
        if (searchQuery.isEmpty())
            return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchImage(searchQuery)
            _images.value = Event(response)
        }
    }

}