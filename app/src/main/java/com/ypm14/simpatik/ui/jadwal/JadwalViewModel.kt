package com.ypm14.simpatik.ui.jadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Jadwal
import com.ypm14.simpatik.data.SimpatikRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JadwalUiState(
    val isLoading: Boolean = true,
    val selectedDay: String = "Senin",
    val jadwalList: List<Jadwal> = emptyList(),
    val error: String? = null
)

class JadwalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JadwalUiState())
    val uiState: StateFlow<JadwalUiState> = _uiState.asStateFlow()

    private val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu")

    init { loadJadwal() }

    fun getDays() = days

    fun selectDay(day: String) {
        _uiState.value = _uiState.value.copy(selectedDay = day)
        loadJadwal()
    }

    fun loadJadwal() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val guru = SimpatikRepo.currentUser()
            val hari = _uiState.value.selectedDay
            try {
                val list = if (guru != null)
                    SimpatikRepo.getJadwalByGuruIdAndHari(guru.uid, hari)
                else emptyList()
                _uiState.value = _uiState.value.copy(isLoading = false, jadwalList = list)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
