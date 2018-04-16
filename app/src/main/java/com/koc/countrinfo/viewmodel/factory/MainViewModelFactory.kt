package com.koc.countrinfo.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.koc.countrinfo.api.RestCountriesApi
import com.koc.countrinfo.viewmodel.MainViewModel
import io.reactivex.Scheduler

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(val api:RestCountriesApi, val processSchedulers: Scheduler, val androidSchedulers: Scheduler):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(api, processSchedulers, androidSchedulers) as T
    }
}