package com.niyas.shoppinglisttdd.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    var name: String,
    var amount: String,
    var price: Float,
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
