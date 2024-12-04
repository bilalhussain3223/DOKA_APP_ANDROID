package com.dokaLocal.ui.screens.settings.exposure_timer

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExposureTimerViewModel @Inject constructor() : ViewModel() {
    val timer = mutableFloatStateOf(15F)

}