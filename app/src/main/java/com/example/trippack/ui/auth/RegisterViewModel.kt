package com.example.trippack.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onNameChange(value: String) { _uiState.value = _uiState.value.copy(name = value, errorMessage = null) }
    fun onEmailChange(value: String) { _uiState.value = _uiState.value.copy(email = value, errorMessage = null) }
    fun onPhoneChange(value: String) { _uiState.value = _uiState.value.copy(phone = value, errorMessage = null) }
    fun onPasswordChange(value: String) { _uiState.value = _uiState.value.copy(password = value, errorMessage = null) }
    fun togglePasswordVisibility() { _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible) }
    fun toggleAgreedToTerms() { _uiState.value = _uiState.value.copy(agreedToTerms = !_uiState.value.agreedToTerms) }

    fun register() {
        val state = _uiState.value
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Mohon lengkapi semua data")
            return
        }
        if (state.password.length < 8) {
            _uiState.value = state.copy(errorMessage = "Password minimal 8 karakter")
            return
        }
        if (!state.agreedToTerms) {
            _uiState.value = state.copy(errorMessage = "Anda harus menyetujui Syarat & Ketentuan")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.register(state.name, state.email, state.password)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isRegisterSuccessful = true)
            }.onFailure { e ->
                val message = when {
                    e.message?.contains("already in use", ignoreCase = true) == true -> "Email sudah terdaftar, silakan masuk"
                    e.message?.contains("badly formatted", ignoreCase = true) == true ||
                            e.message?.contains("invalid", ignoreCase = true) == true -> "Format email tidak valid"
                    e.message?.contains("weak password", ignoreCase = true) == true -> "Password terlalu lemah, gunakan kombinasi huruf dan angka"
                    e.message?.contains("network", ignoreCase = true) == true -> "Periksa koneksi internet Anda"
                    else -> "Registrasi gagal, silakan coba lagi"
                }
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
            }
        }
    }
}