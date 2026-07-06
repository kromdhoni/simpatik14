package com.ypm14.simpatik.ui.statistik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.components.*
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 4 — Statistik
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatistikScreen() {
    var stats by remember { mutableStateOf(StatistikCounts()) }
    LaunchedEffect(Unit) { stats = SimpatikRepo.getStatistik() }

    val pieSlices = remember {
        listOf(
            PieSlice("Hadir", 62f, Success),
            PieSlice("Izin", 12f, Warning),
            PieSlice("Sakit", 8f, Info),
            PieSlice("Alpha", 18f, Danger),
        )
    }

    val lineTrend = remember {
        listOf("Sen" to 45f, "Sel" to 52f, "Rab" to 38f, "Kam" to 55f, "Jum" to 48f, "Sab" to 22f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistik", fontWeight = FontWeight.SemiBold) },
                
            )
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(pad).background(Background).padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── KPI Cards ──
            item {
                Text("Ringkasan", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard("Guru", "${stats.jumlahGuru}", Icons.Default.School, Accent, Modifier.weight(1f))
                    KpiCard("Siswa", "${stats.jumlahSiswa}", Icons.Default.Groups, Primary, Modifier.weight(1f))
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard("Kelas", "${stats.jumlahKelas}", Icons.Default.MeetingRoom, Warning, Modifier.weight(1f))
                    KpiCard("Presensi", "${stats.presensiHariIni}", Icons.Default.QrCodeScanner, Danger, Modifier.weight(1f))
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KpiCard("Aktivitas", "${stats.aktivitasHariIni}", Icons.Default.Assignment, Info, Modifier.weight(1f))
                    KpiCard("Pengumuman", "${stats.pengumumanBaru}", Icons.Default.Campaign, Success, Modifier.weight(1f))
                }
            }

            // ── Pie Chart ──
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Status Kehadiran", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SimplePieChart(slices = pieSlices, modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(16.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                pieSlices.forEach { s ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(Modifier.size(10.dp).background(s.color, RoundedCornerShape(2.dp)))
                                        Spacer(Modifier.width(6.dp))
                                        Text("${s.label} ${(s.value / pieSlices.sumOf { it.value.toDouble() } * 100).toInt()}%",
                                            fontSize = 12.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Line Chart ──
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Tren Kehadiran", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                        Spacer(Modifier.height(8.dp))
                        SimpleLineChart(dataPoints = lineTrend, lineColor = Primary)
                    }
                }
            }

            // ── Progress Bar ──
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Capaian Bulanan", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                        Spacer(Modifier.height(12.dp))
                        ProgressRow("Presensi", 0.82f, Success)
                        Spacer(Modifier.height(8.dp))
                        ProgressRow("Aktivitas", 0.65f, Primary)
                        Spacer(Modifier.height(8.dp))
                        ProgressRow("Jurnal", 0.45f, Warning)
                        Spacer(Modifier.height(8.dp))
                        ProgressRow("Kehadiran Guru", 0.90f, Info)
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(label, fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun ProgressRow(label: String, progress: Float, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 13.sp, color = TextPrimary, modifier = Modifier.width(100.dp))
        SimpleProgressBar(progress = progress, color = color, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
        Text("${(progress * 100).toInt()}%", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(36.dp), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}
