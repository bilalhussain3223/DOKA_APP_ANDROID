package com.dokaLocal.ui.screens.settings.saturation

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaturationViewModel @Inject constructor() : ViewModel() {
    val saturation = mutableFloatStateOf(1f)
}