package com.jerry.mvirxjava.data.model

class ProductListResponse(
    val products : List<Product> = emptyList(),
    val title : String?,
    val product_count : Int?,
)