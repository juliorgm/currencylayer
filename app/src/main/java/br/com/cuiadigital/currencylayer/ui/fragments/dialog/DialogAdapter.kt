package br.com.cuiadigital.currencylayer.ui.fragments.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.databinding.DialogItemBinding


class DialogAdapter : RecyclerView.Adapter<DialogAdapter.ViewHolder>() {
    private val listCurrencies= mutableListOf<Currency>()

    var clickListener : (Currency) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = listCurrencies[position]
        val textItem = "${currency.code}-${currency.name}"
        holder.contentView.text = textItem
        holder.contentView.setOnClickListener {
            clickListener(currency)
        }
    }

    override fun getItemCount(): Int = listCurrencies.size
    fun submitList(currencies: List<Currency>) {
        listCurrencies.clear()
        listCurrencies.addAll(currencies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: DialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.itemDialog
    }

}