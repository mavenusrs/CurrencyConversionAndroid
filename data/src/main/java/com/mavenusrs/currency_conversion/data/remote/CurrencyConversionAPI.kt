package com.mavenusrs.currency_conversion.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConversionAPI {
    @GET("live")
    suspend fun getQuotes(@Query("access_key") key: String
                          , @Query("source") source: String)
    : QuoteResponse

    @GET("list")
    suspend fun getCurrencies(@Query("access_key") key: String)
    : CurrencyResponse

    companion object {
        const val TOKEN_KEY = "1a4a95c4315a916901543c48e17b093f"
    }
}
