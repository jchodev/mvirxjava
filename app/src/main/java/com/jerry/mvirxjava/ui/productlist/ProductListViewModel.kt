package com.jerry.mvirxjava.ui.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jerry.mvirxjava.data.repository.NetworkRepository

import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import com.jerry.mvirxjava.data.model.Product
import com.jerry.mvirxjava.base.ViewState
import com.jerry.mvirxjava.data.model.ProductListResponse

import com.jerry.mvirxjava.ui.productlist.intent.ProductListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val networkRepository : NetworkRepository
) : ViewModel()  {
    private val disposables = CompositeDisposable()
    private val _viewState: MutableLiveData<ViewState<List<Product>>> = MutableLiveData<ViewState<List<Product>>>()
    val viewState : LiveData<ViewState<List<Product>>> =_viewState

    init {
        getProductList()
    }

    fun sendIntent(intent : ProductListIntent) {
        when (intent){
            is ProductListIntent.GetProductList -> getProductList()
        }
    }

    private fun getProductList(){
        _viewState.value = ViewState.Loading
        disposables.add(
            Observable.zip (
                networkRepository.getHoodieList(),
                networkRepository.getSneakerList(),
                BiFunction<ProductListResponse, ProductListResponse, List<Product>> { hoodies, sneakers ->
                    // here we get both the results at a time.
                    val list = mutableListOf<Product>()
                    if (hoodies.products.isNotEmpty())
                        list.addAll(hoodies.products)
                    if (sneakers.products.isNotEmpty())
                        list.addAll(sneakers.products)
                    return@BiFunction list
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { s-> _viewState.value = ViewState.Success(s) },
                    { e-> _viewState.value = ViewState.Failure(e.toString()) }
                )
        )
    }


    override fun onCleared() {
        disposables.clear()
    }
}