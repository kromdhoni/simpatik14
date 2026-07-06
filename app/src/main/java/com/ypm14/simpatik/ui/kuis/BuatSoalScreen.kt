package com.ypm14.simpatik.ui.kuis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ypm14.simpatik.data.Opsi
import com.ypm14.simpatik.data.Soal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatSoalScreen(
    kuisId: String,
    onBack: () -> Unit,
    onSelesai: () -> Unit
) {
    var soalList by remember { mutableStateOf(listOf<Soal>()) }
    var showTambahDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Soal") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                actions = {
                    TextButton(onClick = onSelesai) { Text("Selesai", color = MaterialTheme.colorScheme.primary) }
                },
                
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTambahDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) { Icon(Icons.Default.Add, "Tambah Soal") }
        }
    ) { padding ->
        if (soalList.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("Belum ada soal", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("Klik + untuk menambah soal", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(soalList, key = { _, s -> s.id }) { index, soal ->
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Soal ${index + 1}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(soal.pertanyaan, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            soal.opsi.forEach { opsi ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("${opsi.label}. ", fontWeight = FontWeight.Bold)
                                    Text(opsi.teks)
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Jawaban: ${('A' + soal.jawaban)}", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tambah Soal Dialog
        if (showTambahDialog) {
            var pertanyaan by remember { mutableStateOf("") }
            var opsiA by remember { mutableStateOf("") }
            var opsiB by remember { mutableStateOf("") }
            var opsiC by remember { mutableStateOf("") }
            var opsiD by remember { mutableStateOf("") }
            var jawabanBenar by remember { mutableIntStateOf(0) }
            var expandedJawaban by remember { mutableStateOf(false) }
            val jawabanOptions = listOf("A", "B", "C", "D")

            AlertDialog(
                onDismissRequest = { showTambahDialog = false },
                title = { Text("Tambah Soal") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = pertanyaan, onValueChange = { pertanyaan = it },
                            label = { Text("Pertanyaan") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = opsiA, onValueChange = { opsiA = it },
                            label = { Text("Opsi A") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = opsiB, onValueChange = { opsiB = it },
                            label = { Text("Opsi B") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = opsiC, onValueChange = { opsiC = it },
                            label = { Text("Opsi C") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = opsiD, onValueChange = { opsiD = it },
                            label = { Text("Opsi D") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                        ExposedDropdownMenuBox(expanded = expandedJawaban, onExpandedChange = { expandedJawaban = !expandedJawaban }) {
                            OutlinedTextField(
                                value = jawabanOptions[jawabanBenar],
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Jawaban Benar") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJawaban) },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )
                            ExposedDropdownMenu(expanded = expandedJawaban, onDismissRequest = { expandedJawaban = false }) {
                                jawabanOptions.forEachIndexed { idx, label ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = { jawabanBenar = idx; expandedJawaban = false }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (pertanyaan.isNotBlank()) {
                            val soal = Soal(
                                id = "s${soalList.size + 1}",
                                nomor = soalList.size + 1,
                                pertanyaan = pertanyaan,
                                opsi = listOf(
                                    Opsi("A", opsiA), Opsi("B", opsiB),
                                    Opsi("C", opsiC), Opsi("D", opsiD)
                                ),
                                jawaban = jawabanBenar,
                                bobot = 10
                            )
                            soalList = soalList + soal
                            showTambahDialog = false
                        }
                    }) { Text("Tambah") }
                },
                dismissButton = {
                    TextButton(onClick = { showTambahDialog = false }) { Text("Batal") }
                }
            )
        }
    }
}
