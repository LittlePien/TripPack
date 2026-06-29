package com.example.trippack.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun login() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email dan password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.login(state.email, state.password)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isLoginSuccessful = true)
            }.onFailure { e ->
                val message = when {
                    e.message?.contains("incorrect", ignoreCase = true) == true ||
                            e.message?.contains("malformed", ignoreCase = true) == true ||
                            e.message?.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) == true -> "Email atau password salah"
                    e.message?.contains("no user record", ignoreCase = true) == true -> "Akun tidak ditemukan"
                    e.message?.contains("network", ignoreCase = true) == true -> "Periksa koneksi internet Anda" else -> "Login gagal, silakan coba lagi"
                }
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
            }
        }
    }
}