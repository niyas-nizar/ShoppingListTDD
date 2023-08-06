package com.niyas.shoppinglisttdd.data.remote.imageresponse

data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)
