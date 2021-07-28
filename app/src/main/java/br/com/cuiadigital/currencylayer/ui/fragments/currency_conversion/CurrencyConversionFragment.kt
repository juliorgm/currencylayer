package br.com.cuiadigital.currencylayer.ui.fragments.currency_conversion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.cuiadigital.currencylayer.App
import br.com.cuiadigital.currencylayer.R
import br.com.cuiadigital.currencylayer.databinding.FragmentCurrencyConversionBinding
import br.com.cuiadigital.currencylayer.ui.fragments.dialog.CustomDialogFragment
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.DIALOG_SEACH_DESTINY_CODE
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.DIALOG_SEACH_ORIGIN_CODE
import br.com.cuiadigital.currencylayer.util.Constantes.Companion.EXTRA_DIALOG_SEACH

class CurrencyConversionFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyConversionBinding
    private val viewModel: CurrencyConversionViewModel by viewModels {
        CurrencyConversionViewModelFactory((activity?.application as App).dataSourceRemote)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConversionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleEvents()
        initObserver()
    }

    private fun initObserver() {
        viewModel.currencyOrigin.observe(viewLifecycleOwner,{
            viewModel.doCurrencyConversion()
        })
        viewModel.currencyDestiny.observe(viewLifecycleOwner,{
            viewModel.doCurrencyConversion()
        })
        viewModel.conversionNumber.observe(viewLifecycleOwner,{
            viewModel.doCurrencyConversion()
        })

        viewModel.textConversionsDisplay.observe(viewLifecycleOwner, { result ->
            result?.let { binding.txtResult.text = it }
        })
    }

    private fun handleEvents() {
        binding.txtDestiny.setOnClickListener {
            showAlertDialog(DIALOG_SEACH_DESTINY_CODE)
        }

        binding.txtOrigin.setOnClickListener {
            showAlertDialog(DIALOG_SEACH_ORIGIN_CODE)
        }

        binding.tilValueConversion.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valueConversion = s.toString()
                val numberConvertion = valueConversion.toDouble()
                viewModel.setNumberConversion(numberConvertion)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data?.hasExtra(EXTRA_DIALOG_SEACH) == false) return

        if (requestCode == DIALOG_SEACH_ORIGIN_CODE && resultCode == Activity.RESULT_OK) {
            val originCode = data?.getStringExtra(EXTRA_DIALOG_SEACH)
            originCode?.let {
                binding.txtOrigin.text = it
                viewModel.setOrigin(it)
            }

        }

        if (requestCode == DIALOG_SEACH_DESTINY_CODE && resultCode == Activity.RESULT_OK) {
            val destinyCode = data?.getStringExtra(EXTRA_DIALOG_SEACH)
            destinyCode?.let {
                binding.txtDestiny.text = it
                viewModel.setDestiny(it)
            }
        }
    }

    private fun showAlertDialog(code: Int) {
        val fragmentManager = activity?.supportFragmentManager
        val dialogFragment = CustomDialogFragment.newInstance()
        dialogFragment.setTargetFragment(this, code)

        fragmentManager?.let {
            dialogFragment.show(it, EXTRA_DIALOG_SEACH)
        }
    }
}

