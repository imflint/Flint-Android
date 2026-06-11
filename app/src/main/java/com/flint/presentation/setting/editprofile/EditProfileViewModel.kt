package com.flint.presentation.setting.editprofile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.data.local.PreferencesManager
import com.flint.domain.repository.StorageRepository
import com.flint.domain.repository.UserRepository
import com.flint.domain.type.FileExtension
import com.flint.domain.type.StoragePathType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
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
                    uploadProfileImage(state.profileImageUri)
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

    private suspend fun uploadProfileImage(uri: Uri): Result<Unit> {
        val mimeType = withContext(Dispatchers.IO) {
            context.contentResolver.getType(uri)
        } ?: "image/jpeg"

        val extension = mimeTypeToFileExtension(mimeType)

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = StoragePathType.USER_PROFILE,
            extension = extension,
        ).getOrElse { error ->
            Timber.e(error, "Failed to get presigned URL")
            return Result.failure(error)
        }

        val imageBytes = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } ?: return Result.failure(IllegalStateException("Failed to open image stream"))

        storageRepository.uploadToS3(
            uploadUrl = presignedUrl.uploadUrl,
            imageBytes = imageBytes,
            mimeType = mimeType,
        ).getOrElse { error ->
            Timber.e(error, "Failed to upload profile image to S3")
            return Result.failure(error)
        }

        return userRepository.updateProfileImage(presignedUrl.key)
            .map { }
    }

    private fun mimeTypeToFileExtension(mimeType: String): FileExtension = when (mimeType) {
        "image/png" -> FileExtension.PNG
        "image/gif" -> FileExtension.GIF
        "image/webp" -> FileExtension.WEBP
        else -> FileExtension.JPEG
    }
}
