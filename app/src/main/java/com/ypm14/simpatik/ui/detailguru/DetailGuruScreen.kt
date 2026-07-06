package com.ypm14.simpatik.ui.detailguru

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 6 — Detail Guru + Tabs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailGuruScreen(guruId: String, onBack: () -> Unit) {
    var guru by remember { mutableStateOf<Guru?>(null) }
    var tab by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(guruId) {
        guru = SimpatikRepo.getGuru(guruId); loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Guru", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, null) } },
                
            )
        }
    ) { pad ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
        } else if (guru == null) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { Text("Guru tidak ditemukan") }
        } else {
            val g = guru!!
            Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
                // Header
                Surface(color = Primary, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center) {
                            Text(g.nama.take(2).uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(g.nama, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(g.mapel, fontSize = 14.sp, color = Color.White.copy(alpha = 0.85f))
                        Spacer(Modifier.height(4.dp))

                        // Status badge
                        Surface(shape = RoundedCornerShape(10.dp),
                            color = if (g.statusAktif) Color.White.copy(alpha = 0.2f) else Danger.copy(alpha = 0.6f)) {
                            Text(
                                if (g.statusAktif) "Aktif" else "Nonaktif",
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Tabs
                TabRow(
                    selectedTabIndex = tab,
                    containerColor = CardWhite,
                    contentColor = Primary,
                ) {
                    Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Profil", fontSize = 13.sp) })
                    Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Presensi", fontSize = 13.sp) })
                    Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("Aktivitas", fontSize = 13.sp) })
                }

                // Content
                when (tab) {
                    0 -> ProfilTab(g)
                    1 -> PresensiTab(g.uid, g.nama)
                    2 -> AktivitasTab(g.uid, g.nama)
                }
            }
        }
    }
}

@Composable
private fun ProfilTab(g: Guru) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
            Column(Modifier.padding(16.dp)) {
                InfoRow(Icons.Default.Badge, "NIP", g.nip)
                HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                InfoRow(Icons.Default.LocationOn, "Alamat", g.alamat)
                HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                InfoRow(Icons.Default.Phone, "No. HP", g.noHp)
                HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                InfoRow(Icons.Default.Email, "Email", g.email)
                HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                InfoRow(Icons.Default.School, "Pendidikan", g.pendidikan)
            }
        }
    }
}

@Composable
private fun PresensiTab(guruId: String, guruNama: String) {
    var list by remember { mutableStateOf<List<Presensi>>(emptyList()) }
    LaunchedEffect(guruId) { list = SimpatikRepo.getPresensiList(filterGuruId = guruId) }

    if (list.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada presensi", color = TextSecondary) }
    } else {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            list.take(5).forEach { p ->
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, null, tint = Primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.tanggal, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                            Text("${p.jamMasuk} - ${p.jamPulang}", fontSize = 12.sp, color = TextSecondary)
                        }
                        StatusChip(p.status)
                    }
                }
            }
        }
    }
}

@Composable
private fun AktivitasTab(guruId: String, guruNama: String) {
    var list by remember { mutableStateOf<List<Aktivitas>>(emptyList()) }
    LaunchedEffect(guruId) { list = SimpatikRepo.getAktivitasList(filterGuruId = guruId) }

    if (list.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada aktivitas", color = TextSecondary) }
    } else {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            list.take(5).forEach { a ->
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, null, tint = Primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(a.aktivitas, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                            Text("${a.tanggal} · ${a.jamMulai} - ${a.jamSelesai}", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Text(value.ifEmpty { "-" }, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (color, label) = when (status) {
        "hadir" -> Success to "Hadir"
        "izin" -> Warning to "Izin"
        "sakit" -> Info to "Sakit"
        else -> Danger to "Alpha"
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.12f)) {
        Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
    }
}
