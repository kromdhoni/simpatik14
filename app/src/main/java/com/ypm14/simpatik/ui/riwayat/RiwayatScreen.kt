package com.ypm14.simpatik.ui.riwayat

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ypm14.simpatik.data.Kuis
import com.ypm14.simpatik.data.StatusKuis
import com.ypm14.simpatik.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(
    onDetailKuis: (String) -> Unit,
    viewModel: RiwayatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Presensi", "Jurnal", "Kuis")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab.ordinal == index,
                        onClick = { viewModel.changeTab(TabRiwayat.entries[index]) },
                        text = { Text(title, fontWeight = if (uiState.selectedTab.ordinal == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                when (uiState.selectedTab) {
                    TabRiwayat.PRESENSI -> RiwayatPresensiTab(uiState.presensiItems)
                    TabRiwayat.JURNAL -> RiwayatJurnalTab(uiState.jurnalItems)
                    TabRiwayat.KUIS -> RiwayatKuisTab(uiState.kuisItems, onDetailKuis)
                }
            }
        }
    }
}

@Composable
private fun RiwayatPresensiTab(items: List<RiwayatPresensiItem>) {
    if (items.isEmpty()) {
        EmptyState(icon = Icons.Default.Fingerprint, text = "Belum ada riwayat presensi")
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.id }) { item ->
                Card(shape = RoundedCornerShape(10.dp)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null,
                            tint = Success, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${item.kelasNama} - ${item.mapelNama}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RiwayatJurnalTab(items: List<RiwayatJurnalItem>) {
    if (items.isEmpty()) {
        EmptyState(icon = Icons.Default.MenuBook, text = "Belum ada riwayat jurnal")
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.id }) { item ->
                Card(shape = RoundedCornerShape(10.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(item.materi, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium,
                            maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
private fun RiwayatKuisTab(items: List<Kuis>, onDetail: (String) -> Unit) {
    if (items.isEmpty()) {
        EmptyState(icon = Icons.Default.Quiz, text = "Belum ada riwayat kuis")
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.id }) { kuis ->
                Card(
                    onClick = { onDetail(kuis.id) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(kuis.judul, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(kuis.kelasId, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (kuis.status) {
                                StatusKuis.AKTIF -> Success.copy(alpha = 0.15f)
                                StatusKuis.DRAFT -> Color(0xFFFF9800).copy(alpha = 0.15f)
                                StatusKuis.SELESAI -> Color(0xFF757575).copy(alpha = 0.15f)
                            }
                        ) {
                            Text(
                                when (kuis.status) { StatusKuis.AKTIF -> "Aktif"; StatusKuis.DRAFT -> "Draft"; StatusKuis.SELESAI -> "Selesai" },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
