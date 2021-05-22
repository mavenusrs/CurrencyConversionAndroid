package com.mavenusrs.domain.currency_conversion.model

data class Quote(
    val id: Int,
    val source: String,
    val distCode: String,
    val distRate: Double = 1.0,
    var calculatedRate: Double = 0.0,
    var distName: String,
    ) {
    constructor(distCode: String) : this(1, "", distCode, 1.0, 1.0, "")

    override fun equals(other: Any?): Boolean {
        other?.apply {
            if (this is Quote && this.distCode == this@Quote.distCode)
                return true
        }
        return super.equals(other)
    }

    override fun toString(): String {
        return "Source currency $source destination Code $distCode with rate per unit $distRate" +
                " the calculate total rate $calculatedRate "
    }
}
