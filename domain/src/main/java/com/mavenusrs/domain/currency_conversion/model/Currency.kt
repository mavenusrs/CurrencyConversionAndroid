package com.mavenusrs.domain.currency_conversion.model

data class Currency(val id: Int ,
                    var currencyCode : String,
                    var currencyName : String,) {
    override fun toString(): String {
        return "$currencyCode : $currencyName"
    }
}