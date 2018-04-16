package com.koc.countrinfo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.koc.countrinfo.api.RestCountriesApi
import com.koc.countrinfo.model.data.CountryDataModel
import com.koc.countrinfo.model.ui.CountryUiModel
import com.koc.countrinfo.util.toUiModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(val api:RestCountriesApi, val processSchedulers: Scheduler, val androidSchedulers: Scheduler):ViewModel() {

    private val data = MutableLiveData<List<CountryDataModel>>()
    val uiData = Transformations.map(data,{
        val dataReturn = mutableListOf<CountryUiModel>()
        for (d in it){
            dataReturn.add(d.toUiModel)
        }
        dataReturn.toList()
    })

    val loading = MutableLiveData<Boolean>()

    val error = MutableLiveData<Throwable>()

    val compositeDisposable = CompositeDisposable()

    init {
        updateData()
    }

    fun updateData(){
        loading.value = true
        compositeDisposable.add(api.getAllCountry()

                .subscribeOn(processSchedulers)
                .observeOn(androidSchedulers)
                .subscribe({
                    data.value = it
                    loading.value = false
                },{
                    error.value = it
                    loading.value = false
                }))
    }
}