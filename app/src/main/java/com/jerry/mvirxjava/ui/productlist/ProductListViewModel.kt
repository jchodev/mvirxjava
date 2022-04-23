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
import com.jerry.mvirxjava.data.repository.local.MyRespository
import com.jerry.mvirxjava.ui.productlist.intent.ProductListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val networkRepository : NetworkRepository,
    private val myRespository: MyRespository
) : ViewModel()  {
    private val disposables = CompositeDisposable()
    private val _viewState: MutableLiveData<ViewState<List<Product>>> = MutableLiveData<ViewState<List<Product>>>()
    val viewState : LiveData<ViewState<List<Product>>> =_viewState

    init {
        getProductList()
        System.out.println("jerry>>" + myRespository.getSomething())
    }

    fun sendIntent(intent : ProductListIntent) {
        when (intent){
            is ProductListIntent.GetProductList -> getProductList()
        }
    }

    private fun getProductList(){
        disposables.add(
            Observable.zip (
                networkRepository.getHoodieList(),
                networkRepository.getSneakerList(),
                BiFunction<ProductListResponse, ProductListResponse, List<Product>> { hoodies, sneakers ->
                    // here we get both the results at a time.
                    var list = mutableListOf<Product>()
                    if (hoodies!=null && hoodies.products.isNotEmpty())
                        list.addAll(hoodies.products)
                    if (sneakers!=null && sneakers.products.isNotEmpty())
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

    private fun getProductList2(){

        disposables.add(
            networkRepository.getSneakerList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()) //2. ...and the exact object type here
                .doOnSubscribe { _viewState.value = ViewState.Loading }
                .subscribe(
                    { s-> _viewState.value = ViewState.Success(s.products) },
                    { e-> _viewState.value = ViewState.Failure(e.toString()) },
                    //{ println("onComplete")}
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}