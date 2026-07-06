package com.ypm14.simpatik.ui.kuis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.SimpatikRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class KuisUiState(
    val isLoading: Boolean = true,
    val kuisList: List<Kuis> = emptyList(),
    val error: String? = null
)

class KuisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KuisUiState())
    val uiState: StateFlow<KuisUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val guru = SimpatikRepo.currentUser()
            try {
                val list = if (guru != null) SimpatikRepo.getKuisByGuruId(guru.uid) else emptyList()
                _uiState.value = KuisUiState(isLoading = false, kuisList = list)
            } catch (e: Exception) {
                _uiState.value = KuisUiState(isLoading = false, error = e.message)
            }
        }
    }
}
