package com.jerry.mvirxjava.data.repository

import com.jerry.mvirxjava.data.api.ProductApi
import com.jerry.mvirxjava.data.model.ProductListResponse
import io.reactivex.Observable
import javax.inject.Inject

class NetworkRepository @Inject constructor(
        private val productApi : ProductApi
) {

    fun getHoodieList() : Observable<ProductListResponse>{
        return productApi.getHoodieList()
    }

    fun getSneakerList(): Observable<ProductListResponse> {
        return  productApi.getSneakerList()
    }

}