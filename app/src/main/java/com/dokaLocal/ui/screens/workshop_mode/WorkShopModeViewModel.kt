package com.dokaLocal.ui.screens.workshop_mode

import androidx.lifecycle.ViewModel
import com.dokaLocal.data.data_source.network.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkShopModeViewModel @Inject constructor(
    val remoteDataSource: RemoteDataSource
) : ViewModel() {
    // Your logic using remoteDataSource can go here
}
