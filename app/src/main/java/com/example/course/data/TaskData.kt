package com.example.course.data

data class TaskData(
    val id: String,
    val title: String,
    val courseName: String,
    val deadline: String,
    val description: String,
    val isCompleted: Boolean = false
)

val initialTasks = listOf(
    TaskData(
        id = "1",
        title = "Makalah Metodologi Penelitian",
        courseName = "Pengembangan Aplikasi Bergerak",
        deadline = "25 Oktober 2025",
        description = "Membuat rancangan latar belakang masalah dan tinjauan pustaka untuk aplikasi Android berbasis Jetpack Compose.",
        isCompleted = false
    ),
    TaskData(
        id = "2",
        title = "Laporan Skenario Gim 2D",
        courseName = "Pengembangan Gim",
        deadline = "28 Oktober 2025",
        description = "Menyusun cerita utama, karakter pemain, dan diagram alur mekanika permainan untuk prototipe gim 2D.",
        isCompleted = true
    ),
    TaskData(
        id = "3",
        title = "Analisis Kebutuhan Sistem Rumah Sakit",
        courseName = "Pengembangan Aplikasi Medis",
        deadline = "2 November 2025",
        description = "Melakukan identifikasi kebutuhan fungsional dan non-fungsional aplikasi informasi rekam medis pasien.",
        isCompleted = false
    )
)
