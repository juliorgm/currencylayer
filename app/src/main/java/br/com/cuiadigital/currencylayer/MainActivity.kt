package br.com.cuiadigital.currencylayer

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.cuiadigital.currencylayer.ui.main.SectionsPagerAdapter
import br.com.cuiadigital.currencylayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAB_CONVERTER = 0
    private val TAB_CURRENCIES = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(TAB_CONVERTER)?.setIcon(R.drawable.ic_converter)
        tabs.getTabAt(TAB_CURRENCIES)?.setIcon(R.drawable.ic_money)


    }
}