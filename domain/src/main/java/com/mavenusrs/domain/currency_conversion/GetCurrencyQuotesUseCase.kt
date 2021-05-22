package com.mavenusrs.domain.currency_conversion

import com.mavenusrs.domain.currency_conversion.model.Quote
import com.mavenusrs.domain.currency_conversion.model.Response
import com.mavenusrs.domain.currency_conversion.repository.CurrencyConversionRepository
import javax.inject.Inject

class GetCurrencyQuotesUseCase @Inject constructor(private val currencyConversionRepository: CurrencyConversionRepository) {

    suspend fun getQuotes(source: String, amount: Double): Response<List<Quote>> {
        val resQuotes = currencyConversionRepository.getQuotes()
        return resQuotes.apply {
            val quotes = resQuotes.data
            quotes?.also {quoteList->
                val usdSourceQuote = quoteList[quoteList.indexOf(Quote(source))]
                val toUSDTotalAmount = amount / usdSourceQuote.distRate
                quoteList.map { quote->
                    quote.calculatedRate = quote.distRate * toUSDTotalAmount
                    quote
                }
            }
        }

    }
}