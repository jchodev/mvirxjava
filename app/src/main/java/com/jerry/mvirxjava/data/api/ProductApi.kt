package com.jerry.mvirxjava.data.api

import com.jerry.mvirxjava.data.model.ProductListResponse
import io.reactivex.Flowable

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface ProductApi {

    @GET("/media/catalog/android_test_example.json")
        fun getHoodieList(): Observable<ProductListResponse>

    @GET("/media/catalog/example.json")
    fun getSneakerList(): Observable<ProductListResponse>

    @GET("/media/catalog/example.json")
    fun getSneakerListFlowableResponse(): Flowable<ProductListResponse>


}