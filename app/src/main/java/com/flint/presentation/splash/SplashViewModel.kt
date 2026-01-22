package com.flint.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
        private val preferencesManager: PreferencesManager,
) : ViewModel() {
    val prefData = preferencesManager.getString("sampleKey")

    init {
        saveTempUserData()
    }

    fun sampleSaveData() = viewModelScope.launch {
        preferencesManager.saveString("sampleKey", "sampleValue")
    }

    fun saveTempUserData() = viewModelScope.launch {
        preferencesManager.saveString(ACCESS_TOKEN, "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjgwMDAyMTcxMTM1NzExODE5MSwicm9sZSI6IkZMSU5HIiwidHlwZSI6IkFDQ0VTUyIsImlhdCI6MTc2ODgzODU0MCwiZXhwIjoxNzcwMDQ4MTQwfQ.A2XYyu24IoGAXNSQHJ1S-iudWmg8II2_ivI4EdyyWw9KS9oJlxHKOAhcKrsLpLkc9kllZyxwaTJO1t4vI7oZlg")
        preferencesManager.saveString(USER_ID, "800021711357118191")
        preferencesManager.saveString(USER_NAME, "hojoo")
    }
}
