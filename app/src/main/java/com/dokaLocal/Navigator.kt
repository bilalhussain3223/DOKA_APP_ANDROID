package com.dokaLocal

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class Navigator {

    private val _sharedFlow =
        MutableSharedFlow<NavTarget>(extraBufferCapacity = 1)
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun navigateTo(navTarget: NavTarget) {
        _sharedFlow.tryEmit(navTarget)
    }
}

enum class NavTarget(val label: String) {
    Splash("splash"),
    ImageSource("imageSource"),
    Edit("edit"),
    Exposure("exposure"),
    TimerExposure("timerExposure"),
    TimerDeveloper("timerDeveloper"),
    TimerFixer("timerFixer"),
    Settings("settings"),
    ExposureTimer("exposureTimer"),
    ExposureE("exposureE"),
    Saturation("saturation"),
    Contrast("contrast"),
    Tint("tint"),
    ExposureTimerSettings("exposureTimerSettings"),
    Done("done"),
    WorkShopMode("workshopMode")  // Add this enum for WorkShopMode navigation

}