package com.mavenusrs.domain.currency_conversion.model

data class Currency(val id: Int ,
                    var currencyCode : String,
                    var currencyName : String,) {
    override fun toString(): String {
        return "Item id $id the currency code is $currencyCode, with Name in English $currencyName"
    }
}