package br.com.cuiadigital.currencylayer.ui.fragments.currency_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.cuiadigital.currencylayer.data.model.Currency
import br.com.cuiadigital.currencylayer.databinding.CurrencyItemBinding

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {
    private lateinit var binding: CurrencyItemBinding
    private var currentList: List<Currency> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = "${currentList[position].code} - ${currentList[position].name}"
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(list: List<Currency>){
        currentList = list
        notifyDataSetChanged()
    }

    class ViewHolder (private val binding: CurrencyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String){
            val arraDividerItem = item.split(DIVIDER_ITEM)
            binding.txtCode.text = arraDividerItem[0]
            binding.txtName.text = arraDividerItem[1]
        }
    }

    companion object{
        private const val DIVIDER_ITEM = " - "
    }

}