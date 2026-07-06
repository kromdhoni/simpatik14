package com.ypm14.simpatik.ui.tentang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.ui.theme.*

// ✅ PRD Screen 14 — Tentang Aplikasi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TentangScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tentang Aplikasi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, null) } },
                
            )
        }
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).background(Background).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            // Logo
            Box(Modifier.size(80.dp).clip(RoundedCornerShape(20.dp)).background(Primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center) {
                Icon(Icons.Default.School, null, tint = Primary, modifier = Modifier.size(44.dp))
            }
            Spacer(Modifier.height(12.dp))

            Text("SIMPATIK 14", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Sistem Informasi Manajemen\nPresensi & Aktivitas Guru Terintegrasi",
                fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center, lineHeight = 18.sp)
            Spacer(Modifier.height(8.dp))

            Text("Versi 1.0.0", fontSize = 13.sp, color = TextPrimary.copy(alpha = 0.5f))

            Spacer(Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    TentangRow("Developer", "Tim SIMPATIK 14")
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), color = Background)
                    TentangRow("Hak Cipta", "© 2025 SMK YPM 14 Sumobito")
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), color = Background)
                    TentangRow("Platform", "Android + Web")
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), color = Background)
                    TentangRow("Lisensi", "MIT License")
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(4.dp)) {
                    TextButton(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) { Text("Kebijakan Privasi", color = TextPrimary) }
                    TextButton(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) { Text("Syarat & Ketentuan", color = TextPrimary) }
                }
            }

            Spacer(Modifier.weight(1f))
            Text("SMK YPM 14 Sumobito", fontSize = 12.sp, color = TextSecondary)
            Text("Built with ❤️ for Education", fontSize = 11.sp, color = TextSecondary)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TentangRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = TextSecondary)
        Text(value, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}
