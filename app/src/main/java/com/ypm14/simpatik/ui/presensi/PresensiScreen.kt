package com.ypm14.simpatik.ui.presensi

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.components.*
import com.ypm14.simpatik.ui.detailguru.StatusChip
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 7 — Presensi (list + ringkasan)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresensiScreen(onDetail: (String) -> Unit, onTambah: () -> Unit) {
    val repo = SimpatikRepo
    var list by remember { mutableStateOf<List<Presensi>>(emptyList()) }
    var ringkasan by remember { mutableStateOf(RingkasanPresensi()) }
    var loading by remember { mutableStateOf(true) }
    var filterTanggal by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        list = repo.getPresensiList(); ringkasan = repo.getPresensiRingkasan(""); loading = false
    }

    val filtered = list.filter { p ->
        (filterTanggal.isBlank() || p.tanggal == filterTanggal) &&
        (filterStatus == null || p.status == filterStatus)
    }

    val pieSlices = remember {
        listOf(
            PieSlice("Hadir", ringkasan.hadir.toFloat(), Success),
            PieSlice("Izin", ringkasan.izin.toFloat(), Warning),
            PieSlice("Sakit", ringkasan.sakit.toFloat(), Info),
            PieSlice("Alpha", ringkasan.alpha.toFloat(), Danger),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Presensi", fontWeight = FontWeight.SemiBold) },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah, containerColor = Primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(pad).background(Background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Ringkasan ──
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Ringkasan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            RingkasanItem("Hadir", ringkasan.hadir, Success)
                            RingkasanItem("Izin", ringkasan.izin, Warning)
                            RingkasanItem("Sakit", ringkasan.sakit, Info)
                            RingkasanItem("Alpha", ringkasan.alpha, Danger)
                        }
                    }
                }
            }

            // ── Grafik ──
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        SimplePieChart(slices = pieSlices, modifier = Modifier.weight(1f), size = 100.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            pieSlices.forEach { s ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(8.dp).background(s.color, RoundedCornerShape(2.dp)))
                                    Spacer(Modifier.width(6.dp))
                                    Text("${s.label} ${if (ringkasan.hadir > 0) (s.value / (ringkasan.hadir + ringkasan.izin + ringkasan.sakit + ringkasan.alpha).coerceAtLeast(1)) * 100 else 0}%",
                                        fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // ── Filter ──
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Status filter
                    listOf(null, "hadir", "izin", "sakit", "alpha").forEach { s ->
                        FilterChip(
                            selected = filterStatus == s,
                            onClick = { filterStatus = if (filterStatus == s) null else s },
                            label = { Text(s?.replaceFirstChar { it.uppercase() } ?: "Semua", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary.copy(alpha = 0.12f),
                                selectedLabelColor = Primary
                            )
                        )
                    }
                }
            }

            // ── List Riwayat ──
            if (loading) {
                item { Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) } }
            } else if (filtered.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Data presensi tidak ditemukan", color = TextSecondary)
                        }
                    }
                }
            } else {
                items(filtered) { p ->
                    Card(
                        onClick = { onDetail(p.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(p.guruNama, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                                Spacer(Modifier.height(2.dp))
                                Text(p.tanggal, fontSize = 12.sp, color = TextSecondary)
                                Text("${p.jamMasuk} - ${p.jamPulang} · ${p.lokasi}", fontSize = 12.sp, color = TextSecondary)
                            }
                            StatusChip(p.status)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RingkasanItem(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$count", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}
