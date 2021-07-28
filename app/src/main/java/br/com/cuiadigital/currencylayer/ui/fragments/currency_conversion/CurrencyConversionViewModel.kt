package br.com.cuiadigital.currencylayer.ui.fragments.currency_conversion

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.*
import br.com.cuiadigital.currencylayer.data.DataSourceRemote
import br.com.cuiadigital.currencylayer.data.QuotesCallback
import br.com.cuiadigital.currencylayer.data.model.Quote
import br.com.cuiadigital.currencylayer.util.CurrencyConversions
import kotlinx.coroutines.*
import java.util.*

class CurrencyConversionViewModel(private val dataSourceRemote: DataSourceRemote) : ViewModel() {


    private val listQuotes = MutableLiveData<List<Quote>>()

    private val _textConversionsDisplay = MutableLiveData<String>()
    val textConversionsDisplay: LiveData<String> = _textConversionsDisplay

    private val _conversionValue = MutableLiveData<Double>()
    val conversionNumber: LiveData<Double> = _conversionValue

    val _currencyOrigin = MutableLiveData<String>()
    val currencyOrigin: LiveData<String> = _currencyOrigin

    private val _currencyDestiny = MutableLiveData<String>()
    val currencyDestiny: LiveData<String> = _currencyDestiny

    fun setOrigin(originCode: String) {
        _currencyOrigin.value = originCode
    }

    fun setDestiny(destinyCode: String) {
        _currencyDestiny.value = destinyCode
    }

    fun setNumberConversion(numberConvertion: Double) {
        _conversionValue.value = numberConvertion
    }

    private fun getQuotes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataSourceRemote.getQuotes(object : QuotesCallback {
                    override fun onSucess(quotes: List<Quote>) {
                        listQuotes.value = quotes
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("Exception", throwable.message.toString())
                    }
                })
            } catch (e: Exception) {
                Log.e("Exception", e.message.toString())
            }
        }
    }


    fun doCurrencyConversion() {
        initData()
        if (isValidData()) {

            val dolar = listQuotes.value?.let {
                CurrencyConversions.convertCurrency(it, currencyOrigin.value!!, currencyDestiny.value!!,  conversionNumber.value!!)
            }

            val symbol = CurrencyConversions.getSymbol(currencyDestiny.value!!)

            _textConversionsDisplay.value =  "$symbol ${String.format("%.2f", dolar)}"
        }
    }

    private fun initData() {
        val quotes = listQuotes.value

        if (quotes.isNullOrEmpty()) getQuotes()
    }

    private fun isValidData(): Boolean {
        val number: Double = conversionNumber.value ?: 0.0
        if (number <= 0.0) return false

        val codeOrigin = currencyOrigin.value
        if (codeOrigin.equals("") || codeOrigin.isNullOrEmpty()) return false

        val codeDestiny = currencyDestiny.value
        if (codeDestiny.equals("") || codeDestiny.isNullOrEmpty()) return false

        return true
    }
}


class CurrencyConversionViewModelFactory(private val dataSourceRemote: DataSourceRemote) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyConversionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyConversionViewModel(dataSourceRemote) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}