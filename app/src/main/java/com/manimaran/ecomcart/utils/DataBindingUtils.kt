package com.manimaran.ecomcart.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import java.text.DecimalFormat

object DataBindingUtils {

    fun getProductPrice(price: String, specialPrice: String): CharSequence {
        var priceVal = SpannableString(specialPrice)
        if (specialPrice != price) {
            priceVal = SpannableString("$specialPrice($price)")
            priceVal.setSpan(
                StrikethroughSpan(),
                specialPrice.length + 1,
                specialPrice.length + price.length + 1,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        return priceVal
    }

    fun getCartCount(count: Int): String {
        return if (count > 0) {
            if (count < 9) count.toString() else "9+"
        } else "0"
    }

    fun getActualPrice(specialPrice: String, count: Int): Int {
        return count * specialPrice.replace(",", "").replace("₹", "").toInt()
    }

    fun getCartProductPrice(specialPrice: String, count: Int): String {
        return "$count x $specialPrice = ₹${getFormattedCost(getActualPrice(specialPrice, count))}"
    }

    fun getFormattedCost(price: Int): String {
        return DecimalFormat("##,##,##,###.00").format(price)
    }

}