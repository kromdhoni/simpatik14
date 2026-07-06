package com.ypm14.simpatik.ui.dokumen

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

// ✅ PRD — Informasi Dokumen Pembelajaran
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DokumenScreen(onTambah: () -> Unit) {
    var list by remember { mutableStateOf<List<Dokumen>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var filterJenis by remember { mutableStateOf<String?>(null) }
    val jenisList = listOf(null, "Silabus", "RPP", "Modul Ajar")

    LaunchedEffect(Unit) {
        list = SimpatikRepo.getDokumenList(); loading = false
    }

    val filtered = list.filter { d ->
        filterJenis == null || d.jenis == filterJenis
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informasi Dokumen", fontWeight = FontWeight.SemiBold) },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah, containerColor = Primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
            // Filter chips
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                jenisList.forEach { j ->
                    FilterChip(
                        selected = filterJenis == j,
                        onClick = { filterJenis = if (filterJenis == j) null else j },
                        label = { Text(j ?: "Semua", fontSize = 12.sp) },
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
                        Icon(Icons.Default.Description, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Belum ada dokumen", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { d ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                val icon = when (d.jenis) {
                                    "Silabus" -> Icons.Default.MenuBook
                                    "RPP" -> Icons.Default.ChromeReaderMode
                                    "Modul Ajar" -> Icons.Default.AutoStories
                                    else -> Icons.Default.Description
                                }
                                Box(Modifier.size(40.dp).background(Primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center) {
                                    Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(d.judul, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                                    Spacer(Modifier.height(2.dp))
                                    Text("${d.mapel} · ${d.author}", fontSize = 12.sp, color = TextSecondary)
                                    Text(d.tanggal, fontSize = 12.sp, color = TextSecondary)
                                    if (d.keterangan.isNotEmpty()) {
                                        Text(d.keterangan, fontSize = 11.sp, color = TextSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Surface(shape = RoundedCornerShape(6.dp), color = Primary.copy(alpha = 0.1f)) {
                                        Text(d.jenis, fontSize = 10.sp, color = Primary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
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
