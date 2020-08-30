package com.manimaran.ecomcart.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.manimaran.ecomcart.R
import com.manimaran.ecomcart.databases.entities.Product
import com.manimaran.ecomcart.databinding.ActivityMainBinding
import com.manimaran.ecomcart.ui.adapters.ProductListAdapter
import com.manimaran.ecomcart.ui.base.BaseActivity
import com.manimaran.ecomcart.ui.cart.CartActivity
import com.manimaran.ecomcart.viewmodel.ProductCartViewModel
import com.manimaran.ecomcart.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var mAdapter: ProductListAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productList = ArrayList<Product>()
        mAdapter = ProductListAdapter(this, productList)


        recyclerView.apply {
            var spanCount = 2
            val orientation = this.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                spanCount = 5
            mLayoutManager = GridLayoutManager(this@HomeActivity, spanCount)
            layoutManager = mLayoutManager
            adapter = mAdapter
        }

        binding.showLoading = true
        val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getProducts().observe(this, Observer {
            productList.clear()
            if (it.isNotEmpty()) {
                binding.showEmpty = false
                binding.showLoading = false
                productList.addAll(it)
                mAdapter.notifyDataSetChanged()
            } else {
                binding.showEmpty = true
                binding.showLoading = false
            }
        })

        val productCartViewModel = ViewModelProvider(this).get(ProductCartViewModel::class.java)
        productCartViewModel.allProducts.observe(this, Observer {
            var count = 0
            for (cart in it) {
                if (cart.count > 0)
                    count += 1
            }
            binding.cartTotal = count

            mAdapter.updateCartInfo(it as ArrayList<Product>)
        })

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mAdapter.filter.filter(s)
                Handler().post { binding.showEmpty = mAdapter.itemCount <= 0 }
            }
        })

        binding.btnCart.setOnClickListener { launchCartScreen() }


    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    private fun launchCartScreen() {
        startActivity(Intent(baseContext, CartActivity::class.java))
    }

}