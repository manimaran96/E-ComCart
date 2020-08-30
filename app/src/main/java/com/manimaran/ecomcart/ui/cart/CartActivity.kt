package com.manimaran.ecomcart.ui.cart

import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manimaran.ecomcart.R
import com.manimaran.ecomcart.databases.entities.Product
import com.manimaran.ecomcart.databinding.ActivityCartBinding
import com.manimaran.ecomcart.ui.adapters.CartListAdapter
import com.manimaran.ecomcart.ui.base.BaseActivity
import com.manimaran.ecomcart.utils.DataBindingUtils
import com.manimaran.ecomcart.utils.DataBindingUtils.getFormattedCost
import com.manimaran.ecomcart.viewmodel.ProductCartViewModel
import kotlinx.android.synthetic.main.activity_main.*

class CartActivity : BaseActivity<ActivityCartBinding>() {
    private lateinit var mAdapter: CartListAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productList = ArrayList<Product>()
        mAdapter = CartListAdapter(this, productList)


        recyclerView.apply {
            var spanCount = 1
            val orientation = this.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                spanCount = 2
            mLayoutManager = GridLayoutManager(this@CartActivity, spanCount)
            layoutManager = mLayoutManager
            adapter = mAdapter
        }


        val productCartViewModel = ViewModelProvider(this).get(ProductCartViewModel::class.java)
        productCartViewModel.allProducts.observe(this, Observer {
            productList.clear()
            if (it.isNotEmpty()) {
                binding.showEmpty = false
                productList.addAll(it)
                mAdapter.notifyDataSetChanged()
            } else {
                binding.showEmpty = true
            }


            binding.totalItem = "${it.size} Item" + if(it.size>1) "s" else ""

            var totalCost = 0
            for(cart in it){
                totalCost += DataBindingUtils.getActualPrice(cart.specialPrice, cart.count)
            }
            binding.totalCost = "Total ₹${getFormattedCost(totalCost)}"
        })

        binding.btnCancel.setOnClickListener{finish()}
        binding.btnShopNow.setOnClickListener{finish()}

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_cart
    }


}