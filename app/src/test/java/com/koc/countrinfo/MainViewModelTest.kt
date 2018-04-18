package com.koc.countrinfo

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.koc.countrinfo.api.RestCountriesApi
import com.koc.countrinfo.model.data.CountryDataModel
import com.koc.countrinfo.model.ui.CountryUiModel
import com.koc.countrinfo.util.LiveDataTestUtil
import com.koc.countrinfo.util.toUiModel
import com.koc.countrinfo.viewmodel.MainViewModel
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.*

import org.mockito.Mockito
import java.util.concurrent.TimeUnit

class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    lateinit var api : RestCountriesApi

    lateinit var vm : MainViewModel

    @Before
    fun setup(){
        RxJavaPlugins.reset()
        api = Mockito.mock(RestCountriesApi::class.java)
    }

    @After
    fun teardown(){
        RxJavaPlugins.reset()
    }

    @Test
    fun whenInitGetAllCountryData() {

        Mockito.`when`(api.getAllCountry()).thenReturn(Single.just(emptyList()))

        vm = MainViewModel(api, Schedulers.trampoline(), Schedulers.trampoline())

        Mockito.verify(api).getAllCountry()
    }

    @Test
    fun whenInGetAllCountryDataSetLoadingTrue(){

        val pcsSchdlr = TestScheduler()
        val andrdSchdlr = TestScheduler()
        Mockito.`when`(api.getAllCountry()).thenReturn(Single.just(emptyList()))

        vm = MainViewModel(api, pcsSchdlr, andrdSchdlr)
        pcsSchdlr.advanceTimeBy(2,TimeUnit.SECONDS)
        Assert.assertEquals(true,LiveDataTestUtil.getValue(vm.loading))
        andrdSchdlr.advanceTimeTo(2,TimeUnit.SECONDS)
        Assert.assertEquals(false,LiveDataTestUtil.getValue(vm.loading))

    }

    @Test
    fun whenGetAllCountryDataErrorSetErrorValue(){
        val expectedReturn = Throwable("a")
        Mockito.`when`(api.getAllCountry())
                .thenReturn(Single.error(expectedReturn))

        vm = MainViewModel(api, Schedulers.trampoline(), Schedulers.trampoline())

        Assert.assertEquals(expectedReturn,LiveDataTestUtil.getValue(vm.error))
    }
}
