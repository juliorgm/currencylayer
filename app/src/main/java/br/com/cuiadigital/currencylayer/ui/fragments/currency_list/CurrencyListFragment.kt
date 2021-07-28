package br.com.cuiadigital.currencylayer.ui.fragments.currency_list

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cuiadigital.currencylayer.App
import br.com.cuiadigital.currencylayer.R
import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.databinding.FragmentCurrencyListBinding
import br.com.cuiadigital.currencylayer.util.Constantes
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.SEARCH_NAME


class CurrencyListFragment : Fragment() {

    private val viewModel: CurrencyListViewModel by viewModels {
        CurrencyListViewModelFactory((activity?.application as App).dataSourceRemote)
    }

    private lateinit var binding: FragmentCurrencyListBinding
    private lateinit var adapter: CurrencyAdapter
    private val layoutContent by lazy { binding.content }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        initObserver()
        setSeach()
        setButtonClick()
    }

    private fun setButtonClick() {
        layoutContent.btnFilterSetting.setOnClickListener{
            showAlertDialog()
        }
    }

    private fun setSeach() {
        layoutContent.tilSearch.editText?.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { itemEdittext ->

                    if (itemEdittext.length > 0){
                        val textFilter = itemEdittext.toString().lowercase()
                        filterList(textFilter)
                    }else{
                        viewModel.initList()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })

    }

    private fun filterList(textFilter: String) {
        val list = viewModel.listCurrencies.value
        list?.let { list->
            val searchCurrencyCode = viewModel.searchCurrencyCode.value
            var newList = listOf<Currency>()
            if (searchCurrencyCode == SEARCH_NAME){
                newList= list.filter { itemList -> itemList.name.lowercase().contains(textFilter) }
            }else{
                newList= list.filter { itemList -> itemList.code.lowercase().contains(textFilter) }
            }

            viewModel.filterList(newList)
        }
    }

    private fun setRecyclerView() {
        adapter = CurrencyAdapter()
        layoutContent.rvCurrencyList.layoutManager = LinearLayoutManager(requireContext())
        layoutContent.rvCurrencyList.adapter = adapter
    }

    fun initObserver(){
        viewModel.listCurreciesFiltered.observe(viewLifecycleOwner, {
            it?.let { list ->
                messageWhenListEmpty(list.size)
                adapter.submitList(list)
            }
        })
        viewModel.status.observe(viewLifecycleOwner, {
            setVisibity()
        })

        viewModel.searchCurrencyCode.observe(viewLifecycleOwner,{
            val list = viewModel.listCurreciesFiltered.value ?: listOf<Currency>()
            viewModel.filterList(list)
        })
    }

    private fun messageWhenListEmpty(listSize: Int) {
        if (listSize == 0) {
            layoutContent.txtMessage.visibility = View.VISIBLE
            layoutContent.txtMessage.text = getString(R.string.message_list_empty)
        } else {
            layoutContent.txtMessage.visibility = View.GONE
        }
    }

    private fun setVisibity(){
        when(viewModel.status.value){
            ApiStatus.DONE -> {
                layoutContent.progressLoading.visibility = View.GONE
                layoutContent.txtMessage.visibility = View.GONE
            }
            ApiStatus.LOADING -> {
                layoutContent.progressLoading.visibility = View.VISIBLE
                layoutContent.txtMessage.visibility = View.GONE
                layoutContent.txtMessage.text = getString(R.string.message_loading)
            }
            ApiStatus.ERROR -> {
                layoutContent.progressLoading.visibility = View.GONE
                layoutContent.txtMessage.visibility = View.VISIBLE
                layoutContent.txtMessage.text = getString(R.string.message_erro)
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialog= AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.search_settings))
        val items = arrayOf(getString(R.string.search_option_name), getString(R.string.search_option_code), )
        val checkedItem = viewModel.searchCurrencyCode.value ?: 0
        alertDialog.setSingleChoiceItems(items, checkedItem,
            { dialog, which ->
                when (which) {
                    0 -> {
                        viewModel.searchCurrencyCode.value = 0
                        dialog.dismiss()
                    }
                    1 -> {
                        viewModel.searchCurrencyCode.value = 1
                        dialog.dismiss()
                    }
                }
            })
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}


