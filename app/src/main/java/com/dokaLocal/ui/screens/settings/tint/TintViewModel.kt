package com.dokaLocal.ui.screens.settings.tint

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TintViewModel @Inject constructor() : ViewModel() {

    val tint = mutableFloatStateOf(0f)
}