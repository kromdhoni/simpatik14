package com.ypm14.simpatik.ui.kuis

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
fun KuisScreen(
    onBuatKuis: () -> Unit,
    onDetailKuis: (String) -> Unit,
    viewModel: KuisViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kuis", fontWeight = FontWeight.SemiBold) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onBuatKuis,
                icon = { Icon(Icons.Default.Add, "Buat") },
                text = { Text("Kuis Baru") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.kuisList.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("Belum ada kuis", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("Buat kuis baru untuk memulai", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.kuisList, key = { it.id }) { kuis ->
                    KuisCard(kuis = kuis, onClick = { onDetailKuis(kuis.id) })
                }
            }
        }
    }
}

@Composable
private fun KuisCard(kuis: Kuis, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(kuis.judul, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("${kuis.kelasId} · ${kuis.jumlahSoal} soal", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Text("Durasi: ${kuis.durasi} menit", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (kuis.status) {
                    StatusKuis.AKTIF -> Success.copy(alpha = 0.15f)
                    StatusKuis.DRAFT -> Color(0xFFFF9800).copy(alpha = 0.15f)
                    StatusKuis.SELESAI -> Color(0xFF757575).copy(alpha = 0.15f)
                }
            ) {
                Text(
                    text = when (kuis.status) {
                        StatusKuis.AKTIF -> "Aktif"
                        StatusKuis.DRAFT -> "Draft"
                        StatusKuis.SELESAI -> "Selesai"
                    },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = when (kuis.status) {
                        StatusKuis.AKTIF -> Success
                        StatusKuis.DRAFT -> Color(0xFFFF9800)
                        StatusKuis.SELESAI -> Color(0xFF757575)
                    }
                )
            }
        }
    }
}
