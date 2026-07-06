package com.ypm14.simpatik.data

import java.text.SimpleDateFormat
import java.util.*

// ═══════════════════════════════════════
// REPOSITORY — ponytail: semua Firebase ops di satu tempat
// Gunakan DemoRepo dulu buat testing UI, swap ke FirebaseRepo nanti
// ═══════════════════════════════════════

interface IRepository {
    // Auth
    suspend fun login(email: String, password: String): Result<User>
    suspend fun loginGoogle(): Result<User>
    suspend fun loginMicrosoft(): Result<User>
    suspend fun register(email: String, password: String, nama: String, role: Role): Result<User>
    suspend fun logout()
    suspend fun currentUser(): User?

    // Guru
    suspend fun getGuruList(): List<Guru>
    suspend fun getGuru(uid: String): Guru?
    suspend fun saveGuru(guru: Guru): Result<Unit>

    // Siswa
    suspend fun getSiswaByKelas(kelasId: String): List<Siswa>

    // Presensi
    suspend fun getPresensiList(filterGuruId: String? = null, filterTanggal: String? = null, filterStatus: String? = null): List<Presensi>
    suspend fun getPresensi(id: String): Presensi?
    suspend fun getPresensiRingkasan(tanggal: String): RingkasanPresensi
    suspend fun savePresensi(p: Presensi): Result<Unit>
    suspend fun deletePresensi(id: String): Result<Unit>

    // Aktivitas
    suspend fun getAktivitasList(filterGuruId: String? = null, filterKategori: String? = null, filterTanggal: String? = null): List<Aktivitas>
    suspend fun saveAktivitas(a: Aktivitas): Result<Unit>

    // Pengumuman
    suspend fun getPengumumanList(kategori: String? = null): List<Pengumuman>
    suspend fun savePengumuman(p: Pengumuman): Result<Unit>
    suspend fun markPengumumanRead(id: String)

    // Notifikasi
    suspend fun getNotifikasiList(filterJenis: String? = null, unreadOnly: Boolean = false): List<Notifikasi>
    suspend fun getUnreadCount(): Int
    suspend fun markNotifRead(id: String)

    // Sekolah
    suspend fun getSekolah(): Sekolah
    suspend fun saveSekolah(s: Sekolah): Result<Unit>

    // Dokumen
    suspend fun getDokumenList(filterJenis: String? = null): List<Dokumen>

    // Statistik
    suspend fun getStatistik(): StatistikCounts

    // Jadwal
    suspend fun getJadwalByGuruIdAndHari(guruId: String, hari: String): List<Jadwal>
    suspend fun getJadwalById(id: String): Result<Jadwal>

    // Jurnal
    suspend fun getJurnalByJadwalAndDate(jadwalId: String, tanggal: Long): Result<Jurnal?>
    suspend fun getJurnalSebelumnya(guruId: String, mapelId: String): Result<Jurnal?>
    suspend fun saveJurnal(jurnal: Jurnal): Result<Unit>
    suspend fun updateJurnal(jurnal: Jurnal): Result<Unit>
    suspend fun getRiwayatJurnal(guruId: String, start: Long, end: Long): Result<List<Jurnal>>

    // Kuis
    suspend fun getKuisByGuruId(guruId: String): List<Kuis>
    suspend fun getKuisById(id: String): Kuis?
    suspend fun saveKuis(kuis: Kuis): Result<Unit>
    suspend fun updateKuis(kuis: Kuis): Result<Unit>
    suspend fun deleteKuis(id: String): Result<Unit>
}

// ═══════════════════════════════════════
// DEMO REPO — data in-memory buat UI testing
// ═══════════════════════════════════════
object SimpatikRepo : IRepository {

