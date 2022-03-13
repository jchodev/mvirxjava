package com.jerry.mvirxjava.ui.productlist.intent

sealed class ProductListIntent {
    object GetProductList : ProductListIntent()
}