package com.dokaLocal.ui.screens.settings.contrast

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContrastViewModel @Inject constructor() : ViewModel() {

    val contrast = mutableFloatStateOf(1f)
}