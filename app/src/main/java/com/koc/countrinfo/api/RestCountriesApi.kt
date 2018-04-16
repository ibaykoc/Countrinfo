package com.koc.countrinfo.api

import com.koc.countrinfo.model.data.CountryDataModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RestCountriesApi {

    companion object {
        private var mInstance : RestCountriesApi? = null
        val instance:RestCountriesApi
            get() {
                if(mInstance == null){
                    mInstance = Retrofit.Builder()
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl("https://restcountries.eu/rest/v2/")
                            .build().create(RestCountriesApi::class.java)
                }
                return mInstance!!
            }
    }

    @GET("all")
    fun getAllCountry() : Single<List<CountryDataModel>>
}