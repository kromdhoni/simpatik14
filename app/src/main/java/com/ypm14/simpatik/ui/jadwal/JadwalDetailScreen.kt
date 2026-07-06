package com.ypm14.simpatik.ui.jadwal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ypm14.simpatik.data.Jadwal
import com.ypm14.simpatik.data.JurnalStatus
import com.ypm14.simpatik.data.PresensiStatus
import com.ypm14.simpatik.data.SimpatikRepo
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalDetailScreen(
    jadwalId: String,
    onBack: () -> Unit,
    onPresensi: (String) -> Unit,
    onJurnal: (String, String) -> Unit
) {
    var jadwal by remember { mutableStateOf<Jadwal?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(jadwalId) {
        scope.launch {
            val result = SimpatikRepo.getJadwalById(jadwalId)
            jadwal = result.getOrNull()
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Jadwal") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (jadwal == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Jadwal tidak ditemukan")
            }
        } else {
            val j = jadwal!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Info card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DetailRow(icon = Icons.Default.Book, label = "Mata Pelajaran", value = j.mapelNama)
                        DetailRow(icon = Icons.Default.Person, label = "Kelas", value = j.kelasNama)
                        DetailRow(icon = Icons.Default.Schedule, label = "Waktu", value = "${j.jamMulai} - ${j.jamSelesai}")
                        DetailRow(icon = Icons.Default.Room, label = "Ruang", value = j.ruang)
                        DetailRow(icon = Icons.Default.CalendarMonth, label = "Hari", value = j.hari)
                        DetailRow(icon = Icons.Default.Person, label = "Guru", value = j.guruNama)
                    }
                }

                // Action cards
                Text("Aksi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Fingerprint,
                        label = "Presensi",
                        status = if (j.presensiStatus == PresensiStatus.SELESAI) "Selesai" else "Belum",
                        completed = j.presensiStatus == PresensiStatus.SELESAI,
                        onClick = { onPresensi(j.id) }
                    )
                    ActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.MenuBook,
                        label = "Jurnal",
                        status = if (j.jurnalStatus == JurnalStatus.SELESAI) "Selesai" else "Belum",
                        completed = j.jurnalStatus == JurnalStatus.SELESAI,
                        onClick = { onJurnal(j.id, j.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    status: String,
    completed: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(36.dp),
                tint = if (completed) Success else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(status, style = MaterialTheme.typography.labelSmall,
                color = if (completed) Success else MaterialTheme.colorScheme.error)
        }
    }
}
