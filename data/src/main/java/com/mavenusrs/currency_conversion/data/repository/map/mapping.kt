package com.mavenusrs.currency_conversion.data.repository.map

import com.google.gson.JsonObject
import com.mavenusrs.currency_conversion.data.local.room.CurrencyEntity
import com.mavenusrs.currency_conversion.data.local.room.QuoteEntity
import com.mavenusrs.domain.currency_conversion.model.Currency
import com.mavenusrs.domain.currency_conversion.model.Quote

fun JsonObject.mapToCurrencyEntities(): List<CurrencyEntity> {
    val size = this.size()
    val currencyList = ArrayList<CurrencyEntity>(size)

    val keySet = this.keySet()
    keySet.forEach {
        val item = CurrencyEntity(currencyList.count(), it, this.get(it).asString)
        currencyList.add(item)
    }
    return currencyList
}

fun JsonObject.mapToQuoteEntities(source: String, timeStamp: Long): List<QuoteEntity> {
    val size = this.size()
    val quoteList = ArrayList<QuoteEntity>(size)

    val keySet = this.keySet()
    keySet.forEach {
        val item = QuoteEntity(quoteList.count(),
            source,
            timeStamp,
            it.substringAfter(source),
            this.get(it).asDouble,
        )
        quoteList.add(item)
    }
    return quoteList
}

fun mapQuotesEntityToQuotes(quotesEntities: List<QuoteEntity>) =
    quotesEntities.map {
        Quote(
            id = it.id,
            source = it.source,
            distCode = it.distCode,
            distRate = it.distRate,
        )
    }

fun mapCurrencyEntitiesTOCurrencies(currenciesEntities: List<CurrencyEntity>) =
    currenciesEntities.map {
        Currency(
            it.id,
            it.currencyCode,
            it.currencyName,
        )
    }