package com.ypm14.simpatik.ui.daftarguru

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

// ✅ PRD Screen 5 — Daftar Guru
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarGuruScreen(onDetailGuru: (String) -> Unit, onTambah: () -> Unit) {
    var list by remember { mutableStateOf<List<Guru>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }
    var filterAktif by remember { mutableStateOf<String?>(null) } // null = semua

    LaunchedEffect(Unit) {
        list = SimpatikRepo.getGuruList(); loading = false
    }

    val filtered = list.filter { g ->
        (searchText.isBlank() || g.nama.contains(searchText, ignoreCase = true) || g.nip.contains(searchText)) &&
        (filterAktif == null || (filterAktif == "aktif" && g.statusAktif) || (filterAktif == "nonaktif" && !g.statusAktif))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Guru", fontWeight = FontWeight.SemiBold) },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah, containerColor = Primary,
                contentColor = Color.White) { Icon(Icons.Default.Add, null) }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background)) {
            // Search bar
            OutlinedTextField(
                searchText, { searchText = it },
                placeholder = { Text("Cari guru...", fontSize = 14.sp) },
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

            // Filter chips
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(null to "Semua", "aktif" to "Aktif", "nonaktif" to "Nonaktif").forEach { (key, label) ->
                    FilterChip(
                        selected = filterAktif == key,
                        onClick = { filterAktif = if (key == filterAktif) null else key },
                        label = { Text(label, fontSize = 12.sp) },
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
                        Icon(Icons.Default.SearchOff, null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Guru tidak ditemukan", color = TextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { guru ->
                        Card(
                            onClick = { onDetailGuru(guru.uid) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                // Avatar
                                Box(Modifier.size(48.dp).clip(CircleShape).background(Primary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center) {
                                    Text(guru.nama.take(2).uppercase(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(guru.nama, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                                    Text(guru.nip, fontSize = 12.sp, color = TextSecondary)
                                    Text(guru.mapel, fontSize = 12.sp, color = TextSecondary)
                                }
                                // Status
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (guru.statusAktif) Success.copy(alpha = 0.12f) else Danger.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        if (guru.statusAktif) "Aktif" else "Nonaktif",
                                        fontSize = 11.sp, fontWeight = FontWeight.Medium,
                                        color = if (guru.statusAktif) Success else Danger,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Icon(Icons.Default.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
