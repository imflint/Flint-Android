package com.flint.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.datastore.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel(){

    val prefData = preferencesManager.getString("sampleKey")

    fun sampleSaveData() = viewModelScope.launch {
        preferencesManager.saveString("sampleKey", "sampleValue")
    }
}