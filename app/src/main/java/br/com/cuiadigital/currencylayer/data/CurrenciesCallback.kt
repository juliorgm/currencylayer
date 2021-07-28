package br.com.cuiadigital.currencylayer.data

import br.com.cuiadigital.currencylayer.data.model.Currency

interface CurrenciesCallback {
    fun onSucess(currencies: List<Currency>)
    fun onFailure(throwable: Throwable)
}