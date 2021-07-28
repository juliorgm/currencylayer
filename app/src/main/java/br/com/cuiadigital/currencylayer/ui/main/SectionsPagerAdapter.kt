package br.com.cuiadigital.currencylayer.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.cuiadigital.currencylayer.R
import br.com.cuiadigital.currencylayer.ui.fragments.currency_conversion.CurrencyConversionFragment
import br.com.cuiadigital.currencylayer.ui.fragments.currency_list.CurrencyListFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

private val TAB_CURRENCY_CONVERSION = 1
private val NUM_TABS = 2

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position) {
            TAB_CURRENCY_CONVERSION -> return CurrencyListFragment()
            else -> return CurrencyConversionFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return NUM_TABS
    }
}