package com.ypm14.simpatik.ui.pengaturan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch

// ✅ PRD Screen 13 — Pengaturan
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengaturanScreen(onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    var showLogout by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        PengaturanItem("Profil", Icons.Default.Person, "profil"),
        PengaturanItem("Password", Icons.Default.Lock, "password"),
        PengaturanItem("Keamanan", Icons.Default.Security, "keamanan"),
        PengaturanItem("Bahasa", Icons.Default.Language, "bahasa"),
        PengaturanItem("Tema", Icons.Default.DarkMode, "tema"),
        PengaturanItem("Notifikasi", Icons.Default.Notifications, "notifikasi"),
        PengaturanItem("Tentang Aplikasi", Icons.Default.Info, "tentang"),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.SemiBold) },
                
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(Background).padding(20.dp)) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    menuItems.forEachIndexed { i, item ->
                        Row(
                            Modifier.fillMaxWidth().clickable { onNavigate(item.route) }.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(item.icon, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(14.dp))
                            Text(item.label, modifier = Modifier.weight(1f), fontSize = 15.sp, color = TextPrimary)
                            Icon(Icons.Default.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        }
                        if (i < menuItems.size - 1) {
                            HorizontalDivider(color = Background, modifier = Modifier.padding(start = 52.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { showLogout = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Danger)
            ) { Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Keluar") }
        }
    }

    if (showLogout) {
        AlertDialog(
            onDismissRequest = { showLogout = false },
            icon = { Icon(Icons.Default.Logout, null, tint = Danger) },
            title = { Text("Keluar") },
            text = { Text("Yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                TextButton({
                    scope.launch { com.ypm14.simpatik.data.SimpatikRepo.logout() }; showLogout = false; onLogout()
                }) { Text("Keluar", color = Danger) }
            },
            dismissButton = { TextButton({ showLogout = false }) { Text("Batal") } }
        )
    }
}

private data class PengaturanItem(val label: String, val icon: ImageVector, val route: String)
