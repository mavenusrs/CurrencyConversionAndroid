package com.mavenusrs.currency_conversion.data.remote

import javax.inject.Inject

class CurrencyConversionRemoteDS @Inject constructor(private val conversionAPI: CurrencyConversionAPI) {

    suspend fun getQuotes(source : String = "USD") = conversionAPI
            .getQuotes(CurrencyConversionAPI.TOKEN_KEY, source)

    suspend fun getCurrencies() = conversionAPI
        .getCurrencies(CurrencyConversionAPI.TOKEN_KEY)
}