package com.ypm14.simpatik.ui.jurnal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JurnalScreen(
    presensiId: String,
    jadwalId: String,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: JurnalViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(jadwalId, presensiId) {
        viewModel.load(jadwalId, presensiId)
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jurnal Mengajar") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.jadwal?.let { j ->
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text("${j.mapelNama} - ${j.kelasNama}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("${j.hari}, ${j.jamMulai}-${j.jamSelesai}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                if (uiState.jurnalSebelumnya.isNotEmpty()) {
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Materi Sebelumnya", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text(uiState.jurnalSebelumnya, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                OutlinedTextField(value = uiState.materi, onValueChange = viewModel::updateMateri, label = { Text("Materi Pembelajaran *") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                OutlinedTextField(value = uiState.tujuanPembelajaran, onValueChange = viewModel::updateTujuan, label = { Text("Tujuan Pembelajaran") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(value = uiState.capaianPembelajaran, onValueChange = viewModel::updateCapaian, label = { Text("Capaian Pembelajaran") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(value = uiState.catatanKelas, onValueChange = viewModel::updateCatatanKelas, label = { Text("Catatan Kelas") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(value = uiState.kendala, onValueChange = viewModel::updateKendala, label = { Text("Kendala") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(value = uiState.tindakLanjut, onValueChange = viewModel::updateTindakLanjut, label = { Text("Tindak Lanjut") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(value = uiState.catatanSiswaKhusus, onValueChange = viewModel::updateCatatanSiswaKhusus, label = { Text("Catatan Siswa Khusus") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

                uiState.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = { viewModel.save(onSaved) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !uiState.isSaving && uiState.materi.isNotBlank()
                ) {
                    if (uiState.isSaving) CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    else Text("${if (uiState.isEditing) "Update" else "Simpan"} Jurnal")
                }
            }
        }
    }
}
