package com.mavenusrs.domain.currency_conversion.model

data class Response<T> (
    var fresh: Boolean? = null,
    var data: T? = null,
)