package com.jerry.mvirxjava.repository

import com.jerry.mvirxjava.data.api.ProductApi
import com.jerry.mvirxjava.data.model.Product
import com.jerry.mvirxjava.data.model.ProductListResponse
import com.jerry.mvirxjava.data.repository.NetworkRepository
import getHoodieListResponse
import getSneakerListResponse
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import junit.framework.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NetworkRepositoryTest {

    private val mockProductApi: ProductApi = mock()

    @Test
    fun test_hoodie_list_success(){
        val singleHoodie =  Observable.just(getHoodieListResponse())

        whenever(mockProductApi.getHoodieList()).thenReturn(singleHoodie)

        val mockNetworkRepository = NetworkRepository(mockProductApi)
        val mockResponse = mockNetworkRepository.getHoodieList()

        val testObserver = TestObserver<ProductListResponse>()
        mockResponse.subscribe(testObserver)

        val mockProductListResponse = testObserver.values()[0]

        val actual = mockProductListResponse.products.size
        val expected = getHoodieListResponse().products.size

        Assert.assertEquals(actual, expected)

    }

    @Test
    fun test_sneaker_list_success(){
        val singleSneaker =  Observable.just(getSneakerListResponse())

        whenever(mockProductApi.getSneakerList()).thenReturn(singleSneaker)

        val mockNetworkRepository = NetworkRepository(mockProductApi)
        val mockResponse = mockNetworkRepository.getSneakerList()

        val testObserver = TestObserver<ProductListResponse>()
        mockResponse.subscribe(testObserver)

        val mockProductListResponse = testObserver.values()[0]

        val actual = mockProductListResponse.products.size
        val expected = getSneakerListResponse().products.size

        Assert.assertEquals(actual, expected)

    }
}