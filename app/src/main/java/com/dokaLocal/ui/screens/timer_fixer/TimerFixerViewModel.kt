package com.dokaLocal.ui.screens.timer_fixer

import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerFixerViewModel @Inject constructor() : ViewModel() {
    val maxTime = mutableStateOf(59L)
    val timeLeft = mutableStateOf(maxTime.value)
    val timeSpent = mutableStateOf(0)
    val progress = mutableStateOf(1f)
    val paused = mutableStateOf(false)
    var navigateNext: () -> Unit = {}
    var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null
    private var soundJob: Job? = null
    private val interval = 1L

    fun loadProgress() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in (maxTime.value - timeSpent.value) downTo 0 step interval) {
                if (paused.value) return@launch
                delay(1000)
//                timeSpent.value += interval.toInt()
//                timeLeft.value = (maxTime.value - timeSpent.value) / 1
//                progress.value = 1f - (timeSpent.value.toFloat() / maxTime.value.toFloat())
                timeLeft.value = i
            }
            if (soundJob == null) playBeeps()
        }
    }

    private fun playBeeps() {
        soundJob = viewModelScope.launch(Dispatchers.Default) {
            repeat(3) {
                async {
                    mediaPlayer?.start()
                    delay(500)
                }.await()
            }
            delay(1000)
            repeat(3) {
                async {
                    mediaPlayer?.start()
                    delay(500)
                }.await()
            }
            navigateNext()
        }
    }

    fun pauseTimer() {
        paused.value = true
    }

    fun resumeTimer() {
        paused.value = false
        loadProgress()
    }

    override fun onCleared() {
        timerJob?.cancel()
        soundJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }
}