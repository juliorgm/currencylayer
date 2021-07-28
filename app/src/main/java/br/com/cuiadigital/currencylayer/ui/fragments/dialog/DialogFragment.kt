package br.com.cuiadigital.currencylayer.ui.fragments.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cuiadigital.currencylayer.App
import br.com.cuiadigital.currencylayer.data.CurrenciesCallback
import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.databinding.DialogSearchBinding
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.EXTRA_DIALOG_SEACH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CustomDialogFragment : DialogFragment() {

    private lateinit var binding: DialogSearchBinding
    private val adapter by lazy { DialogAdapter() }
    private var listCurrencies = mutableListOf<Currency>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
        setSearch()
        getData()

        adapter.clickListener = { currency ->
            dialog?.let{
                val bundle = Bundle()
                bundle.putString(EXTRA_DIALOG_SEACH, currency.code)

                val intent = Intent().putExtras(bundle)

                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

                dismiss()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setGravity(Gravity.CENTER)
    }

    private fun setSearch() {
        binding.tilSearch.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val textSearch = s.toString().uppercase()
                if (textSearch.length == 0) { adapter.submitList(listCurrencies) }
                else{
                    val filteredList = listCurrencies.filter{
                        val currencyText = "${it.code.uppercase()}-${it.name.uppercase()}"
                        currencyText.contains(textSearch)
                    }
                    Log.i("asdf", textSearch)
                    Log.i("asdf",filteredList.size.toString())
                    adapter.submitList(filteredList)
                }
            }
        })
    }

    private fun setRecyclerview() {
        binding.rvCurrencyList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrencyList.adapter = adapter
    }

    fun getData() {
        val dataSourceRemote = (activity?.application as App).dataSourceRemote
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataSourceRemote.getCurrencies(object : CurrenciesCallback {
                    override fun onSucess(currencies: List<Currency>) {
                        listCurrencies.addAll(currencies)
                        adapter.submitList(currencies)
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


    companion object {
        fun newInstance(): CustomDialogFragment {
            val f = CustomDialogFragment()
            return f
        }
    }
}