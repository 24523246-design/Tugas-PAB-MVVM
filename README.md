# Aplikasi Pencatatan Tugas Kuliah (Course Assignment Tracker)

Aplikasi Android berbasis Jetpack Compose yang mengimplementasikan arsitektur **MVVM**, **UI State**, **StateFlow**, **Unidirectional Data Flow (UDF)**, dan **Adaptive Layout** untuk membantu mahasiswa mencatat dan mengelola tugas-tugas perkuliahan.

---

## 👥 Anggota Kelompok
1. **Nama Mahasiswa 1** - NIM 123456781
2. **Nama Mahasiswa 2** - NIM 123456782
3. **Nama Mahasiswa 3** - NIM 123456783

---

## 🎯 Masalah, Pengguna, dan Solusi
* **Pengguna Sasaran:** Mahasiswa dan pelajar yang mengikuti berbagai mata kuliah.
* **Masalah:** Mahasiswa sering kali kesulitan melacak deadline/tenggat waktu tugas perkuliahan, rincian instruksi tugas, serta status penyelesaian tugas dari banyak matakuliah.
* **Solusi:** Aplikasi pencatatan tugas kuliah yang responsif, dilengkapi fitur pencarian cepat, penandaan status tugas (selesai / belum selesai), penambahan tugas baru dengan validasi input, serta penghapusan tugas.

---

## ✨ Fitur Utama
1. **Daftar & Pencarian Tugas Kuliah:** Memfilter tugas berdasarkan judul atau nama matakuliah secara real-time.
2. **Penandaan Status Selesai / Belum:** Menandai tugas yang sudah selesai (*checkbox* atau tombol status) dengan efek coret (*strikethrough*).
3. **Tambah Tugas & Validasi Input Kosong:** Form tambah tugas baru dengan validasi pesan error merah jika ada kolom yang belum diisi.
4. **Hapus Tugas:** Menghapus tugas dari daftar.
5. **Adaptive Layout (Responsif):**
   * **Layar Sempit (HP Portrait):** Tampilan bertumpuk (*Single Pane*) dengan tombol kembali (*BackHandler*).
   * **Layar Lebar (Tablet / Landscape):** Tampilan berdampingan (*Dual Pane / List-Detail*) secara *side-by-side*.

---

## 🏗️ Arsitektur & Contoh Alur UDF (Unidirectional Data Flow)

Aplikasi ini menggunakan pola arsitektur **MVVM (Model-View-ViewModel)** dengan alur data satu arah (**UDF**):

```text
[ User Action / Event ]
          │
          ▼
    TaskViewModel ──( Memperbarui State )──► _uiState (MutableStateFlow)
          ▲                                         │
          │                                         ▼
          └─────── UI (collectAsStateWithLifecycle) ◄─┘
```

### Contoh Alur UDF: Penambahan Tugas Baru & Validasi Input
1. **UI Event:** Pengguna menekan tombol "+ Tambah", mengisi Judul Tugas, Nama Matakuliah, Tenggat Waktu, dan Deskripsi, lalu menekan "Simpan".
2. **ViewModel Action:** Fungsi `onAddTask()` di `TaskViewModel` dipanggil.
3. **Validation & State Change:** `TaskViewModel` memeriksa apakah ada input yang kosong:
   * **Jika Kosong:** ViewModel memperbarui `validationError = "Judul, matakuliah, tenggat, dan deskripsi wajib diisi!"` di `TaskUiState`.
   * **Jika Valid:** ViewModel membuat `TaskData` baru, menambahkannya ke `allTasks`, mengosongkan form, menutup dialog, dan memperbarui `_uiState`.
4. **UI Update:** Composable `TaskApp` menerima `TaskUiState` terbaru via `collectAsStateWithLifecycle()` dan memperbarui layar secara otomatis.

---

## 🚀 Cara Menjalankan Aplikasi
1. Clone repository ini dari GitHub.
2. Buka proyek menggunakan **Android Studio (Ladybug / Koala / Versi Terbaru)**.
3. Pastikan koneksi internet aktif untuk mengunduh dependensi Gradle.
4. Jalankan aplikasi pada Emulator atau Perangkat Android fisik (Min SDK 24 / Android 7.0+).
