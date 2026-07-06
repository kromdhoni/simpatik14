package com.ypm14.simpatik.ui.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.SimpatikRepo
import com.ypm14.simpatik.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfilUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null
)

class ProfilViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfilUiState())
    val uiState: StateFlow<ProfilUiState> = _uiState.asStateFlow()

    init { loadProfil() }

    fun loadProfil() {
        viewModelScope.launch {
            _uiState.value = ProfilUiState(isLoading = true)
            _uiState.value = ProfilUiState(user = SimpatikRepo.currentUser())
        }
    }

    fun logout() {
        viewModelScope.launch { SimpatikRepo.logout() }
    }
}
