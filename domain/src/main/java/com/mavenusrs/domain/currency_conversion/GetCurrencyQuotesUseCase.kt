package com.mavenusrs.domain.currency_conversion

import com.mavenusrs.domain.currency_conversion.model.Quote
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import javax.inject.Inject

class GetCurrencyQuotesUseCase @Inject constructor(private val currencyConversionRepository: CurrencyConversionRepository) {

    suspend fun getQuotes(source: String, amount: Double): List<Quote> {
        val quotes = currencyConversionRepository.getQuotes()
        val usdSourceQuote = quotes[quotes.indexOf(Quote(source))]

        val toUSDTotalAmount = amount / usdSourceQuote.distRate

        return quotes.map {
            it.calculatedRate = it.distRate * toUSDTotalAmount
            it
        }
    }
}