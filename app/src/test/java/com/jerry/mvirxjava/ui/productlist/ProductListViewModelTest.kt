package com.jerry.mvirxjava.ui.productlist

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jerry.mvirxjava.base.ViewState
import com.jerry.mvirxjava.data.api.ProductApi
import com.jerry.mvirxjava.data.repository.NetworkRepository
import com.jerry.mvirxjava.ui.productlist.intent.ProductListIntent
import getHoodieListResponse
import getSneakerListResponse
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {


    private val mockProductApi: ProductApi = mock()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.onComputationScheduler(Schedulers.trampoline())
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun test_with_success(){
        val sneakers = getSneakerListResponse()
        val hoodies = getHoodieListResponse()

        val singleSneaker =  Observable.just(sneakers)
        val singleHoodie =  Observable.just(hoodies)
        whenever(mockProductApi.getHoodieList()).thenReturn(singleHoodie)
        whenever(mockProductApi.getSneakerList()).thenReturn(singleSneaker)

        val mockNetworkRepository = NetworkRepository(mockProductApi)
        val mockProductListViewHolder = ProductListViewModel(mockNetworkRepository)

        mockProductListViewHolder.sendIntent(ProductListIntent.GetProductList)
        val viewState = mockProductListViewHolder.viewState.value

        when (viewState) {
            is ViewState.Success ->{
                // Ensure that hoodies are always displayed before sneakers.
                val expectedList = hoodies.products.plus(sneakers.products)

                val actualSize = viewState.data.size
                val expectedSize = expectedList.size
                Assert.assertEquals(expectedSize, actualSize)

                viewState.data.forEachIndexed { i, product->
                    val expectedName = expectedList.get(i).name
                    val actualName = product.name
                    Assert.assertEquals(expectedName, actualName)
                }
            }
        }
    }

}