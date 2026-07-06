# SIMPATIK 14

## 1. Informasi Dokumen

| Item | Isi |
|------|-----|
| Nama Produk | SIMPATIK 14 |
| Kepanjangan | Sistem Informasi Manajemen Presensi & Aktivitas Guru |
| Versi | 1.0 (Minimum Viable Product) |
| Status | Draft Final |
| Tanggal | 1 Juli 2026 |
| Pemilik Produk | Akhmad Kamaluddin Romdhoni |
| Organisasi | SMK YPM 14 Sumobito |
| Platform | Android + Web |
| Backend | Firebase |
| Target Rilis | Q3 2026 |

## 2. Executive Summary

SIMPATIK 14 merupakan aplikasi administrasi guru berbasis Android
yang dirancang khusus untuk membantu guru SMK YPM 14 Sumobito
menyelesaikan seluruh administrasi pembelajaran secara cepat melalui
satu aplikasi.

Seluruh proses mulai dari melihat jadwal mengajar, mengisi presensi,
membuat jurnal pembelajaran, membagikan kuis kepada siswa, hingga
melihat hasil nilai dilakukan dalam satu sistem yang saling terintegrasi
menggunakan Firebase sebagai backend.

Siswa tidak perlu menginstal aplikasi. Mereka cukup membuka tautan
yang dikirim guru melalui WhatsApp dan mengerjakan kuis menggunakan
browser.

Target utama aplikasi adalah memangkas waktu administrasi guru menjadi
kurang dari lima menit setiap selesai pembelajaran.

## 3. Latar Belakang

Saat ini administrasi pembelajaran masih dilakukan menggunakan banyak
media. Contohnya:

- Presensi dilakukan secara manual.
- Jurnal ditulis di buku.
- Nilai tersimpan di file Excel.
- Kuis menggunakan Google Form atau Google Classroom.
- Rekap harus dipindahkan kembali ke E-Rapor.

Kondisi tersebut menyebabkan:

- pekerjaan berulang,
- data tersebar,
- membutuhkan banyak aplikasi,
- waktu administrasi menjadi lama.

SIMPATIK 14 dibuat untuk menyederhanakan seluruh proses tersebut.

## 4. Tujuan Produk

### Tujuan Utama

Menyediakan aplikasi administrasi guru yang sederhana, cepat, ringan,
dan mudah digunakan menggunakan smartphone Android.

### Tujuan Bisnis

Digitalisasi administrasi guru
- Mengurangi penggunaan kertas
- Mempercepat proses pelaporan
- Meningkatkan kualitas data sekolah
- Menjadi aplikasi internal SMK YPM 14 Sumobito

## 5. Sasaran Pengguna

- Guru

Karakteristik:

- Mobilitas tinggi
- Menggunakan HP Android
- Tidak ingin aplikasi rumit
- Mengajar beberapa kelas

Kebutuhan:

- Presensi cepat
- Jurnal mudah
- Kuis sederhana
- Rekap nilai otomatis

- Siswa

Karakteristik:

- Menggunakan smartphone berbagai spesifikasi
- Terbiasa menggunakan WhatsApp

Kebutuhan:

- Tidak perlu login
- Tidak perlu instal aplikasi
- Bisa langsung mengerjakan kuis

## 6. Target Keberhasilan

Minimal 95% guru aktif menggunakan aplikasi.
- Waktu administrasi satu jam pelajaran kurang dari lima menit
- Sinkronisasi nilai kurang dari dua detik
- Downtime aplikasi kurang dari 1%

## 7. Ruang Lingkup MVP

**Modul 1 — Login**

**Fitur**

- Login Email
- Login Google
- Logout
- Remember Login

**Modul 2 — Dashboard**

Menampilkan:

- Nama Guru
- Foto Guru
- Hari dan tanggal
- Jadwal hari ini
- Jumlah kelas hari ini
- Presensi belum selesai
- Jurnal belum dibuat
- Kuis aktif
- Shortcut menu

**Modul 3 — Jadwal Mengajar**

Menampilkan:

- Hari
- Jam
- Mata Pelajaran
- Kelas
- Ruang

**Fitur**

- Filter hari
- Detail jadwal

**Modul 4 — Presensi**

Guru memilih jadwal.
Daftar siswa muncul otomatis.
Status:

- Hadir
- Izin
- Sakit
- Alfa
- Terlambat

Fitur:

- Simpan
- Edit
- Rekap
- Export Excel

**Modul 5 — Jurnal Mengajar**

Field:

- Tanggal
- Materi
- Tujuan Pembelajaran
- Capaian Pembelajaran
- Catatan kelas
- Kendala
- Tindak lanjut
- Catatan siswa khusus
