package com.ypm14.simpatik.ui.profilsekolah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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

// ✅ PRD Screen 12 — Profil Sekolah
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilSekolahScreen(onBack: () -> Unit) {
    var sekolah by remember { mutableStateOf(Sekolah()) }
    LaunchedEffect(Unit) { sekolah = SimpatikRepo.getSekolah() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Sekolah", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, null) } },
                
            )
        }
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).background(Background)
                .verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(Modifier.size(96.dp).clip(RoundedCornerShape(20.dp)).background(Primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center) {
                Icon(Icons.Default.School, null, tint = Primary, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(sekolah.nama, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(sekolah.npsn, fontSize = 14.sp, color = TextSecondary)
            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    ProfilRow(Icons.Default.LocationOn, "Alamat", sekolah.alamat)
                    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                    ProfilRow(Icons.Default.Numbers, "NPSN", sekolah.npsn)
                    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                    ProfilRow(Icons.Default.Language, "Website", sekolah.website)
                    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                    ProfilRow(Icons.Default.Email, "Email", sekolah.email)
                    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                    ProfilRow(Icons.Default.Phone, "Telepon", sekolah.noTelp)
                    HorizontalDivider(Modifier.padding(vertical = 10.dp), color = Background)
                    ProfilRow(Icons.Default.Person, "Kepala Sekolah", sekolah.kepalaSekolah)
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = { /* TODO: edit */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Edit Profil Sekolah") }
        }
    }
}

@Composable
private fun ProfilRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Text(value.ifEmpty { "-" }, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}
