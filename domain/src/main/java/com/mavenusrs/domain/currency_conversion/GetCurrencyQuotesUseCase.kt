package com.mavenusrs.domain.currency_conversion

import com.mavenusrs.domain.currency_conversion.model.Quote
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository

class GetCurrencyQuotesUseCase(private val currencyConversionRepository: CurrencyConversionRepository)
{
    suspend fun getQuotes(): List<Quote> {
        return currencyConversionRepository.getQuotes()
    }
}