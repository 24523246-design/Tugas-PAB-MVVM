# Aplikasi Pencatatan Tugas Kuliah (Course Assignment Tracker)

Aplikasi Android berbasis **Jetpack Compose** yang mengimplementasikan arsitektur **MVVM**, **UI State**, **StateFlow**, **Unidirectional Data Flow (UDF)**, dan **Adaptive Layout** untuk membantu mahasiswa mencatat dan mengelola tugas-tugas perkuliahan.

---

## 👥 Anggota Kelompok (Kicau Mania)
1. *Nama Mahasiswa 1* - NIM 24523179
2. *Nama Mahasiswa 2* - NIM 24523246
3. *Nama Mahasiswa 3* - NIM 24523008

---

## 🎯 Masalah, Pengguna, dan Solusi
* **Pengguna Sasaran:** Mahasiswa dan pelajar yang mengikuti berbagai mata kuliah.
* **Masalah:** Mahasiswa sering kali kesulitan melacak *deadline*/tenggat waktu tugas perkuliahan, rincian instruksi tugas, serta status penyelesaian tugas dari banyak mata kuliah.
* **Solusi:** Aplikasi pencatatan tugas kuliah yang responsif, dilengkapi fitur pencarian cepat, penandaan status tugas (selesai / belum selesai), penambahan tugas baru dengan validasi input, serta penghapusan tugas.

---

## ✨ Fitur Utama
1. **Daftar & Pencarian Tugas Kuliah:** Memfilter tugas berdasarkan judul atau nama mata kuliah secara *real-time*.
2. **Penandaan Status Selesai / Belum:** Menandai tugas yang sudah selesai (*checkbox* atau tombol status) dengan efek coret (*strikethrough*).
3. **Tambah Tugas & Validasi Input Kosong:** Form tambah tugas baru dengan validasi pesan error jika ada kolom yang belum diisi.
4. **Hapus Tugas:** Menghapus tugas dari daftar.
5. **Adaptive Layout (Responsif):**
   * **Layar Sempit (HP Portrait):** Tampilan bertumpuk (*Single Pane*) dengan tombol kembali (*BackHandler*).
   * **Layar Lebar (Tablet / Landscape):** Tampilan berdampingan (*Dual Pane / List-Detail*) secara *side-by-side*.

---

## 📂 Gambaran Umum Struktur Paket `com.example.course`

Proyek ini menerapkan arsitektur MVVM (Model-View-ViewModel) dengan prinsip UDF (Unidirectional Data Flow) dan pemisahan tanggung jawab (*separation of concerns*).

```text
com.example.course/
├── data/
│   └── TaskData.kt          # [MODEL] Struktur data & data tiruan (mock data)
├── ui/
│   ├── TaskUiState.kt       # [UI STATE] Menyimpan state layar secara immutable
│   ├── TaskViewModel.kt     # [VIEWMODEL] Logika bisnis & pengelolaan StateFlow
│   └── theme/
│       ├── Color.kt         # Warna tema Material 3
│       ├── Theme.kt         # Konfigurasi MaterialTheme (Dark/Light mode)
│       └── Type.kt          # Konfigurasi tipografi/font teks
└── MainActivity.kt          # [VIEW / UI] Komponen Composable & Adaptive Layout
