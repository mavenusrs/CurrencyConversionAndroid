package com.mavenusrs.domain.currency_conversion.repository

import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote

interface CurrencyConversionRepository {

    suspend fun getCurrencies() : List<Currency>
    suspend fun getQuotes() : List<Quote>

}
