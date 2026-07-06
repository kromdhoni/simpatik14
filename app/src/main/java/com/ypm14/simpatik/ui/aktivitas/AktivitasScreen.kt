package com.ypm14.simpatik.ui.aktivitas

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 9 — Aktivitas Guru
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasScreen(onTambah: () -> Unit) {
    var list by remember { mutableStateOf<List<Aktivitas>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }
    var filterKategori by remember { mutableStateOf<String?>(null) }
    val kategoriList = listOf(null, "Mengajar", "Workshop", "Ekstrakurikuler", "Rapat")

    LaunchedEffect(Unit) {
        list = SimpatikRepo.getAktivitasList(); loading = false
    }

    val filtered = list.filter { a ->
        (searchText.isBlank() || a.aktivitas.contains(searchText, ignoreCase = true) || a.guruNama.contains(searchText, ignoreCase = true)) &&
        (filterKategori == null || a.kategori == filterKategori)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aktivitas", fontWeight = FontWeight.SemiBold) },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah, containerColor = Primary, contentColor = Color.White) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
            // Search
            OutlinedTextField(
                searchText, { searchText = it },
                placeholder = { Text("Cari aktivitas...", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) IconButton({ searchText = "" }) {
                        Icon(Icons.Default.Close, null, tint = TextSecondary)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 12.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Primary.copy(alpha = 0.5f),
                    unfocusedContainerColor = CardWhite,
                    focusedContainerColor = CardWhite
                )
            )

            // Kategori filter
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                kategoriList.forEach { k ->
                    FilterChip(
                        selected = filterKategori == k,
                        onClick = { filterKategori = if (filterKategori == k) null else k },
                        label = { Text(k ?: "Semua", fontSize = 11.sp) },
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
                        Icon(Icons.Default.Assignment, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Belum ada aktivitas", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { a ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                // Icon by kategori
                                val icon = when (a.kategori) {
                                    "Mengajar" -> Icons.Default.School
                                    "Workshop" -> Icons.Default.Handyman
                                    "Ekstrakurikuler" -> Icons.Default.SportsSoccer
                                    else -> Icons.Default.Task
                                }
                                Box(Modifier.size(40.dp).background(Primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center) {
                                    Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(a.aktivitas, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                                    Spacer(Modifier.height(2.dp))
                                    Text(a.guruNama, fontSize = 12.sp, color = TextSecondary)
                                    Text("${a.tanggal} · ${a.jamMulai} - ${a.jamSelesai}", fontSize = 12.sp, color = TextSecondary)
                                }
                                // Status
                                val (sColor, sLabel) = when (a.status) {
                                    "selesai" -> Success to "Selesai"
                                    "berlangsung" -> Warning to "Berlangsung"
                                    else -> Primary to "Direncanakan"
                                }
                                Surface(shape = RoundedCornerShape(8.dp), color = sColor.copy(alpha = 0.12f)) {
                                    Text(sLabel, fontSize = 11.sp, color = sColor, fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
