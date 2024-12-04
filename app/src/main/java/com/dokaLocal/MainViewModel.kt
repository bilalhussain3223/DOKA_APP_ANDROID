package com.dokaLocal

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.dokaLocal.domain.repository.Repository
import com.dokaLocal.ui.screens.edit.Size
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

)
    : ViewModel() {
    var originalBitmap: Bitmap? = null
    var beforeExposure: Bitmap? = null
    var currentBitmap: Bitmap? = null
    var changedBitmap: Bitmap? = null
    var imageSize: Size? = null
    val savedImagesSettings = mutableStateOf(ImageSettings(0f, 0f, 0f, 0f))
    val exposure = mutableFloatStateOf(1f)
    val saturation = mutableFloatStateOf(1f)
    val contrast = mutableFloatStateOf(1f)
    val tint = mutableFloatStateOf(0f)
    val timeForExposure = mutableFloatStateOf(45f) //45
    var finalImageWidth: Dp? = null
    var finalImageHeight: Dp? = null
    var finalImageRotate: Float = 0f
    var defaultImageWidth: Float = 0f
    var defaultImageHeight: Float = 0f
    var isDefault: Boolean = false


    var zoomState by mutableStateOf(ZoomState())

    fun updateZoomState(offsetX: Float, offsetY: Float, zoom: Float) {
        zoomState = zoomState.copy(offsetX = offsetX, offsetY = offsetY, zoom = zoom)
    }

    private val _checkedState = MutableStateFlow(false)
    val checkedState = _checkedState.asStateFlow()

    // Function to update the checked state
    fun toggleChecked() {
        _checkedState.value = !_checkedState.value
    }

    fun clearData() {
        setSettingsDefault()
        beforeExposure = null
        currentBitmap = null
        originalBitmap = null
        imageSize = null
        zoomState = ZoomState()
        finalImageWidth = null
        finalImageHeight = null
        finalImageRotate = 0f
        defaultImageHeight = 0f
        defaultImageWidth = 0f
        isDefault = false
        savedImagesSettings.value = ImageSettings(0f, 0f, 0f, 0f)
    }

    private fun setSettingsDefault() {
        saturation.floatValue = 1f
        exposure.floatValue = 1f
        contrast.floatValue = 1f
        tint.floatValue = 0f
    }
}

data class ImageSettings(
    val zoom: Float,
    val rotation: Float,
    val offsetX: Float,
    val offsetY: Float
)

data class ZoomState(
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    var zoom: Float = 0.5f
)