package com.ypm14.simpatik.ui.pengumuman

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

// ✅ PRD Screen 10 — Pengumuman
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengumumanScreen(onTambah: () -> Unit) {
    var list by remember { mutableStateOf<List<Pengumuman>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var filterKat by remember { mutableStateOf<String?>(null) }
    val kategoriList = listOf("Semua", "Guru", "Siswa", "Akademik", "Keuangan")

    LaunchedEffect(Unit) {
        list = SimpatikRepo.getPengumumanList(); loading = false
    }

    val filtered = if (filterKat == null || filterKat == "Semua") list
    else list.filter { it.kategori == filterKat }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengumuman", fontWeight = FontWeight.SemiBold) },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah, containerColor = Primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
            // Kategori filter
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                kategoriList.forEach { k ->
                    FilterChip(
                        selected = filterKat == k || (filterKat == null && k == "Semua"),
                        onClick = { filterKat = if (k == "Semua") null else k },
                        label = { Text(k, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary.copy(alpha = 0.12f),
                            selectedLabelColor = Primary
                        )
                    )
                }
            }

            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
            } else if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Campaign, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Belum ada pengumuman", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { pm ->
                        Card(
                            onClick = { /* TODO: detail */ },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(pm.judul, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary,
                                            maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                        if (pm.isNew) {
                                            Spacer(Modifier.width(6.dp))
                                            Surface(shape = RoundedCornerShape(6.dp), color = Danger) {
                                                Text("Baru", fontSize = 9.sp, color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(pm.isi, fontSize = 13.sp, color = TextSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Spacer(Modifier.height(6.dp))
                                    Row {
                                        // kategori badge
                                        Surface(shape = RoundedCornerShape(6.dp), color = Primary.copy(alpha = 0.1f)) {
                                            Text(pm.kategori, fontSize = 10.sp, color = Primary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text(pm.tanggal, fontSize = 11.sp, color = TextSecondary)
                                    }
                                }
                                Icon(Icons.Default.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
