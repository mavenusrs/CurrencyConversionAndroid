package com.mavenusrs.currencyconversion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.mavenusrs.currencyconversion.common.Resource
import com.mavenusrs.currencyconversion.conversion_feat.ConversionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val conversionViewModel : ConversionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        conversionViewModel.fetchCurrencies()
        conversionViewModel.getCurrencies().observe(this, {
            it?.apply {
                when (this) {
                    is Resource.Success -> {
                        val dataa = this.data
                        dataa!!.forEach {currency ->
                            Log.d(TAG, currency.toString())
                        }
                        val currency = dataa!![Random.nextInt(0, dataa.size - 1)]
                        conversionViewModel.fetchQuotes(currency.currencyCode, 100.0)
                        conversionViewModel.getQuotes().observe(this@MainActivity, {
                            if (it is Resource.Success) {
                                val quote = it.data
                                quote!!.forEach { quoteItem ->
                                    Log.d(TAG, quoteItem.toString())
                                }
                            }
                        })
                    }
                    is Resource.Failed -> {
                        this.myThrowable.message?.let {
                                it1 -> Log.d(TAG, it1)
                        }

                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
