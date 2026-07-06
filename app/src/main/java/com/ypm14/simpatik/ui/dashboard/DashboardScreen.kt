package com.ypm14.simpatik.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.components.*
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// ✅ PRD Screen 3 — Dashboard (redesigned: premium, airy, modern)
data class MenuGrid(val label: String, val icon: ImageVector, val route: String)

private val menuItems = listOf(
    MenuGrid("Presensi", Icons.Rounded.QrCodeScanner, "presensi"),
    MenuGrid("Aktivitas", Icons.Rounded.Assignment, "aktivitas"),
    MenuGrid("Dokumen", Icons.Rounded.Description, "dokumen"),
    MenuGrid("Guru", Icons.Rounded.School, "daftar_guru"),
    MenuGrid("Siswa", Icons.Rounded.Groups, "siswa"),
    MenuGrid("Kelas", Icons.Rounded.MeetingRoom, "kelas"),
    MenuGrid("Keuangan", Icons.Rounded.AccountBalanceWallet, "keuangan"),
    MenuGrid("Inventaris", Icons.Rounded.Inventory2, "inventaris"),
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashboardScreen(onNavigate: (String) -> Unit) {
    val repo = SimpatikRepo
    var ringkasan by remember { mutableStateOf(RingkasanPresensi()) }
    var user by remember { mutableStateOf<User?>(null) }
    var aktivitasList by remember { mutableStateOf(emptyList<Aktivitas>()) }
    var pengumumanList by remember { mutableStateOf(emptyList<Pengumuman>()) }

    LaunchedEffect(Unit) {
        user = repo.currentUser()
        ringkasan = repo.getPresensiRingkasan("")
        aktivitasList = repo.getAktivitasList()
        pengumumanList = repo.getPengumumanList()
    }

    val tanggal = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date())
    }

    val attendanceTrend = remember {
        listOf("Sen" to 12f, "Sel" to 14f, "Rab" to 11f, "Kam" to 15f, "Jum" to 13f, "Sab" to 8f)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF34D399).copy(alpha = 0.06f), Background))).padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 48.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // ── Card Ringkasan Presensi (glass) ──
        item {
            Box(Modifier.clip(RoundedCornerShape(20.dp))) {
                Box(
                    Modifier.matchParentSize()
                        .background(Brush.verticalGradient(listOf(Primary.copy(alpha = 0.12f), Primary.copy(alpha = 0.04f), Color.Transparent)))
                        .blur(radius = 30.dp)
                )
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(0.5.dp, Primary.copy(alpha = 0.1f)),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite.copy(alpha = 0.08f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Selamat pagi 👋", style = MaterialTheme.typography.displayLarge, color = TextPrimary)
                                Text("Ringkasan Presensi Hari Ini", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Spacer(Modifier.weight(1f))
                            Surface(shape = RoundedCornerShape(8.dp), color = Primary.copy(alpha = 0.1f)) {
                                Text("Lihat", fontSize = 12.sp, color = Primary, fontWeight = FontWeight.Medium,
                                    modifier = Modifier.clickable { onNavigate("presensi") }.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("${(ringkasan.hadir * 100 / (ringkasan.hadir + ringkasan.izin + ringkasan.sakit + ringkasan.alpha).coerceAtLeast(1))}%", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Primary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            PresensiStat("Hadir", ringkasan.hadir, Success)
                            PresensiStat("Izin", ringkasan.izin, Warning)
                            PresensiStat("Sakit", ringkasan.sakit, Info)
                            PresensiStat("Alpha", ringkasan.alpha, Danger)
                        }
                    }
                }
            }
        }

        // ── Quick Action ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(
                        Triple("Presensi", Icons.Rounded.QrCodeScanner, "presensi"),
                        Triple("Izin", Icons.Rounded.EventBusy, ""),
                        Triple("Jadwal", Icons.Rounded.CalendarMonth, "jadwal")
                    ).forEach { (l, i, r) ->
                        Column(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Primary.copy(alpha = 0.06f)).clickable { onNavigate(r) },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(i, null, tint = Primary, modifier = Modifier.size(24.dp).padding(top = 12.dp))
                            Text(l, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, modifier = Modifier.padding(bottom = 12.dp, top = 4.dp))
                        }
                    }
                }
            }
        }

        // ── Grafik Kehadiran ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Grafik Kehadiran", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    val maxVal = attendanceTrend.maxOf { it.second }
                    Canvas(Modifier.fillMaxWidth().height(140.dp)) {
                        val barSpacing = size.width / attendanceTrend.size
                        attendanceTrend.forEachIndexed { i, (_, v) ->
                            val barH = (v / maxVal * (size.height - 20.dp.toPx())).coerceAtLeast(4.dp.toPx())
                            val x = i * barSpacing + barSpacing * 0.15f
                            val bw = barSpacing * 0.7f
                            drawRoundRect(
                                color = Success.copy(alpha = 0.7f),
                                topLeft = Offset(x, size.height - barH),
                                size = Size(bw, barH),
                                cornerRadius = CornerRadius(bw / 2, bw / 2)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        attendanceTrend.forEach { (day, _) ->
                            Text(day, fontSize = 11.sp, color = TextTertiary, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

        // ── Aktivitas Terbaru ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Aktivitas Terbaru", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    if (aktivitasList.isEmpty()) {
                        Text("Belum ada aktivitas", fontSize = 13.sp, color = TextTertiary)
                    } else {
                        aktivitasList.take(5).forEachIndexed { i, a ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(Primary.copy(alpha = 0.3f)))
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(a.aktivitas, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                    Spacer(Modifier.height(2.dp))
                                    Text("${a.guruNama} • ${a.tanggal} ${a.jamMulai.take(5)}", fontSize = 11.sp, color = TextTertiary)
                                }
                            }
                            if (i < aktivitasList.lastIndex) Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        // ── Pengumuman ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Pengumuman", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    if (pengumumanList.isEmpty()) {
                        Text("Belum ada pengumuman", fontSize = 13.sp, color = TextTertiary)
                    } else {
                        pengumumanList.take(5).forEachIndexed { i, p ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(if (p.isNew) Primary else TextTertiary.copy(alpha = 0.3f)).offset(y = 4.dp))
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(p.judul, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                    Spacer(Modifier.height(2.dp))
                                    Text(p.isi.take(60) + if (p.isi.length > 60) "..." else "", fontSize = 11.sp, color = TextSecondary)
                                    Spacer(Modifier.height(2.dp))
                                    Text("${p.tanggal} • ${p.kategori}", fontSize = 10.sp, color = TextTertiary)
                                }
                            }
                            if (i < pengumumanList.lastIndex) Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        // ── Header ──
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.ArrowBack, null, tint = TextPrimary, modifier = Modifier.size(28.dp))
                Text("SMK YPM 14 Sumobito", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Icon(Icons.Rounded.Notifications, null, tint = TextSecondary, modifier = Modifier.size(28.dp).clickable { onNavigate("notifikasi") })
            }
        }

        // Tanggal
        item {
            Text(tanggal, fontSize = 13.sp, color = TextTertiary, modifier = Modifier.padding(top = 4.dp))
        }

        // ── Grid Menu ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Fitur", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    val gridRows = menuItems.chunked(4)
                    gridRows.forEach { row ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { item ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { onNavigate(item.route) }
                                ) {
                                    Box(
                                        Modifier.size(56.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Primary.copy(alpha = 0.08f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(item.icon, null, tint = Primary, modifier = Modifier.size(28.dp))
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Text(item.label, fontSize = 11.sp, color = TextPrimary, textAlign = TextAlign.Center)
                                }
                                if (row.last() != item) Spacer(Modifier.width(8.dp))
                            }
                            repeat(4 - row.size) { Spacer(Modifier.width(48.dp + 8.dp)) }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }

        // ── Grafik Kehadiran ──
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Grafik Kehadiran", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(Modifier.weight(1f))
                        Text("Minggu ini", fontSize = 12.sp, color = TextTertiary)
                    }
                    Spacer(Modifier.height(12.dp))
                    SimpleLineChart(dataPoints = attendanceTrend, lineColor = Primary)
                }
            }
        }

        // ── Quick Actions ──
        item {
            Text("Aksi Cepat", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionBtn("Presensi", Icons.Rounded.QrCodeScanner, Primary) { onNavigate("tambah_presensi") }
                QuickActionBtn("Aktivitas", Icons.Rounded.Assignment, Success) { onNavigate("tambah_aktivitas") }
                QuickActionBtn("Jurnal", Icons.Rounded.EditNote, Warning) { onNavigate("tambah_jurnal") }
            }
        }
    }
}

@Composable
private fun PresensiStat(label: String, count: Int, color: Color) {
    val animatedCount by animateIntAsState(targetValue = count, animationSpec = tween(durationMillis = 600))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$animatedCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
private fun QuickActionBtn(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(28.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