    private var loggedInUser: User? = null
    private val now = { SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date()) }
    private val today = now()

    // Demo data
    private val demoGuru = mutableListOf(
        Guru(uid = "g1", nama = "Drs. H. Agus", nip = "196501011990031005", mapel = "Matematika", statusAktif = true, alamat = "Jl. Merdeka No. 10", noHp = "08123456789", email = "agus@smk14.sch.id", pendidikan = "S2 Pendidikan Matematika"),
        Guru(uid = "g2", nama = "Siti Nurhaliza, S.Pd.", nip = "197002012005012003", mapel = "Bahasa Indonesia", statusAktif = true, alamat = "Jl. Kenanga No. 5", noHp = "08234567890", email = "siti@smk14.sch.id", pendidikan = "S1 Pendidikan Bahasa"),
        Guru(uid = "g3", nama = "Bambang Suprapto, S.Kom.", nip = "198503102010011007", mapel = "Informatika", statusAktif = true, alamat = "Jl. Anggrek No. 15", noHp = "08345678901", email = "bambang@smk14.sch.id", pendidikan = "S1 Teknik Informatika"),
        Guru(uid = "g4", nama = "Dra. Dewi Sartika", nip = "196803052000122002", mapel = "PPKN", statusAktif = false, alamat = "Jl. Melati No. 8", noHp = "08456789012", email = "dewi@smk14.sch.id", pendidikan = "S2 Pendidikan Pancasila"),
        Guru(uid = "g5", nama = "Hendra Gunawan, S.Pd.", nip = "199012012015051003", mapel = "Penjasorkes", statusAktif = true, alamat = "Jl. Flamboyan No. 20", noHp = "08567890123", email = "hendra@smk14.sch.id", pendidikan = "S1 Pendidikan Olahraga"),
        Guru(uid = "g6", nama = "Rpiah, S.Ag.", nip = "197206012006042004", mapel = "Pendidikan Agama", statusAktif = true, alamat = "Jl. Mawar No. 12", noHp = "08678901234", email = "rpiah@smk14.sch.id", pendidikan = "S1 Pendidikan Agama"),
    )

    private val demoPresensi = mutableListOf(
        Presensi(id = "p1", guruId = "g1", guruNama = "Drs. H. Agus", tanggal = "01/04/2025", jamMasuk = "07:15", jamPulang = "14:30", lokasi = "Ruang 101", status = "hadir"),
        Presensi(id = "p2", guruId = "g2", guruNama = "Siti Nurhaliza, S.Pd.", tanggal = "01/04/2025", jamMasuk = "07:20", jamPulang = "14:15", lokasi = "Ruang 102", status = "hadir"),
        Presensi(id = "p3", guruId = "g3", guruNama = "Bambang Suprapto, S.Kom.", tanggal = "01/04/2025", jamMasuk = "07:30", jamPulang = "14:00", lokasi = "Lab Komputer", status = "hadir"),
        Presensi(id = "p4", guruId = "g4", guruNama = "Dra. Dewi Sartika", tanggal = "01/04/2025", jamMasuk = "", jamPulang = "", lokasi = "-", status = "sakit", catatan = "Demam"),
        Presensi(id = "p5", guruId = "g5", guruNama = "Hendra Gunawan, S.Pd.", tanggal = "01/04/2025", jamMasuk = "07:10", jamPulang = "13:45", lokasi = "Lapangan", status = "hadir"),
    )

    private val demoAktivitas = mutableListOf(
        Aktivitas(id = "a1", guruId = "g1", guruNama = "Drs. H. Agus", aktivitas = "Mengajar Matematika Kelas XII-A", tanggal = "01/04/2025", jamMulai = "07:30", jamSelesai = "09:00", status = "selesai", kategori = "Mengajar"),
        Aktivitas(id = "a2", guruId = "g3", guruNama = "Bambang Suprapto, S.Kom.", aktivitas = "Workshop Coding untuk XI-RPL", tanggal = "01/04/2025", jamMulai = "09:00", jamSelesai = "11:00", status = "selesai", kategori = "Workshop"),
        Aktivitas(id = "a3", guruId = "g2", guruNama = "Siti Nurhaliza, S.Pd.", aktivitas = "Membimbing Lomba Cerpen", tanggal = "01/04/2025", jamMulai = "12:00", jamSelesai = "13:00", status = "selesai", kategori = "Ekstrakurikuler"),
        Aktivitas(id = "a4", guruId = "g5", guruNama = "Hendra Gunawan, S.Pd.", aktivitas = "Latihan Futsal", tanggal = "02/04/2025", jamMulai = "15:00", jamSelesai = "17:00", status = "berlangsung", kategori = "Ekstrakurikuler"),
    )

    private val demoPengumuman = mutableListOf(
        Pengumuman(id = "pm1", judul = "Rapat Dewan Guru", isi = "Rapat bulanan seluruh dewan guru akan dilaksanakan pada hari Jumat, 5 April 2025 pukul 13.00 di Aula.", tanggal = "01/04/2025", kategori = "Guru", authorId = "admin", isNew = true),
        Pengumuman(id = "pm2", judul = "Ujian Tengah Semester", isi = "Jadwal UTS Semester Genap telah diterbitkan. Silakan cek jadwal di papan pengumuman.", tanggal = "28/03/2025", kategori = "Akademik", authorId = "admin", isNew = false),
        Pengumuman(id = "pm3", judul = "Pembayaran SPP", isi = "Pembayaran SPP bulan April paling lambat tanggal 15.", tanggal = "25/03/2025", kategori = "Keuangan", authorId = "admin", isNew = true),
    )

    private val demoNotifikasi = mutableListOf(
        Notifikasi(id = "n1", judul = "Presensi Baru", isi = "Drs. H. Agus telah melakukan presensi", waktu = "2 jam lalu", jenis = "PRESENSI", sudahDibaca = false, linkScreen = "detail_presensi", linkId = "p1"),
        Notifikasi(id = "n2", judul = "Aktivitas Tercatat", isi = "Siti Nurhaliza mencatat aktivitas baru", waktu = "5 jam lalu", jenis = "AKTIVITAS", sudahDibaca = false, linkScreen = "aktivitas", linkId = "a3"),
        Notifikasi(id = "n3", judul = "Pengumuman Baru", isi = "Rapat Dewan Guru telah diumumkan", waktu = "1 hari lalu", jenis = "PENGUMUMAN", sudahDibaca = true, linkScreen = "pengumuman", linkId = "pm1"),
    )

    private val demoDokumen = mutableListOf(
        Dokumen(id = "d1", judul = "Silabus Matematika Kelas X", jenis = "Silabus", mapel = "Matematika", author = "Drs. H. Agus", tanggal = "10/01/2025"),
        Dokumen(id = "d2", judul = "RPP Bahasa Indonesia Bab 3", jenis = "RPP", mapel = "Bahasa Indonesia", author = "Siti Nurhaliza, S.Pd.", tanggal = "15/01/2025"),
        Dokumen(id = "d3", judul = "Modul Ajar Informatika - Dasar Pemrograman", jenis = "Modul Ajar", mapel = "Informatika", author = "Bambang Suprapto, S.Kom.", tanggal = "20/01/2025"),
        Dokumen(id = "d4", judul = "Silabus PPKN Kelas XI", jenis = "Silabus", mapel = "PPKN", author = "Dra. Dewi Sartika", tanggal = "05/02/2025"),
        Dokumen(id = "d5", judul = "RPP Penjasorkes Bab Bola Voli", jenis = "RPP", mapel = "Penjasorkes", author = "Hendra Gunawan, S.Pd.", tanggal = "12/02/2025"),
    )

    override suspend fun login(email: String, password: String): Result<User> {
        val user = User(uid = "admin1", email = email, nama = "Kepala Sekolah", role = Role.KEPSEK)
        loggedInUser = user
        return Result.success(user)
    }

    override suspend fun loginGoogle(): Result<User> {
        return login("google@user.com", "")
    }

    override suspend fun loginMicrosoft(): Result<User> {
        return login("microsoft@user.com", "")
    }

    override suspend fun register(email: String, password: String, nama: String, role: Role): Result<User> {
        val user = User(uid = UUID.randomUUID().toString(), email = email, nama = nama, role = role)
        loggedInUser = user
        return Result.success(user)
    }

    override suspend fun logout() {
        loggedInUser = null
    }

    override suspend fun currentUser(): User? = loggedInUser

    override suspend fun getGuruList(): List<Guru> = demoGuru

    override suspend fun getGuru(uid: String): Guru? = demoGuru.find { it.uid == uid }

    override suspend fun saveGuru(guru: Guru): Result<Unit> {
        val idx = demoGuru.indexOfFirst { it.uid == guru.uid }
        if (idx >= 0) demoGuru[idx] = guru else demoGuru.add(guru)
        return Result.success(Unit)
    }

    override suspend fun getSiswaByKelas(kelasId: String): List<Siswa> = listOf(
        Siswa(nis = "24001", nama = "Ahmad Faiz", kelasId = kelasId),
        Siswa(nis = "24002", nama = "Bella Safira", kelasId = kelasId),
        Siswa(nis = "24003", nama = "Candra Pratama", kelasId = kelasId),
        Siswa(nis = "24004", nama = "Dina Amalia", kelasId = kelasId),
        Siswa(nis = "24005", nama = "Eko Prasetyo", kelasId = kelasId),
    )

    override suspend fun getPresensiList(filterGuruId: String?, filterTanggal: String?, filterStatus: String?): List<Presensi> {
        return demoPresensi.filter { p ->
            (filterGuruId == null || p.guruId == filterGuruId) &&
            (filterTanggal == null || p.tanggal == filterTanggal) &&
            (filterStatus == null || p.status == filterStatus)
        }
    }

    override suspend fun getPresensi(id: String): Presensi? = demoPresensi.find { it.id == id }

    override suspend fun getPresensiRingkasan(tanggal: String): RingkasanPresensi {
        val todayData = demoPresensi.filter { it.tanggal == tanggal }.ifEmpty { demoPresensi }
        return RingkasanPresensi(
            hadir = todayData.count { it.status == "hadir" },
            izin = todayData.count { it.status == "izin" },
            sakit = todayData.count { it.status == "sakit" },
            alpha = todayData.count { it.status == "alpha" }
        )
    }

    override suspend fun savePresensi(p: Presensi): Result<Unit> {
        val entry = if (p.id.isBlank()) p.copy(id = UUID.randomUUID().toString()) else p
        val idx = demoPresensi.indexOfFirst { it.id == entry.id }
        if (idx >= 0) demoPresensi[idx] = entry else demoPresensi.add(entry)
        return Result.success(Unit)
    }

    override suspend fun deletePresensi(id: String): Result<Unit> {
        demoPresensi.removeAll { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun getAktivitasList(filterGuruId: String?, filterKategori: String?, filterTanggal: String?): List<Aktivitas> {
        return demoAktivitas.filter { a ->
            (filterGuruId == null || a.guruId == filterGuruId) &&
            (filterKategori == null || a.kategori == filterKategori) &&
            (filterTanggal == null || a.tanggal == filterTanggal)
        }
    }

    override suspend fun saveAktivitas(a: Aktivitas): Result<Unit> {
        val entry = if (a.id.isBlank()) a.copy(id = UUID.randomUUID().toString()) else a
        val idx = demoAktivitas.indexOfFirst { it.id == entry.id }
        if (idx >= 0) demoAktivitas[idx] = entry else demoAktivitas.add(entry)
        return Result.success(Unit)
    }

    override suspend fun getPengumumanList(kategori: String?): List<Pengumuman> {
        return if (kategori == null || kategori == "Semua") demoPengumuman
        else demoPengumuman.filter { it.kategori == kategori }
    }

    override suspend fun savePengumuman(p: Pengumuman): Result<Unit> {
        val entry = if (p.id.isBlank()) p.copy(id = UUID.randomUUID().toString()) else p
        demoPengumuman.add(0, entry)
        return Result.success(Unit)
    }

    override suspend fun markPengumumanRead(id: String) {
        demoPengumuman.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.let { demoPengumuman[it] = demoPengumuman[it].copy(isNew = false) }
    }

    override suspend fun getNotifikasiList(filterJenis: String?, unreadOnly: Boolean): List<Notifikasi> {
        return demoNotifikasi.filter { n ->
            (filterJenis == null || n.jenis == filterJenis) &&
            (!unreadOnly || !n.sudahDibaca)
        }
    }

    override suspend fun getUnreadCount(): Int = demoNotifikasi.count { !it.sudahDibaca }

    override suspend fun markNotifRead(id: String) {
        demoNotifikasi.indexOfFirst { it.id == id }.takeIf { it >= 0 }?.let { demoNotifikasi[it] = demoNotifikasi[it].copy(sudahDibaca = true) }
    }

    override suspend fun getSekolah(): Sekolah = Sekolah()

    override suspend fun saveSekolah(s: Sekolah): Result<Unit> = Result.success(Unit)

    override suspend fun getStatistik(): StatistikCounts = StatistikCounts(
        jumlahGuru = demoGuru.size,
        jumlahSiswa = 750,
        jumlahKelas = 24,
        presensiHariIni = demoPresensi.count { it.tanggal == today },
        aktivitasHariIni = demoAktivitas.count { it.tanggal == today },
        pengumumanBaru = demoPengumuman.count { it.isNew }
    )

    override suspend fun getDokumenList(filterJenis: String?): List<Dokumen> {
        return if (filterJenis == null) demoDokumen
        else demoDokumen.filter { it.jenis == filterJenis }
    }

    // ═══ Jadwal Demo ═══
    private val demoJadwal = mutableListOf(
        Jadwal(id = "j1", guruId = "g1", guruNama = "Drs. H. Agus", kelasId = "k1", kelasNama = "XII-A", mapelId = "m1", mapelNama = "Matematika", hari = "Senin", jamMulai = "07:30", jamSelesai = "09:00", ruang = "R 101", presensiStatus = PresensiStatus.SELESAI, jurnalStatus = JurnalStatus.SELESAI),
        Jadwal(id = "j2", guruId = "g1", guruNama = "Drs. H. Agus", kelasId = "k2", kelasNama = "XII-B", mapelId = "m1", mapelNama = "Matematika", hari = "Senin", jamMulai = "09:15", jamSelesai = "10:45", ruang = "R 102", presensiStatus = PresensiStatus.BELUM, jurnalStatus = JurnalStatus.BELUM),
        Jadwal(id = "j3", guruId = "g2", guruNama = "Siti Nurhaliza, S.Pd.", kelasId = "k3", kelasNama = "XI-A", mapelId = "m2", mapelNama = "Bahasa Indonesia", hari = "Senin", jamMulai = "07:30", jamSelesai = "09:00", ruang = "R 201"),
        Jadwal(id = "j4", guruId = "g3", guruNama = "Bambang Suprapto, S.Kom.", kelasId = "k4", kelasNama = "XI-RPL", mapelId = "m3", mapelNama = "Informatika", hari = "Selasa", jamMulai = "07:30", jamSelesai = "10:00", ruang = "Lab Kom"),
        Jadwal(id = "j5", guruId = "g1", guruNama = "Drs. H. Agus", kelasId = "k3", kelasNama = "XI-A", mapelId = "m1", mapelNama = "Matematika", hari = "Rabu", jamMulai = "07:30", jamSelesai = "09:00", ruang = "R 101"),
        Jadwal(id = "j6", guruId = "g5", guruNama = "Hendra Gunawan, S.Pd.", kelasId = "k5", kelasNama = "X-A", mapelId = "m5", mapelNama = "Penjasorkes", hari = "Rabu", jamMulai = "09:15", jamSelesai = "10:45", ruang = "Lapangan"),
        Jadwal(id = "j7", guruId = "g6", guruNama = "Rpiah, S.Ag.", kelasId = "k1", kelasNama = "XII-A", mapelId = "m6", mapelNama = "Pendidikan Agama", hari = "Kamis", jamMulai = "07:30", jamSelesai = "09:00", ruang = "R 301"),
        Jadwal(id = "j8", guruId = "g2", guruNama = "Siti Nurhaliza, S.Pd.", kelasId = "k2", kelasNama = "XII-B", mapelId = "m2", mapelNama = "Bahasa Indonesia", hari = "Jum'at", jamMulai = "07:30", jamSelesai = "09:00", ruang = "R 201"),
        Jadwal(id = "j9", guruId = "g3", guruNama = "Bambang Suprapto, S.Kom.", kelasId = "k1", kelasNama = "XII-A", mapelId = "m3", mapelNama = "Informatika", hari = "Sabtu", jamMulai = "08:00", jamSelesai = "10:30", ruang = "Lab Kom"),
    )

    override suspend fun getJadwalByGuruIdAndHari(guruId: String, hari: String): List<Jadwal> {
        return demoJadwal.filter { it.guruId == guruId && it.hari == hari }
    }

    override suspend fun getJadwalById(id: String): Result<Jadwal> {
        return demoJadwal.find { it.id == id }?.let { Result.success(it) }
            ?: Result.failure(Exception("Jadwal tidak ditemukan"))
    }

    // ═══ Jurnal Demo ═══
    private val demoJurnal = mutableListOf<Jurnal>()

    override suspend fun getJurnalByJadwalAndDate(jadwalId: String, tanggal: Long): Result<Jurnal?> {
        return Result.success(demoJurnal.find { it.jadwalId == jadwalId })
    }

    override suspend fun getJurnalSebelumnya(guruId: String, mapelId: String): Result<Jurnal?> {
        return Result.success(demoJurnal.lastOrNull())
    }

    override suspend fun saveJurnal(jurnal: Jurnal): Result<Unit> {
        demoJurnal.add(jurnal)
        return Result.success(Unit)
    }

    override suspend fun updateJurnal(jurnal: Jurnal): Result<Unit> {
        val idx = demoJurnal.indexOfFirst { it.id == jurnal.id }
        if (idx >= 0) demoJurnal[idx] = jurnal else demoJurnal.add(jurnal)
        return Result.success(Unit)
    }

    override suspend fun getRiwayatJurnal(guruId: String, start: Long, end: Long): Result<List<Jurnal>> {
        return Result.success(demoJurnal.filter { it.guruId == guruId && it.tanggal in start..end })
    }

    // ═══ Kuis Demo ═══
    private val demoKuis = mutableListOf(
        Kuis(id = "k1", judul = "UTS Matematika Semester Genap", kelasId = "XII-A", guruId = "g1", durasi = 90, status = StatusKuis.AKTIF, jumlahSoal = 10, createdAt = System.currentTimeMillis() / 1000),
        Kuis(id = "k2", judul = "Kuis Harian Bahasa Indonesia Bab 3", kelasId = "XI-A", guruId = "g2", durasi = 20, status = StatusKuis.DRAFT, jumlahSoal = 5, createdAt = System.currentTimeMillis() / 1000 - 86400),
        Kuis(id = "k3", judul = "Remedial Matematika - Lingkaran", kelasId = "XII-A", guruId = "g1", durasi = 30, status = StatusKuis.SELESAI, jumlahSoal = 8, jumlahPeserta = 32, rataRataNilai = 72.5, createdAt = System.currentTimeMillis() / 1000 - 172800),
    )

    override suspend fun getKuisByGuruId(guruId: String): List<Kuis> {
        return demoKuis.filter { it.guruId == guruId }
    }

    override suspend fun getKuisById(id: String): Kuis? = demoKuis.find { it.id == id }

    override suspend fun saveKuis(kuis: Kuis): Result<Unit> {
        demoKuis.add(kuis)
        return Result.success(Unit)
    }

    override suspend fun updateKuis(kuis: Kuis): Result<Unit> {
        val idx = demoKuis.indexOfFirst { it.id == kuis.id }
        if (idx >= 0) demoKuis[idx] = kuis else demoKuis.add(kuis)
        return Result.success(Unit)
    }

    override suspend fun deleteKuis(id: String): Result<Unit> {
        demoKuis.removeAll { it.id == id }
        return Result.success(Unit)
    }
}
