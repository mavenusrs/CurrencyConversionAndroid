package com.mavenusrs.currencyconversion.common

class MyThrowable(msg: String?, val errorCode: Int): Throwable(message = msg) {
    companion object {
        const val General_error_code = 500
    }
}