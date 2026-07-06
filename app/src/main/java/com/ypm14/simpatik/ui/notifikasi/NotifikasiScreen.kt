package com.ypm14.simpatik.ui.notifikasi

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 11 — Notifikasi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen() {
    var list by remember { mutableStateOf<List<Notifikasi>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var unreadOnly by remember { mutableStateOf(false) }
    var filterJenis by remember { mutableStateOf<String?>(null) }
    val jenisList = listOf(null, "PRESENSI", "AKTIVITAS", "PENGUMUMAN", "REMINDER")

    LaunchedEffect(Unit) {
        list = SimpatikRepo.getNotifikasiList(); loading = false
    }

    val filtered = list.filter { n ->
        (!unreadOnly || !n.sudahDibaca) &&
        (filterJenis == null || n.jenis == filterJenis)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi", fontWeight = FontWeight.SemiBold) },
                
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
            // Filter: Semua / Belum Dibaca
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !unreadOnly,
                    onClick = { unreadOnly = false },
                    label = { Text("Semua", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary.copy(alpha = 0.12f), selectedLabelColor = Primary)
                )
                FilterChip(
                    selected = unreadOnly,
                    onClick = { unreadOnly = true },
                    label = { Text("Belum Dibaca", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary.copy(alpha = 0.12f), selectedLabelColor = Primary)
                )
            }

            // Filter jenis
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                jenisList.forEach { j ->
                    FilterChip(
                        selected = filterJenis == j,
                        onClick = { filterJenis = if (filterJenis == j) null else j },
                        label = { Text(j ?: "Semua", fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Primary.copy(alpha = 0.12f), selectedLabelColor = Primary)
                    )
                }
            }

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
            } else if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NotificationsNone, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Tidak ada notifikasi", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filtered) { n ->
                        Card(
                            onClick = { /* TODO: open link */ },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (n.sudahDibaca) CardWhite else CardWhite
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (n.sudahDibaca) 0.dp else 1.dp)
                        ) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                // Icon by jenis
                                val icon = when (n.jenis) {
                                    "PRESENSI" -> Icons.Default.QrCodeScanner
                                    "AKTIVITAS" -> Icons.Default.Assignment
                                    "PENGUMUMAN" -> Icons.Default.Campaign
                                    else -> Icons.Default.Notifications
                                }
                                val iconColor = when (n.jenis) {
                                    "PRESENSI" -> Success
                                    "AKTIVITAS" -> Primary
                                    "PENGUMUMAN" -> Warning
                                    else -> Info
                                }
                                Box(Modifier.size(40.dp).background(iconColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center) {
                                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(n.judul, fontWeight = if (!n.sudahDibaca) FontWeight.SemiBold else FontWeight.Normal,
                                            fontSize = 14.sp, color = TextPrimary)
                                        if (!n.sudahDibaca) {
                                            Spacer(Modifier.width(6.dp))
                                            Box(Modifier.size(8.dp).background(Danger, RoundedCornerShape(4.dp)))
                                        }
                                    }
                                    Text(n.isi, fontSize = 12.sp, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(n.waktu, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
