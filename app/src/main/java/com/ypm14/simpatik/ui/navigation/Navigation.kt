package com.ypm14.simpatik.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ypm14.simpatik.ui.aktivitas.AktivitasScreen
import com.ypm14.simpatik.ui.daftarguru.DaftarGuruScreen
import com.ypm14.simpatik.ui.dashboard.DashboardScreen
import com.ypm14.simpatik.ui.detailguru.DetailGuruScreen
import com.ypm14.simpatik.ui.detailpresensi.DetailPresensiScreen
import com.ypm14.simpatik.ui.dokumen.DokumenScreen
import com.ypm14.simpatik.ui.form.FormTambahDataScreen
import com.ypm14.simpatik.ui.jadwal.JadwalDetailScreen
import com.ypm14.simpatik.ui.jadwal.JadwalScreen
import com.ypm14.simpatik.ui.jurnal.JurnalScreen
import com.ypm14.simpatik.ui.kuis.BuatKuisScreen
import com.ypm14.simpatik.ui.kuis.BuatSoalScreen
import com.ypm14.simpatik.ui.kuis.DetailKuisScreen
import com.ypm14.simpatik.ui.kuis.KuisScreen
import com.ypm14.simpatik.ui.login.LoginScreen
import com.ypm14.simpatik.ui.notifikasi.NotifikasiScreen
import com.ypm14.simpatik.ui.pengaturan.PengaturanScreen
import com.ypm14.simpatik.ui.pengumuman.PengumumanScreen
import com.ypm14.simpatik.ui.presensi.PresensiScreen
import com.ypm14.simpatik.ui.profil.ProfilScreen
import com.ypm14.simpatik.ui.profilsekolah.ProfilSekolahScreen
import com.ypm14.simpatik.ui.riwayat.RiwayatScreen
import com.ypm14.simpatik.ui.splash.SplashScreen
import com.ypm14.simpatik.ui.statistik.StatistikScreen
import com.ypm14.simpatik.ui.tentang.TentangScreen
import com.ypm14.simpatik.ui.theme.*

data class BottomTab(val label: String, val icon: ImageVector, val outlineIcon: ImageVector, val route: String)

private val tabs = listOf(
    BottomTab("Beranda", Icons.Default.Home, Icons.Outlined.Home, Routes.DASHBOARD),
    BottomTab("Jadwal", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth, Routes.JADWAL),
    BottomTab("Presensi", Icons.Default.QrCodeScanner, Icons.Outlined.QrCodeScanner, Routes.PRESENSI),
    BottomTab("Kuis", Icons.Default.Quiz, Icons.Outlined.Quiz, Routes.KUIS),
    BottomTab("Profil", Icons.Default.Person, Icons.Outlined.Person, Routes.PROFIL),
)
private val tabRoutes = tabs.map { it.route }

