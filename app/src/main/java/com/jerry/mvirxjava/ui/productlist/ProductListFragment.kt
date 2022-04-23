package com.jerry.mvirxjava.ui.productlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jerry.mvirxjava.R
import com.jerry.mvirxjava.base.BaseFragment
import com.jerry.mvirxjava.base.ViewState
import com.jerry.mvirxjava.databinding.FragmentProductListBinding
import com.jerry.mvirxjava.ui.productlist.adapter.ProductAdapter
import com.jerry.mvirxjava.ui.productlist.intent.ProductListIntent

class ProductListFragment : BaseFragment(R.layout.fragment_product_list) {

    private val viewModel by viewModels<ProductListViewModel>()
    private lateinit var productAdapter: ProductAdapter
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProductListBinding.bind(view)

        //setup swipe Refresh
        binding?.swipeToRefresh?.apply {
            setOnRefreshListener {
                isRefreshing = false
                requestProductList()
            }
        }

        //setup recycle view
        _binding?.rvProduct?.apply {
            productAdapter = ProductAdapter()
            setHasFixedSize(true)

            adapter = productAdapter
        }

        _binding?.btnTest?.setOnClickListener {
            try {
                val i = Integer.valueOf("A")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _binding?.btnTestWithoutTry?.setOnClickListener {
           // val i = Integer.valueOf("A")
        }

        viewModelObserve()
    }

    private fun viewModelObserve(){
        viewModel?.viewState?.observe(this, Observer { viewState->
            when (viewState) {
                is ViewState.Success ->{
                    showLoading(false)
                    productAdapter.submitList(viewState.data)
                }
                is ViewState.Failure ->{
                    showLoading(false)
                    displayRetryDialog(viewState.errorAny)
                }
                is ViewState.Loading->{
                    showLoading(true)
                }
            }
        })
    }

    fun requestProductList(){
        viewModel?.sendIntent(ProductListIntent.GetProductList)
    }

    override fun doRetry() {
        requestProductList()
    }
}