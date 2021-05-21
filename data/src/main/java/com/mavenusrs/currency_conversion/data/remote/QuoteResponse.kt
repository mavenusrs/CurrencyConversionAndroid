package com.mavenusrs.currency_conversion.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class QuoteResponse (

    @SerializedName("success") var success : Boolean,
    @SerializedName("terms") var terms : String,
    @SerializedName("privacy") var privacy : String,
    @SerializedName("timestamp") var timestamp : Long,
    @SerializedName("source") var source : String,
    @SerializedName("quotes") var quotes : JsonObject

)