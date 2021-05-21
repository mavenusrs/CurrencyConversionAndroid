package com.mavenusrs.domain.currency_conversion.repository

import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote

interface CurrencyConversionRepository {

    suspend fun getCurrencies() : List<Currency>
    // for unsubscribed version of api, source will be USD
    suspend fun getQuotes(source: String = "USD") : List<Quote>

}
