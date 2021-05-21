package com.mavenusrs.currency_conversion.data.local

import com.mavenusrs.currency_conversion.data.local.room.CurrencyDAO
import com.mavenusrs.currency_conversion.data.local.room.CurrencyEntity
import com.mavenusrs.currency_conversion.data.local.room.QuoteDAO
import com.mavenusrs.currency_conversion.data.local.room.QuoteEntity
import javax.inject.Inject

/**
 * this dataSource class for hiding DAOs class from repository, in case there is a lot of DAOs
 */
class CurrencyConversionLocalDS @Inject constructor(private val currencyDAO: CurrencyDAO,
private val quoteDAO : QuoteDAO,
){

    fun getQuotes(source: String) : List<QuoteEntity> {
        return quoteDAO.getAllQuotes(source)
    }

    suspend fun insetQuotes(source: String, quotes : List<QuoteEntity>) {
        quoteDAO.deleteAll(source)
        quoteDAO.insetQuotes(quotes)
    }

    fun getCurrencies() : List<CurrencyEntity>{
        return currencyDAO.getAllCurrencies()
    }

    suspend fun insetCurrencies(currencies : List<CurrencyEntity>) {
        currencyDAO.deleteAll()
        currencyDAO.insetAllCurrencies(currencies)
    }
}