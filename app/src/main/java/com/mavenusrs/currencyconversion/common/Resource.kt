package com.mavenusrs.currencyconversion.common

sealed class Resource<out T> {
    data class Success<T>(val data: T?): Resource<T>()
    data class Failed<T>(val myThrowable: MyThrowable): Resource<T>()
    object Loading: Resource<Nothing>()

    var freshData: Boolean? = false
}
