package com.dokaLocal.ui.screens.source_picture

import android.graphics.Bitmap

data class ImageSourceScreenState(
    val picture: Bitmap? = null,
    val isLoading: Boolean = true,
    val message: String? = null
)