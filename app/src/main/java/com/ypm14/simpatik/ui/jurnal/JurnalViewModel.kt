package com.ypm14.simpatik.ui.jurnal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ypm14.simpatik.data.Jadwal
import com.ypm14.simpatik.data.Jurnal
import com.ypm14.simpatik.data.Lampiran
import com.ypm14.simpatik.data.SimpatikRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class JurnalUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val jadwal: Jadwal? = null,
    val jurnal: Jurnal? = null,
    val jurnalSebelumnya: String = "",
    val materi: String = "",
    val tujuanPembelajaran: String = "",
    val capaianPembelajaran: String = "",
    val catatanKelas: String = "",
    val kendala: String = "",
    val tindakLanjut: String = "",
    val catatanSiswaKhusus: String = "",
    val lampiran: List<Lampiran> = emptyList(),
    val error: String? = null,
    val saveSuccess: Boolean = false
)

class JurnalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JurnalUiState())
    val uiState: StateFlow<JurnalUiState> = _uiState.asStateFlow()

    fun load(jadwalId: String, presensiId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val jadwal = SimpatikRepo.getJadwalById(jadwalId).getOrNull()
            val today = System.currentTimeMillis()
            val existingJurnal = SimpatikRepo.getJurnalByJadwalAndDate(jadwalId, today).getOrNull()?.flatten()
            val jurnalSebelumnya = jadwal?.let { j ->
                SimpatikRepo.getJurnalSebelumnya(j.guruId, j.mapelId).getOrNull()
            }?.materi ?: ""

            if (existingJurnal != null) {
                val j = existingJurnal
                _uiState.value = JurnalUiState(
                    isEditing = true,
                    jadwal = jadwal,
                    jurnal = existingJurnal,
                    jurnalSebelumnya = jurnalSebelumnya,
                    materi = j.materi,
                    tujuanPembelajaran = j.tujuanPembelajaran,
                    capaianPembelajaran = j.capaianPembelajaran,
                    catatanKelas = j.catatanKelas,
                    kendala = j.kendala,
                    tindakLanjut = j.tindakLanjut,
                    catatanSiswaKhusus = j.catatanSiswaKhusus,
                    lampiran = j.lampiran
                )
            } else {
                _uiState.value = JurnalUiState(
                    jadwal = jadwal,
                    jurnalSebelumnya = jurnalSebelumnya
                )
            }
        }
    }

    fun updateMateri(v: String) { _uiState.value = _uiState.value.copy(materi = v) }
    fun updateTujuan(v: String) { _uiState.value = _uiState.value.copy(tujuanPembelajaran = v) }
    fun updateCapaian(v: String) { _uiState.value = _uiState.value.copy(capaianPembelajaran = v) }
    fun updateCatatanKelas(v: String) { _uiState.value = _uiState.value.copy(catatanKelas = v) }
    fun updateKendala(v: String) { _uiState.value = _uiState.value.copy(kendala = v) }
    fun updateTindakLanjut(v: String) { _uiState.value = _uiState.value.copy(tindakLanjut = v) }
    fun updateCatatanSiswaKhusus(v: String) { _uiState.value = _uiState.value.copy(catatanSiswaKhusus = v) }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            val state = _uiState.value
            val jadwal = state.jadwal ?: return@launch
            val guru = SimpatikRepo.currentUser() ?: return@launch

            val jurnal = Jurnal(
                id = state.jurnal?.id ?: UUID.randomUUID().toString(),
                tanggal = System.currentTimeMillis(),
                jadwalId = jadwal.id,
                guruId = guru.uid,
                materi = state.materi,
                tujuanPembelajaran = state.tujuanPembelajaran,
                capaianPembelajaran = state.capaianPembelajaran,
                catatanKelas = state.catatanKelas,
                kendala = state.kendala,
                tindakLanjut = state.tindakLanjut,
                catatanSiswaKhusus = state.catatanSiswaKhusus,
                lampiran = state.lampiran
            )

            val result = if (state.isEditing)
                SimpatikRepo.updateJurnal(jurnal)
            else
                SimpatikRepo.saveJurnal(jurnal)

            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true); onSuccess() },
                onFailure = { _uiState.value = _uiState.value.copy(isSaving = false, error = it.message) }
            )
        }
    }
}

private fun Jurnal.flatten() = this
