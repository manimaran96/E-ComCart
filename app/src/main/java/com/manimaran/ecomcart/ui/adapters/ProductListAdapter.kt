package com.manimaran.ecomcart.ui.adapters

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manimaran.ecomcart.R
import com.manimaran.ecomcart.databases.AppDatabase
import com.manimaran.ecomcart.databases.dao.ProductDao
import com.manimaran.ecomcart.databases.entities.Product
import com.manimaran.ecomcart.databinding.ItemProductBinding
import com.manimaran.ecomcart.ui.listeners.CartQuantityListener
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class ProductListAdapter(
    context: Context,
    private var originalList: List<Product>
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>(), Filterable {

    private var filteredList: List<Product> = ArrayList()
    private var cartList: ArrayList<Product> = ArrayList()
    private var searchText: String = ""
    private val mContext = context
    private var productDao: ProductDao

    init {
        filteredList = originalList
        productDao = AppDatabase.getDatabase(context).productDao()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val viewHolderBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_product,
            parent,
            false
        )

        return ProductViewHolder(viewHolderBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = filteredList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {

                searchText = constraint.toString()
                filteredList = if (searchText.length < 0) {
                    originalList
                } else {
                    val resultList: MutableList<Product> = ArrayList()
                    for (row in originalList) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(searchText.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filteredResults = FilterResults()
                filteredResults.values = filteredList
                return filteredResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence,
                results: FilterResults
            ) {
                filteredList = results.values as List<Product>
                notifyDataSetChanged()
            }
        }
    }

    fun updateCartInfo(cartProducts: ArrayList<Product>) {
        cartList = cartProducts
        for ((pos, prod) in filteredList.withIndex()) {
            val cartProd = cartProducts.find { it.id == prod.id }
            if (cartProd != null) {
                if (prod.count != cartProd.count)
                    notifyItemChanged(pos)
            } else if (prod.count != 0)
                notifyItemChanged(pos)
        }
    }


    inner class ProductViewHolder(private val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(product: Product) {

            var name = SpannableStringBuilder(product.name)
            if (searchText.isNotEmpty()) {
                val sb = SpannableStringBuilder(product.name)
                val word: Pattern = Pattern.compile(searchText.toLowerCase(Locale.ROOT))
                val match: Matcher = word.matcher(product.name.toLowerCase(Locale.ROOT))

                while (match.find()) {
                    val fcs =
                        ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.search_hint))
                    sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
                name = sb
            }

            (viewDataBinding as ItemProductBinding).apply {

                val cartProd = cartList.find { it.id == product.id }
                if (cartProd != null) {
                    if (product.count != cartProd.count)
                        product.count = cartProd.count
                } else {
                    if (product.count != 0) {
                        product.count = 0
                    }
                }

                txtName.text = name
                this.product = product
                Glide.with(mContext).load(product.image).into(viewDataBinding.imgProduct)

                val listener = object : CartQuantityListener {
                    override fun plusQuantity() {
                        if (product.count > 0) {
                            product.count += 1
                            productDao.update(product)
                        } else {
                            product.count = 1
                            productDao.insert(product)
                        }
                        notifyItemChanged(layoutPosition)
                    }

                    override fun minusQuantity() {
                        if (product.count > 1) {
                            product.count -= 1
                            productDao.update(product)
                        } else {
                            product.count = 0
                            productDao.delete(product)
                        }
                        notifyItemChanged(layoutPosition)
                    }

                }
                this.btnAddCart.setOnClickListener { listener.plusQuantity() }
                this.btnPlus.setOnClickListener { listener.plusQuantity() }
                this.btnMinus.setOnClickListener { listener.minusQuantity() }
            }
        }

    }

}