package com.ypm14.simpatik.ui.kuis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ypm14.simpatik.data.Attempt
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.StatusKuis
import com.ypm14.simpatik.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKuisScreen(
    kuisId: String,
    onBack: () -> Unit,
    onBuatSoal: (String) -> Unit,
    viewModel: DetailKuisViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(kuisId) { viewModel.loadKuis(kuisId) }

    // Nilai Bottom Sheet
    if (uiState.showNilaiSheet) {
        ModalBottomSheet(onDismissRequest = { viewModel.tutupNilaiSheet() }) {
            Column(Modifier.padding(16.dp)) {
                Text("Nilai Siswa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                uiState.attempts.forEach { att ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${att.noAbsen}. ${att.namaSiswa}")
                        Text("%.1f".format(att.nilai), fontWeight = FontWeight.Bold,
                            color = Success)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    // Akhiri dialog
    if (uiState.showAkhiriDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.tutupAkhiriDialog() },
            title = { Text("Akhiri Kuis") },
            text = { Text("Apakah Anda yakin ingin mengakhiri kuis ini? Siswa tidak akan bisa mengirim jawaban lagi.") },
            confirmButton = {
                TextButton(onClick = { viewModel.akhiriKuis() }) { Text("Ya, Akhiri") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.tutupAkhiriDialog() }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kuis") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val kuis = uiState.kuis ?: return@Scaffold
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Info card
                item {
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(kuis.judul, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Row { Text("Kelas: ", color = MaterialTheme.colorScheme.onSurfaceVariant); Text(kuis.kelasId) }
                            Row { Text("Durasi: ", color = MaterialTheme.colorScheme.onSurfaceVariant); Text("${kuis.durasi} menit") }
                            Row { Text("Jumlah Soal: ", color = MaterialTheme.colorScheme.onSurfaceVariant); Text("${kuis.jumlahSoal} soal") }
                            val dateFmt = remember { SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")) }
                            Row { Text("Dibuat: ", color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dateFmt.format(Date(kuis.createdAt * 1000))) }
                        }
                    }
                }

                // Stats
                if (kuis.jumlahPeserta > 0) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatCard("Peserta", "${kuis.jumlahPeserta}", Color(0xFF1976D2), Modifier.weight(1f))
                            StatCard("Rata-rata", "%.1f".format(kuis.rataRataNilai), Success, Modifier.weight(1f))
                            StatCard("Tertinggi", "%.0f".format(kuis.nilaiTertinggi), Color(0xFFFF9800), Modifier.weight(1f))
                        }
                    }
                }

                // Actions
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (kuis.jumlahSoal == 0) {
                            Button(
                                onClick = { onBuatSoal(kuisId) },
                                modifier = Modifier.weight(1f)
                            ) { Text("Buat Soal") }
                        }
                        if (kuis.status == StatusKuis.AKTIF) {
                            OutlinedButton(
                                onClick = { viewModel.bukaAkhiriDialog() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) { Text("Akhiri Kuis") }
                        }
                        if (kuis.status != StatusKuis.DRAFT) {
                            OutlinedButton(
                                onClick = { viewModel.bukaNilaiSheet() },
                                modifier = Modifier.weight(1f)
                            ) { Text("Lihat Nilai") }
                        }
                    }
                }

                // Attempt list
                if (uiState.attempts.isNotEmpty()) {
                    item { Text("Daftar Nilai", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) }
                    items(uiState.attempts, key = { it.id }) { att ->
                        AttemptCard(att)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(shape = RoundedCornerShape(12.dp), modifier = modifier) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AttemptCard(att: Attempt) {
    Card(shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(
            Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${att.noAbsen}. ${att.namaSiswa}", style = MaterialTheme.typography.bodyMedium)
                Text(att.status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("%.1f".format(att.nilai), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = Success)
        }
    }
}
