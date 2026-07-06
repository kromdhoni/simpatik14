package com.ypm14.simpatik.data

// ═══════════════════════════════════════
// ENUMS
// ═══════════════════════════════════════
enum class Role(val label: String) {
    KEPSEK("Kepala Sekolah"),
    GURU("Guru"),
    TU("Tata Usaha"),
    WALIKELAS("Wali Kelas")
}

enum class StatusPresensi(val label: String) {
    HADIR("Hadir"),
    IZIN("Izin"),
    SAKIT("Sakit"),
    ALPHA("Alpha")
}

enum class NotifJenis(val label: String) {
    PRESENSI("Presensi"),
    AKTIVITAS("Aktivitas"),
    PENGUMUMAN("Pengumuman"),
    REMINDER("Reminder")
}

enum class KategoriPengumuman(val label: String) {
    SEMUA("Semua"),
    GURU("Guru"),
    SISWA("Siswa"),
    AKADEMIK("Akademik"),
    KEUANGAN("Keuangan")
}

// ═══════════════════════════════════════
// DATA CLASSES
// ═══════════════════════════════════════
data class User(
    val uid: String = "",
    val email: String = "",
    val nama: String = "",
    val role: Role = Role.GURU,
    val fotoUrl: String = ""
)

data class Guru(
    val uid: String = "",
    val nama: String = "",
    val nip: String = "",
    val mapel: String = "",
    val statusAktif: Boolean = true,
    val fotoUrl: String = "",
    val alamat: String = "",
    val noHp: String = "",
    val email: String = "",
    val pendidikan: String = ""
)

data class Siswa(
    val nis: String = "",
    val nama: String = "",
    val kelasId: String = "",
    val alamat: String = "",
    val noHp: String = ""
)

data class Kelas(
    val id: String = "",
    val nama: String = "",
    val waliKelasId: String = "",
    val jumlahSiswa: Int = 0
)

data class Presensi(
    val id: String = "",
    val guruId: String = "",
    val guruNama: String = "",
    val tanggal: String = "",
    val jamMasuk: String = "",
    val jamPulang: String = "",
    val lokasi: String = "",
    val status: String = "hadir",
    val catatan: String = ""
)

data class Aktivitas(
    val id: String = "",
    val guruId: String = "",
    val guruNama: String = "",
    val aktivitas: String = "",
    val tanggal: String = "",
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val status: String = "selesai",
    val kategori: String = ""
)

data class Pengumuman(
    val id: String = "",
    val judul: String = "",
    val isi: String = "",
    val tanggal: String = "",
    val kategori: String = "",
    val authorId: String = "",
    val isNew: Boolean = true
)

data class Notifikasi(
    val id: String = "",
    val judul: String = "",
    val isi: String = "",
    val waktu: String = "",
    val jenis: String = "",
    val sudahDibaca: Boolean = false,
    val linkScreen: String = "",
    val linkId: String = ""
)

data class Sekolah(
    val id: String = "sekolah_1",
    val nama: String = "SMK YPM 14 Sumobito",
    val alamat: String = "Jl. Pendidikan No. 123",
    val npsn: String = "12345678",
    val website: String = "www.smk14.sch.id",
    val email: String = "info@smk14.sch.id",
    val noTelp: String = "(021) 12345678",
    val kepalaSekolah: String = "Dr. H. Ahmad, M.Pd.",
    val logoUrl: String = ""
)

data class StatistikCounts(
    val jumlahGuru: Int = 0,
    val jumlahSiswa: Int = 0,
    val jumlahKelas: Int = 0,
    val presensiHariIni: Int = 0,
    val aktivitasHariIni: Int = 0,
    val pengumumanBaru: Int = 0
)

data class RingkasanPresensi(
    val hadir: Int = 0,
    val izin: Int = 0,
    val sakit: Int = 0,
    val alpha: Int = 0
)

data class Dokumen(
    val id: String = "",
    val judul: String = "",
    val jenis: String = "", // Silabus, RPP, Modul Ajar, dll
    val mapel: String = "",
    val author: String = "",
    val tanggal: String = "",
    val fileUrl: String = "",
    val keterangan: String = ""
)

data class MenuItemData(
    val label: String,
    val screen: String,
    val icon: String = "" // icon name, resolved in UI
)

// ═══════════════════════════════════════
// FITUR BARU — Jadwal, Jurnal, Kuis
// ═══════════════════════════════════════

enum class PresensiStatus { BELUM, SELESAI }
enum class JurnalStatus { BELUM, SELESAI }
enum class StatusSinkronisasi { PENDING, SYNCING, SYNCED, GAGAL }
enum class StatusKuis { DRAFT, AKTIF, SELESAI }
enum class StatusKehadiran { HADIR, SAKIT, IZIN, ALFA, TERLAMBAT }

data class Jadwal(
    val id: String = "",
    val guruId: String = "",
    val guruNama: String = "",
    val kelasId: String = "",
    val kelasNama: String = "",
    val mapelId: String = "",
    val mapelNama: String = "",
    val hari: String = "",
    val jamMulai: String = "",
    val jamSelesai: String = "",
    val ruang: String = "",
    val tahunAjaran: String = "2025/2026",
    val semester: Int = 2,
    val presensiStatus: PresensiStatus = PresensiStatus.BELUM,
    val jurnalStatus: JurnalStatus = JurnalStatus.BELUM
)

data class Jurnal(
    val id: String = "",
    val tanggal: Long = 0,
    val jadwalId: String = "",
    val guruId: String = "",
    val materi: String = "",
    val tujuanPembelajaran: String = "",
    val capaianPembelajaran: String = "",
    val catatanKelas: String = "",
    val kendala: String = "",
    val tindakLanjut: String = "",
    val catatanSiswaKhusus: String = "",
    val lampiran: List<Lampiran> = emptyList(),
    val tahunAjaran: String = "2025/2026",
    val semester: Int = 2,
    val statusSinkronisasi: StatusSinkronisasi = StatusSinkronisasi.PENDING
)

data class Lampiran(
    val url: String = "",
    val namaFile: String = "",
    val tipe: String = ""
)

data class Kuis(
    val id: String = "",
    val judul: String = "",
    val deskripsi: String = "",
    val kelasId: String = "",
    val guruId: String = "",
    val durasi: Int = 20,
    val status: StatusKuis = StatusKuis.DRAFT,
    val jumlahSoal: Int = 0,
    val jumlahPeserta: Int = 0,
    val rataRataNilai: Double = 0.0,
    val nilaiTertinggi: Double = 0.0,
    val nilaiTerendah: Double = 0.0,
    val tahunAjaran: String = "2025/2026",
    val semester: Int = 2,
    val createdAt: Long = 0
)

data class Soal(
    val id: String = "",
    val nomor: Int = 0,
    val pertanyaan: String = "",
    val opsi: List<Opsi> = emptyList(),
    val jawaban: Int = 0,
    val bobot: Int = 10
)

data class Opsi(
    val label: String = "",
    val teks: String = ""
)

data class Attempt(
    val id: String = "",
    val kuisId: String = "",
    val namaSiswa: String = "",
    val noAbsen: Int = 0,
    val status: String = "",
    val nilai: Double = 0.0,
    val benar: Int = 0,
    val salah: Int = 0,
    val durasiPengerjaan: Int = 0,
    val submitPada: Long = 0
)
