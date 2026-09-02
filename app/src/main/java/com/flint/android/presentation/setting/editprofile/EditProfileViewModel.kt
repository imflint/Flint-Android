package com.flint.android.presentation.setting.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.common.util.DataStoreKey.USER_NAME
import com.flint.android.data.local.PreferencesManager
import com.flint.android.domain.repository.ProfileImageUploader
import com.flint.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val userRepository: UserRepository,
    private val profileImageUploader: ProfileImageUploader,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _navigateUp = MutableSharedFlow<Unit>()
    val navigateUp = _navigateUp.asSharedFlow()

    fun loadProfile() {
        viewModelScope.launch {
            val nickname = preferencesManager.getString(USER_NAME).first()
            _uiState.update { it.copy(initialNickname = nickname, nickname = nickname) }
        }

        viewModelScope.launch {
            userRepository.getUserProfile(userId = null)
                .onSuccess { profile ->
                    _uiState.update { it.copy(profileImageUrl = profile.profileImageUrl) }
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun updateNickname(nickname: String) {
        if (nickname.length <= EditProfileUiState.MAX_LENGTH) {
            val isFormatValid = EditProfileUiState.isValidFormat(nickname)
            _uiState.update { current ->
                current.copy(
                    nickname = nickname,
                    isFormatValid = isFormatValid,
                    isNicknameAvailable = null,
                    nicknameErrorType = when {
                        !isFormatValid && nickname.isNotEmpty() -> EditProfileNicknameErrorType.INVALID_FORMAT
                        else -> null
                    },
                )
            }
        }
    }

    fun updateProfileImage(uri: Uri?) {
        if (uri != null) {
            _uiState.update { it.copy(profileImageUri = uri, isProfileImageDeleted = false) }
        } else {
            _uiState.update { it.copy(profileImageUri = null, isProfileImageDeleted = true) }
        }
    }

    fun checkNicknameDuplication() {
        val state = _uiState.value
        if (!state.isFormatValid || !state.isNicknameChanged) return

        viewModelScope.launch {
            userRepository.checkNickname(state.nickname)
                .onSuccess { result ->
                    _uiState.update { current ->
                        current.copy(
                            isNicknameAvailable = result.isAvailable,
                            nicknameErrorType = if (!result.isAvailable) {
                                EditProfileNicknameErrorType.DUPLICATE
                            } else {
                                null
                            },
                        )
                    }
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun clearNicknameError() {
        _uiState.update { it.copy(nicknameErrorType = null) }
    }

    fun saveProfile() {
        val state = _uiState.value
        if (!state.canComplete) return

        viewModelScope.launch {
            when {
                state.isProfileImageDeleted -> {
                    userRepository.updateProfileImage("")
                        .onFailure {
                            Timber.e(it, "Failed to delete profile image")
                            return@launch
                        }
                }
                state.profileImageUri != null -> {
                    profileImageUploader.upload(state.profileImageUri)
                        .onFailure {
                            Timber.e(it, "Failed to save profile image")
                            return@launch
                        }
                }
            }
            if (state.isNicknameChanged) {
                userRepository.updateNickname(state.nickname)
                    .onSuccess { preferencesManager.saveString(USER_NAME, state.nickname) }
                    .onFailure {
                        Timber.e(it, "Failed to update nickname")
                        return@launch
                    }
            }
            _navigateUp.emit(Unit)
        }
    }
}
