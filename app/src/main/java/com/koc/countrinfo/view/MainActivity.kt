package com.koc.countrinfo.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahmadrosid.svgloader.SvgLoader
import com.koc.countrinfo.R
import com.koc.countrinfo.api.RestCountriesApi
import com.koc.countrinfo.model.ui.CountryUiModel
import com.koc.countrinfo.viewmodel.MainViewModel
import com.koc.countrinfo.viewmodel.factory.MainViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_countrylist_item.*
import kotlinx.android.synthetic.main.main_countrylist_item.view.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        viewModel = ViewModelProviders.of(this,
                MainViewModelFactory(RestCountriesApi.instance, Schedulers.io(), AndroidSchedulers.mainThread()))
                .get(MainViewModel::class.java).also {
                    it.uiData.observe(this, Observer {
                        it?.let {
                            updateUi(it)
                        }
                    })
                    it.loading.observe(this, Observer {
                        it?.let {
                            showLoading(it)
                        }
                    })
                }

    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun initUi() {
        countryList.adapter = ListCountryRecyclerViewAdapter()
        countryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun updateUi(newData: List<CountryUiModel>) {
        (countryList.adapter as ListCountryRecyclerViewAdapter).updateData(newData)
    }

    override fun onDestroy() {
        super.onDestroy()
        SvgLoader.pluck().close()
    }

    inner class ListCountryRecyclerViewAdapter : RecyclerView.Adapter<ListCountryRecyclerViewAdapter.ViewHolder>() {

        val data = mutableListOf<CountryUiModel>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_countrylist_item, parent, false))
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.countryName.text = data[position].name
            SvgLoader.pluck()
                    .with(this@MainActivity)
                    .load(data[position].flagUrl,holder.itemView.flagImage)
        }

        fun updateData(newData: List<CountryUiModel>) {
            val oldSize = data.size
            val newSize = newData.size
            val diff = newSize - oldSize
            data.addAll(newData)
            if (diff > 0)
                notifyItemRangeChanged(oldSize, diff)
            else
                notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
