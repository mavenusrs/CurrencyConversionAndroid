package com.mavenusrs.currencyconversion.conversion_feat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mavenusrs.currencyconversion.databinding.RateItemBinding
import com.mavenusrs.domain.currency_conversion.model.Quote

class CurrencyRateAdapter(private val items: List<Quote>): RecyclerView.Adapter<CurrencyRateAdapter.CurrencyRateVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateVH {
        val itemBinding = RateItemBinding.inflate(LayoutInflater
            .from(parent.context), parent, false)
        return CurrencyRateVH(itemBinding)
    }

    override fun onBindViewHolder(holder: CurrencyRateVH, position: Int) {
        items.apply {
            val item : Quote = this[position]
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CurrencyRateVH(private val itemBinding: RateItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Quote) {
            itemBinding.tvRate.text = "${item.calculatedRate}"

            val currency = "${item.distCode}: ${item.distName}"
            itemBinding.tvCurrency.text = currency
        }

    }
}