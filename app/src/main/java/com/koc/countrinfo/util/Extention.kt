package com.koc.countrinfo.util

import com.koc.countrinfo.model.data.CountryDataModel
import com.koc.countrinfo.model.ui.CountryUiModel

val CountryDataModel.toUiModel : CountryUiModel
get() {
    return CountryUiModel(name, flag)
}