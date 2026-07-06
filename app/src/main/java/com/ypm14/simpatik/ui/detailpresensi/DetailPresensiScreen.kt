package com.ypm14.simpatik.ui.detailpresensi

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.detailguru.StatusChip
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch

// ✅ PRD Screen 8 — Detail Presensi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPresensiScreen(presensiId: String, onBack: () -> Unit) {
    var p by remember { mutableStateOf<Presensi?>(null) }
    var loading by remember { mutableStateOf(true) }
    var showDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(presensiId) {
        p = SimpatikRepo.getPresensi(presensiId); loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Presensi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, null) } },
                
                actions = {
                    IconButton({ showDelete = true }) { Icon(Icons.Default.Delete, null, tint = Danger) }
                }
            )
        }
    ) { pad ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
        } else if (p == null) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { Text("Data tidak ditemukan") }
        } else {
            Column(
                Modifier.fillMaxSize().padding(pad).background(Background)
                    .verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status besar
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val icon = when (p!!.status) {
                            "hadir" -> Icons.Default.CheckCircle
                            "izin" -> Icons.Default.EventBusy
                            "sakit" -> Icons.Default.Sick
                            else -> Icons.Default.Cancel
                        }
                        val color = when (p!!.status) {
                            "hadir" -> Success; "izin" -> Warning; "sakit" -> Info
                            else -> Danger
                        }
                        Icon(icon, null, tint = color, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(p!!.guruNama, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        StatusChip(p!!.status)
                    }
                }

                // Detail
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        DetailRow(Icons.Default.CalendarToday, "Tanggal", p!!.tanggal)
                        HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                        DetailRow(Icons.Default.Schedule, "Jam Masuk", p!!.jamMasuk.ifEmpty { "-" })
                        HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                        DetailRow(Icons.Default.Schedule, "Jam Pulang", p!!.jamPulang.ifEmpty { "-" })
                        HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                        DetailRow(Icons.Default.LocationOn, "Lokasi", p!!.lokasi.ifEmpty { "-" })
                        HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                        DetailRow(Icons.Default.Notes, "Catatan", p!!.catatan.ifEmpty { "-" })
                    }
                }

                // Actions
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { /* TODO: edit */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Edit") }
                    Button(
                        onClick = { showDelete = true },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Danger)
                    ) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("Hapus") }
                }
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Hapus Presensi") },
            text = { Text("Yakin ingin menghapus data presensi ini?") },
            confirmButton = {
                TextButton({
                    scope.launch { SimpatikRepo.deletePresensi(presensiId) }; showDelete = false; onBack()
                }) { Text("Hapus", color = Danger) }
            },
            dismissButton = { TextButton({ showDelete = false }) { Text("Batal") } }
        )
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Text(value, fontSize = 15.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}
