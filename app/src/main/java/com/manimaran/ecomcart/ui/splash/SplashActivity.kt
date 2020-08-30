package com.manimaran.ecomcart.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.manimaran.ecomcart.R
import com.manimaran.ecomcart.ui.home.HomeActivity
import com.manimaran.ecomcart.utils.Utils
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Utils.isInternetConnected(applicationContext)) {
            Handler().postDelayed({ launchHomeScreen() }, 1500)
        } else {
            Snackbar.make(imgLogo, getString(R.string.no_internet), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun launchHomeScreen() {
        startActivity(Intent(baseContext, HomeActivity::class.java))
        finish()
    }
}