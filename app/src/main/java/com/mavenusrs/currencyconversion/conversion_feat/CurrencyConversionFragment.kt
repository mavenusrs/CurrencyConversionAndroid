package com.mavenusrs.currencyconversion.conversion_feat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mavenusrs.currencyconversion.R
import com.mavenusrs.currencyconversion.common.DeviceUtils
import com.mavenusrs.currencyconversion.common.Resource
import com.mavenusrs.currencyconversion.databinding.FragmentFirstBinding
import com.mavenusrs.domain.currency_conversion.model.Currency
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [CurrencyConversionFragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class CurrencyConversionFragment : Fragment() {

    private lateinit var amountEditText: AppCompatEditText
    private lateinit var currencySpinner: AppCompatSpinner

    private val conversionViewModel : ConversionViewModel by activityViewModels()
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        binding.btnRateCalc.setOnClickListener {
            val amount = amountEditText.text.toString()
            val selectedCurrency: Currency = currencySpinner.selectedItem as Currency

            fetchQuotes(amount, selectedCurrency)
        }

        fetchCurrencies()
    }

    private fun initViews() {
        currencySpinner = binding.spCurrency
        amountEditText = binding.etAccount
        binding.rvQuotes.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun fetchQuotes(amount: String, selectedCurrency: Currency) {
        if (amount.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.empty_amount_warning), Toast.LENGTH_LONG
            ).show()
        } else {
            DeviceUtils.hideKeyboard(requireActivity())

            conversionViewModel.fetchQuotes(selectedCurrency.currencyCode, amount.toDouble())
            conversionViewModel.getQuotes().observe(viewLifecycleOwner, {
                it?.also {resource ->
                    when (resource) {
                        is Resource.Success -> {
                            hideLoading()
                            resource.data?.apply {
                                val currencyAdapter = CurrencyRateAdapter(this)
                                binding.rvQuotes.adapter = currencyAdapter
                                currencyAdapter.notifyItemRangeInserted(0, this.size)
                            }
                            resource.freshData?.also { isFresh ->
                                binding.tvDataStatus.apply {
                                    visibility = View.VISIBLE
                                    if (isFresh) {
                                        text = getString(R.string.fresh_data)
                                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                                    } else {
                                        text = getString(R.string.obsolete_data)
                                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                                    }
                                }
                            }

                        }
                        is Resource.Failed -> {
                            hideLoading()
                            resource.myThrowable.message?.let { message ->
                                Toast.makeText(
                                    requireContext(),
                                    message, Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        is Resource.Loading -> {
                            showLoading()
                        }
                    }
                }
            })
        }
    }

    private fun fetchCurrencies() {
        conversionViewModel.fetchCurrencies()
        conversionViewModel.getCurrencies().observe(viewLifecycleOwner, {
            it?.apply {
                when (this) {
                    is Resource.Success -> {
                        hideLoading()
                        this.data?.apply {
                            val currencyArrayAdapter =
                                ArrayAdapter(requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    this,
                                )
                            currencyArrayAdapter.setDropDownViewResource(
                                android.R.layout.simple_dropdown_item_1line
                            )
                            currencySpinner.adapter = currencyArrayAdapter
                            Log.d(TAG, "Items size is ${this.size}")
                        }

                    }
                    is Resource.Failed -> {
                        hideLoading()
                        this.myThrowable.message?.let { message ->
                            Toast.makeText(
                                requireContext(),
                                message, Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                    is Resource.Loading -> {
                        binding.tvDataStatus.visibility = View.VISIBLE
                        showLoading()
                    }
                }
            }
        })
    }

    private fun showLoading() {
       binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FirstFragment"
    }
}