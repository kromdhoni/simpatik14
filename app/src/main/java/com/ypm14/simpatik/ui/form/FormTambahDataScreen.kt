package com.ypm14.simpatik.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.data.*
import com.ypm14.simpatik.ui.theme.*
import kotlinx.coroutines.launch

// ✅ PRD Screen 15 — Form Tambah Data (multi-purpose)
// type: "guru", "aktivitas", "presensi", "pengumuman"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTambahDataScreen(formType: String = "guru", onBack: () -> Unit, onSaved: () -> Unit) {
    val scope = rememberCoroutineScope()
    var saving by remember { mutableStateOf(false) }

    // Guru fields
    var nama by remember { mutableStateOf("") }
    var nip by remember { mutableStateOf("") }
    var mapel by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }

    // Aktivitas fields
    var aktivitasNama by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Mengajar") }
    var tanggal by remember { mutableStateOf("") }
    var jamMulai by remember { mutableStateOf("") }
    var jamSelesai by remember { mutableStateOf("") }

    // Presensi fields
    var presensiGuru by remember { mutableStateOf("") }
    var statusPresensi by remember { mutableStateOf("hadir") }
    var lokasi by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }

    // Pengumuman fields
    var judul by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }
    var kategoriPm by remember { mutableStateOf("Guru") }

    val title = when (formType) {
        "guru" -> "Tambah Guru"
        "aktivitas" -> "Tambah Aktivitas"
        "presensi" -> "Tambah Presensi"
        "pengumuman" -> "Tambah Pengumuman"
        else -> "Tambah Data"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                
            )
        }
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).background(Background)
                .verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Dropdown — upload foto placeholder
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(Modifier.size(80.dp).background(Primary.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AddPhotoAlternate, "Tambah foto", tint = Primary, modifier = Modifier.size(32.dp))
                }
            }
            Text("Upload Foto", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            when (formType) {
                "guru" -> {
                    FormField(nama, { nama = it }, "Nama Lengkap")
                    FormField(nip, { nip = it }, "NIP")
                    FormField(mapel, { mapel = it }, "Mata Pelajaran")
                    FormField(email, { email = it }, "Email")
                    FormField(noHp, { noHp = it }, "No. HP")
                }
                "aktivitas" -> {
                    FormField(aktivitasNama, { aktivitasNama = it }, "Nama Aktivitas")
                    FormDropdown(kategori, { kategori = it }, "Kategori", listOf("Mengajar", "Workshop", "Ekstrakurikuler", "Rapat"))
                    FormField(tanggal, { tanggal = it }, "Tanggal", placeholder = "dd/MM/yyyy")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FormField(jamMulai, { jamMulai = it }, "Jam Mulai", placeholder = "07:30", modifier = Modifier.weight(1f))
                        FormField(jamSelesai, { jamSelesai = it }, "Jam Selesai", placeholder = "14:00", modifier = Modifier.weight(1f))
                    }
                }
                "presensi" -> {
                    FormField(presensiGuru, { presensiGuru = it }, "Nama Guru")
                    FormField(tanggal, { tanggal = it }, "Tanggal", placeholder = "dd/MM/yyyy")
                    FormDropdown(statusPresensi, { statusPresensi = it }, "Status", listOf("hadir", "izin", "sakit", "alpha"))
                    FormField(lokasi, { lokasi = it }, "Lokasi")
                    FormField(catatan, { catatan = it }, "Catatan")
                }
                "pengumuman" -> {
                    FormField(judul, { judul = it }, "Judul Pengumuman")
                    FormDropdown(kategoriPm, { kategoriPm = it }, "Kategori", listOf("Guru", "Siswa", "Akademik", "Keuangan"))
                    OutlinedTextField(
                        isi, { isi = it },
                        label = { Text("Isi Pengumuman") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        shape = RoundedCornerShape(14.dp),
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary, unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Buttons
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) { Text("Batal") }

                Button(
                    onClick = {
                        saving = true
                        scope.launch {
                            when (formType) {
                                "guru" -> SimpatikRepo.saveGuru(Guru(nama = nama, nip = nip, mapel = mapel, email = email, noHp = noHp, uid = ""))
                                "aktivitas" -> SimpatikRepo.saveAktivitas(Aktivitas(aktivitas = aktivitasNama, kategori = kategori, tanggal = tanggal, jamMulai = jamMulai, jamSelesai = jamSelesai, guruNama = "Admin"))
                                "presensi" -> SimpatikRepo.savePresensi(Presensi(guruNama = presensiGuru, tanggal = tanggal, status = statusPresensi, lokasi = lokasi, catatan = catatan))
                                "pengumuman" -> SimpatikRepo.savePengumuman(Pengumuman(judul = judul, isi = isi, kategori = kategoriPm, tanggal = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale("id", "ID")).format(java.util.Date())))
                            }
                            saving = false; onSaved()
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = !saving
                ) {
                    if (saving) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Simpan", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun FormField(value: String, onChange: (String) -> Unit, label: String, placeholder: String = "", modifier: Modifier = Modifier) {
    OutlinedTextField(
        value, onChange,
        label = { Text(label) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder, fontSize = 13.sp) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary, unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FormDropdown(value: String, onChange: (String) -> Unit, label: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value, onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f)
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt.replaceFirstChar { it.uppercase() }) },
                    onClick = { onChange(opt); expanded = false }
                )
            }
        }
    }
}
