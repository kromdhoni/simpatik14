package com.ypm14.simpatik.ui.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.SimpatikRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class TabRiwayat { PRESENSI, JURNAL, KUIS }

data class RiwayatPresensiItem(
    val id: String,
    val tanggal: Long = 0,
    val kelasNama: String = "",
    val mapelNama: String = "",
    val jumlahHadir: Int = 0,
    val jumlahSakit: Int = 0,
    val jumlahIzin: Int = 0,
    val jumlahAlfa: Int = 0
)

data class RiwayatJurnalItem(
    val id: String,
    val tanggal: Long = 0,
    val kelasNama: String = "",
    val mapelNama: String = "",
    val materi: String = ""
)

data class RiwayatUiState(
    val selectedTab: TabRiwayat = TabRiwayat.PRESENSI,
    val isLoading: Boolean = false,
    val error: String? = null,
    val presensiItems: List<RiwayatPresensiItem> = emptyList(),
    val jurnalItems: List<RiwayatJurnalItem> = emptyList(),
    val kuisItems: List<Kuis> = emptyList()
)

class RiwayatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RiwayatUiState())
    val uiState: StateFlow<RiwayatUiState> = _uiState.asStateFlow()

    init { loadRiwayat() }

    fun loadRiwayat() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val guru = SimpatikRepo.currentUser()
            if (guru == null) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }
            try {
                val presensi = SimpatikRepo.getPresensiList(filterGuruId = guru.uid).map {
                    RiwayatPresensiItem(id = it.id, kelasNama = "-", mapelNama = "-")
                }
                val jurnal = SimpatikRepo.getRiwayatJurnal(guru.uid, 0, Long.MAX_VALUE)
                    .getOrDefault(emptyList()).map {
                        RiwayatJurnalItem(id = it.id, tanggal = it.tanggal, materi = it.materi)
                    }
                val kuis = SimpatikRepo.getKuisByGuruId(guru.uid)
                _uiState.value = RiwayatUiState(
                    presensiItems = presensi,
                    jurnalItems = jurnal,
                    kuisItems = kuis
                )
            } catch (e: Exception) {
                _uiState.value = RiwayatUiState(error = e.message)
            }
        }
    }

    fun changeTab(tab: TabRiwayat) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
}