@Composable
fun Navigation() {
    var isLoggedIn by remember { mutableStateOf(false) }
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route
    val showBar = isLoggedIn && current in tabRoutes

    Scaffold(bottomBar = {
        AnimatedVisibility(visible = showBar && isLoggedIn, enter = fadeIn(), exit = fadeOut()) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 4.dp,
                color = BottomNavBg
            ) {
                NavigationBar(containerColor = Color.Transparent, tonalElevation = 0.dp) {
                    tabs.forEach { tab ->
                        val sel = backStack?.destination?.hierarchy?.any { it.route == tab.route } == true
                        val capsuleBg by animateColorAsState(if (sel) Primary.copy(alpha = 0.12f) else Color.Transparent, label = "capsule")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    if (sel) return@clickable
                                    nav.navigate(tab.route) {
                                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true; restoreState = true
                                    }
                                }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(capsuleBg)
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(if (sel) tab.icon else tab.outlineIcon, null, tint = if (sel) BottomNavSelected else BottomNavUnselected, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.height(4.dp))
                                Text(tab.label, color = if (sel) BottomNavSelected else BottomNavUnselected,
                                    fontSize = 11.sp, fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }
    }) { pad ->
        NavHost(nav, startDestination = Routes.SPLASH, Modifier.padding(pad)) {

            // ── SPLASH ──
            composable(Routes.SPLASH) {
                SplashScreen(onNavigate = {
                    if (isLoggedIn) nav.navigate(Routes.DASHBOARD) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    else nav.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } }
                })
            }

            // ── LOGIN ──
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        isLoggedIn = true
                        nav.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    },
                    onRegister = { nav.navigate(Routes.formTambah("guru")) }
                )
            }

            // ── DASHBOARD ──
            composable(Routes.DASHBOARD) {
                DashboardScreen(onNavigate = { nav.navigate(it) })
            }

            // ── JADWAL ──
            composable(Routes.JADWAL) {
                JadwalScreen(
                    onJadwalClick = { nav.navigate(Routes.detailJadwal(it)) }
                )
            }
            composable(Routes.JADWAL_DETAIL, arguments = listOf(navArgument("jadwalId") { type = NavType.StringType })) {
                JadwalDetailScreen(
                    jadwalId = it.arguments?.getString("jadwalId") ?: "",
                    onBack = { nav.popBackStack() },
                    onPresensi = { /* TODO: link ke presensi */ },
                    onJurnal = { presensiId, jadwalId -> nav.navigate(Routes.jurnal(presensiId, jadwalId)) }
                )
            }

            // ── JURNAL ──
            composable(Routes.JURNAL, arguments = listOf(
                navArgument("presensiId") { type = NavType.StringType },
                navArgument("jadwalId") { type = NavType.StringType }
            )) {
                JurnalScreen(
                    presensiId = it.arguments?.getString("presensiId") ?: "",
                    jadwalId = it.arguments?.getString("jadwalId") ?: "",
                    onBack = { nav.popBackStack() },
                    onSaved = { nav.popBackStack() }
                )
            }

            // ── KUIS ──
            composable(Routes.KUIS) {
                KuisScreen(
                    onBuatKuis = { nav.navigate(Routes.BUAT_KUIS) },
                    onDetailKuis = { nav.navigate(Routes.detailKuis(it)) }
                )
            }
            composable(Routes.BUAT_KUIS) {
                BuatKuisScreen(
                    onKuisCreated = { kuisId -> nav.navigate(Routes.buatSoal(kuisId)) },
                    onBack = { nav.popBackStack() }
                )
            }
            composable(Routes.BUAT_SOAL, arguments = listOf(navArgument("kuisId") { type = NavType.StringType })) {
                BuatSoalScreen(
                    kuisId = it.arguments?.getString("kuisId") ?: "",
                    onBack = { nav.popBackStack() },
                    onSelesai = { nav.navigate(Routes.DETAIL_KUIS.replace("{kuisId}", it.arguments?.getString("kuisId") ?: "")) { popUpTo(Routes.KUIS) } }
                )
            }
            composable(Routes.DETAIL_KUIS, arguments = listOf(navArgument("kuisId") { type = NavType.StringType })) {
                DetailKuisScreen(
                    kuisId = it.arguments?.getString("kuisId") ?: "",
                    onBack = { nav.popBackStack() },
                    onBuatSoal = { nav.navigate(Routes.buatSoal(it)) }
                )
            }

            // ── RIWAYAT ──
            composable(Routes.RIWAYAT) {
                RiwayatScreen(onDetailKuis = { nav.navigate(Routes.detailKuis(it)) })
            }

            // ── PROFIL ──
            composable(Routes.PROFIL) {
                ProfilScreen(
                    onLogout = {
                        isLoggedIn = false
                        nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    },
                    onPengaturan = { nav.navigate(Routes.PENGATURAN) },
                    onTentang = { nav.navigate(Routes.TENTANG) }
                )
            }

            // ── STATISTIK ──
            composable(Routes.STATISTIK) { StatistikScreen() }

            // ── DAFTAR GURU ──
            composable(Routes.DAFTAR_GURU) {
                DaftarGuruScreen(
                    onDetailGuru = { nav.navigate(Routes.detailGuru(it)) },
                    onTambah = { nav.navigate(Routes.formTambah("guru")) }
                )
            }
            composable(Routes.DETAIL_GURU, arguments = listOf(navArgument("guruId") { type = NavType.StringType })) {
                DetailGuruScreen(guruId = it.arguments?.getString("guruId") ?: "", onBack = { nav.popBackStack() })
            }

            // ── PRESENSI ──
            composable(Routes.PRESENSI) {
                PresensiScreen(
                    onDetail = { nav.navigate(Routes.detailPresensi(it)) },
                    onTambah = { nav.navigate(Routes.formTambah("presensi")) }
                )
            }
            composable(Routes.DETAIL_PRESENSI, arguments = listOf(navArgument("presensiId") { type = NavType.StringType })) {
                DetailPresensiScreen(presensiId = it.arguments?.getString("presensiId") ?: "", onBack = { nav.popBackStack() })
            }

            // ── AKTIVITAS ──
            composable(Routes.AKTIVITAS) {
                AktivitasScreen(onTambah = { nav.navigate(Routes.formTambah("aktivitas")) })
            }

            // ── PENGUMUMAN ──
            composable(Routes.PENGUMUMAN) {
                PengumumanScreen(onTambah = { nav.navigate(Routes.formTambah("pengumuman")) })
            }

            // ── NOTIFIKASI ──
            composable(Routes.NOTIFIKASI) { NotifikasiScreen() }

            // ── PROFIL SEKOLAH ──
            composable(Routes.PROFIL_SEKOLAH) { ProfilSekolahScreen(onBack = { nav.popBackStack() }) }

            // ── PENGATURAN ──
            composable(Routes.PENGATURAN) {
                PengaturanScreen(
                    onNavigate = { route ->
                        when (route) {
                            "tentang" -> nav.navigate(Routes.TENTANG)
                            "profil" -> nav.navigate(Routes.PROFIL_SEKOLAH)
                            else -> nav.navigate(route)
                        }
                    },
                    onLogout = {
                        isLoggedIn = false
                        nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    }
                )
            }

            // ── TENTANG ──
            composable(Routes.TENTANG) { TentangScreen(onBack = { nav.popBackStack() }) }

            // ── DOKUMEN ──
            composable(Routes.DOKUMEN) {
                DokumenScreen(onTambah = { nav.navigate(Routes.formTambah("dokumen")) })
            }

            // ── FORM TAMBAH DATA ──
            composable(Routes.FORM_TAMBAH, arguments = listOf(navArgument("formType") { type = NavType.StringType })) {
                val ft = it.arguments?.getString("formType") ?: "guru"
                FormTambahDataScreen(
                    formType = ft,
                    onBack = { nav.popBackStack() },
                    onSaved = { nav.popBackStack() }
                )
            }
        }
    }
}
