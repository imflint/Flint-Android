package com.flint.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val preferencesManager: PreferencesManager,
    ) : ViewModel() {
        init {
            sampleSaveData()
        }
        val prefData = preferencesManager.getString(USER_ID)

        fun sampleSaveData() =
            viewModelScope.launch {
//                preferencesManager.saveString(USER_ID, "801159854933808613")
//                preferencesManager.saveString(USER_NAME, "코코아")

                preferencesManager.saveString("sampleKey", "sampleValue")

                Timber.d("prefData: ${prefData.first()}")
            }
    }
