package br.com.cuiadigital.currencylayer.ui.fragments.currency_list

import android.util.Log
import androidx.lifecycle.*
import br.com.cuiadigital.currencylayer.data.CurrenciesCallback
import br.com.cuiadigital.currencylayer.data.DataSourceRemote
import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.util.Constantes
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.SEARCH_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class ApiStatus { LOADING, ERROR, DONE }

class CurrencyListViewModel(private val dataSourceRemote: DataSourceRemote) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _listCurrencies = MutableLiveData<List<Currency>>()
    val listCurrencies: LiveData<List<Currency>> = _listCurrencies

    val listCurreciesFiltered= MutableLiveData<List<Currency>>()
    var searchCurrencyCode= MutableLiveData<Int>()

    init {
        getListCurrencies()
        searchCurrencyCode.value = SEARCH_NAME
    }

    private fun getListCurrencies() {
        _status.value = ApiStatus.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataSourceRemote.getCurrencies(object : CurrenciesCallback{
                    override fun onSucess(currencies: List<Currency>) {
                        _listCurrencies.postValue(currencies)
                        _status.value = ApiStatus.DONE
                        filterList(currencies)
                    }

                    override fun onFailure(throwable: Throwable) {
                        _status.value = ApiStatus.ERROR
                        Log.e("Exception", throwable.message.toString())
                    }
                })
            }catch (e: Exception){
                Log.e("Exception", e.message.toString())
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun filterList(list: List<Currency>){
        listCurreciesFiltered.value = orderList(list)
    }

    private fun orderList(list: List<Currency>): List<Currency> {
        if (searchCurrencyCode.value == Constantes.SEARCH_NAME){
            return list.sortedBy { it.name }
        }

        return list.sortedBy { it.code }
    }

    fun initList(){
        listCurreciesFiltered.value = listCurrencies.value?.toList()
    }
}

class CurrencyListViewModelFactory(private val repository: DataSourceRemote) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CurrencyListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}