package com.niyas.shoppinglisttdd.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.niyas.shoppinglisttdd.MainCoroutineRule
import com.niyas.shoppinglisttdd.getOrAwaitValue
import com.niyas.shoppinglisttdd.other.Constant
import com.niyas.shoppinglisttdd.other.Status
import com.niyas.shoppinglisttdd.repositories.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun insertShoppingItemWithEmptyField_returnsError() {
        viewModel.insertShoppingItem("", "", "5")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertShoppingItemWithTooLongName_returnsError() {
        val nameString = buildString {
            for (i in 1..Constant.MAX_NAME_LENGTH + 1)
                append(i.toString())
        }
        viewModel.insertShoppingItem(nameString, "", "5.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertShoppingItemWithTooLongPrice_returnsError() {
        val priceString = buildString {
            for (i in 1..Constant.MAX_PRICE_LENGTH + 1)
                append(i.toString())
        }
        viewModel.insertShoppingItem("", "", priceString)
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertShoppingItemWithTooHighAmount_returnsError() {
        viewModel.insertShoppingItem("", "12345678900987654321", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun insertShoppingItemWithValidInput_returnsSuccess() {
        viewModel.insertShoppingItem("szdae", "1234567", "3.0")
        val value = viewModel.insertShoppingItemsStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}