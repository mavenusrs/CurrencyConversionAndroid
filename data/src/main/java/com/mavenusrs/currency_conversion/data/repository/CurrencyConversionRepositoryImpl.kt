package com.mavenusrs.currency_conversion.data.repository

import android.os.SystemClock
import com.mavenusrs.currency_conversion.data.SharedPreferenceHelper
import com.mavenusrs.currency_conversion.data.local.CurrencyConversionLocalDS
import com.mavenusrs.currency_conversion.data.local.room.CurrencyEntity
import com.mavenusrs.currency_conversion.data.local.room.QuoteEntity
import com.mavenusrs.currency_conversion.data.remote.CurrencyConversionRemoteDS
import com.mavenusrs.currency_conversion.data.repository.map.mapCurrencyEntitiesTOCurrencies
import com.mavenusrs.currency_conversion.data.repository.map.mapQuotesEntityToQuotes
import com.mavenusrs.currency_conversion.data.repository.map.mapToCurrencyEntities
import com.mavenusrs.currency_conversion.data.repository.map.mapToQuoteEntities
import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import javax.inject.Inject

class CurrencyConversionRepositoryImpl @Inject constructor(
    private val currencyConversionLocalDS: CurrencyConversionLocalDS,
    private val currencyConversionRemoteDS: CurrencyConversionRemoteDS,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
) : CurrencyConversionRepository {

    override suspend fun getCurrencies(): List<Currency> {
        val isFreshData = isFreshData("list")
        val currenciesEntities: List<CurrencyEntity>?
        if (isFreshData) {
            currenciesEntities = currencyConversionLocalDS.getCurrencies()
        } else {
            currenciesEntities = currencyConversionRemoteDS
                .getCurrencies().currencies.mapToCurrencyEntities()

            // local work (replace currently cashed currencies and reset time stamp
            if (currenciesEntities.isNotEmpty()) {
                currencyConversionLocalDS.insetCurrencies(currenciesEntities)
                resetTimeStamp("list")
            }
        }

        return mapCurrencyEntitiesTOCurrencies(currenciesEntities)

    }

    override suspend fun getQuotes(source: String): List<Quote> {
        val quotesEntities: List<QuoteEntity>?

        val isFreshData = isFreshData("live-$source")
        if (isFreshData) {
            quotesEntities = currencyConversionLocalDS.getQuotes(source)
        } else {
            val quotesResponse = currencyConversionRemoteDS
                .getQuotes(source)
            quotesEntities = quotesResponse.quotes
                .mapToQuoteEntities(source, quotesResponse.timestamp)

            // local work (replace currently cashed quotes and reset time stamp
            if (quotesEntities.isNotEmpty()) {
                currencyConversionLocalDS.insetQuotes(source, quotesEntities)
                resetTimeStamp("live-$source")
            }
        }

        return mapQuotesEntityToQuotes(quotesEntities)
    }


    private fun isFreshData(path: String): Boolean {
        val timeStamp = sharedPreferenceHelper.getAPITimeStamp(path)
        return (SystemClock.elapsedRealtime() - timeStamp) < 30 * 60 * 1000
    }

    private fun resetTimeStamp(path: String) {
        sharedPreferenceHelper.putAPITimeStamp(path = path)
    }
}


