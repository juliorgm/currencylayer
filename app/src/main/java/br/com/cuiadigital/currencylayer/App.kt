package br.com.cuiadigital.currencylayer

import android.app.Application
import br.com.cuiadigital.currencylayer.data.network.CurrencyApi.retrofitService
import br.com.cuiadigital.currencylayer.data.DataSourceRemote

class App : Application(){
    val currencyApiService by lazy { retrofitService }
    val dataSourceRemote by lazy { DataSourceRemote(currencyApiService) }
}