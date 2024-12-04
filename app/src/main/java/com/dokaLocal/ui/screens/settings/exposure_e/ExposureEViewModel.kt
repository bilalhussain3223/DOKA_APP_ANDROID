package com.dokaLocal.ui.screens.settings.exposure_e

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExposureEViewModel @Inject constructor() : ViewModel() {

    val exposure = mutableFloatStateOf(1f)
}