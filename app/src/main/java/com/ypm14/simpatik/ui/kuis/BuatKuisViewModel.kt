package com.ypm14.simpatik.ui.kuis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Kelas
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.SimpatikRepo
import com.ypm14.simpatik.data.StatusKuis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class KelasOption(val kelasId: String, val kelasNama: String)
data class BuatKuisUiState(
    val isLoading: Boolean = false,
    val judul: String = "",
    val kelasId: String = "",
    val kelasNama: String = "",
    val kelasOptions: List<KelasOption> = listOf(KelasOption("XII-A", "XII-A"), KelasOption("XI-A", "XI-A"), KelasOption("X-RPL", "X-RPL")),
    val mapelNama: String = "",
    val batasWaktu: String = "20",
    val tanggal: Long = System.currentTimeMillis(),
    val error: String? = null
)

class BuatKuisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BuatKuisUiState())
    val uiState: StateFlow<BuatKuisUiState> = _uiState.asStateFlow()

    fun onJudulChanged(v: String) { _uiState.value = _uiState.value.copy(judul = v) }
    fun onKelasSelected(option: KelasOption) { _uiState.value = _uiState.value.copy(kelasId = option.kelasId, kelasNama = option.kelasNama) }
    fun onBatasWaktuChanged(v: String) { _uiState.value = _uiState.value.copy(batasWaktu = v) }
    fun onTanggalChanged(millis: Long) { _uiState.value = _uiState.value.copy(tanggal = millis) }

    fun simpan(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val guru = SimpatikRepo.currentUser() ?: return@launch
            val kuis = Kuis(
                id = UUID.randomUUID().toString(),
                judul = _uiState.value.judul,
                kelasId = _uiState.value.kelasId,
                guruId = guru.uid,
                durasi = (_uiState.value.batasWaktu.toIntOrNull() ?: 20),
                status = StatusKuis.DRAFT,
                createdAt = System.currentTimeMillis() / 1000
            )
            SimpatikRepo.saveKuis(kuis).fold(
                onSuccess = { onCreated(kuis.id) },
                onFailure = { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message) }
            )
        }
    }
}
