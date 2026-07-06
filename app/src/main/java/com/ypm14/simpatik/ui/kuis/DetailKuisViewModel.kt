package com.ypm14.simpatik.ui.kuis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Attempt
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.SimpatikRepo
import com.ypm14.simpatik.data.StatusKuis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailKuisUiState(
    val isLoading: Boolean = true,
    val kuis: Kuis? = null,
    val attempts: List<Attempt> = emptyList(),
    val showNilaiSheet: Boolean = false,
    val showAkhiriDialog: Boolean = false,
    val error: String? = null
)

class DetailKuisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailKuisUiState())
    val uiState: StateFlow<DetailKuisUiState> = _uiState.asStateFlow()

    fun loadKuis(kuisId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val kuis = SimpatikRepo.getKuisById(kuisId)
            _uiState.value = DetailKuisUiState(
                kuis = kuis,
                attempts = listOf(
                    Attempt(id = "a1", kuisId = kuisId, namaSiswa = "Ahmad Faiz", noAbsen = 1, status = "SELESAI", nilai = 85.0, benar = 8, salah = 2, durasiPengerjaan = 45, submitPada = System.currentTimeMillis() - 3600000),
                    Attempt(id = "a2", kuisId = kuisId, namaSiswa = "Bella Safira", noAbsen = 2, status = "SELESAI", nilai = 92.0, benar = 9, salah = 1, durasiPengerjaan = 30, submitPada = System.currentTimeMillis() - 1800000),
                )
            )
        }
    }

    fun bukaNilaiSheet() { _uiState.value = _uiState.value.copy(showNilaiSheet = true) }
    fun tutupNilaiSheet() { _uiState.value = _uiState.value.copy(showNilaiSheet = false) }
    fun bukaAkhiriDialog() { _uiState.value = _uiState.value.copy(showAkhiriDialog = true) }
    fun tutupAkhiriDialog() { _uiState.value = _uiState.value.copy(showAkhiriDialog = false) }

    fun akhiriKuis() {
        viewModelScope.launch {
            val kuis = _uiState.value.kuis ?: return@launch
            SimpatikRepo.updateKuis(kuis.copy(status = StatusKuis.SELESAI))
            _uiState.value = _uiState.value.copy(showAkhiriDialog = false)
            loadKuis(kuis.id)
        }
    }
}
