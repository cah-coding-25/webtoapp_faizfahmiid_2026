package com.example

/**
 * CONFIGURATION FILE (PENGATURAN UTAMA APLIKASI WEB-TO-APP)
 * 
 * Anda bisa mengubah URL Website, Judul, dan Tampilan aplikasi di file ini.
 */
object AppConfig {
    
    // =========================================================================
    // 1. UBAH LINK WEBSITE DI SINI
    // =========================================================================
    // Masukkan URL website Anda (harus diawali dengan https:// atau http://)
    const val WEB_URL: String = "https://google.com"

    // =========================================================================
    // 2. JUDUL APLIKASI
    // =========================================================================
    const val APP_TITLE: String = "Web To App"

    // =========================================================================
    // 3. PENGATURAN TAMPILAN & FITUR
    // =========================================================================
    
    // Tampilkan Top Bar / Header Aplikasi (Set to false agar tampilan bersih FULL PAGE tanpa header)
    const val SHOW_TOP_BAR: Boolean = false

    // Aktifkan fitur tarik ke bawah untuk muat ulang (Pull To Refresh)
    const val ENABLE_PULL_TO_REFRESH: Boolean = true

    // Buka link WhatsApp (wa.me), Telepon (tel:), Email (mailto:), dan Maps di aplikasi luar
    const val ALLOW_EXTERNAL_INTENTS: Boolean = true

    // Tampilan Mode Desktop (Set true jika ingin versi komputer)
    const val DESKTOP_MODE: Boolean = false

    // DUKUNGAN PWA / OFFLINE MODE:
    // Set 'true' jika website Anda adalah PWA / memiliki Service Worker agar dapat dibuka saat offline.
    // Jika set 'true', aplikasi akan menyimpan cache lokal dan tidak akan menghapus cache Service Worker.
    const val ENABLE_OFFLINE_PWA_MODE: Boolean = false

    // Bersihkan cache web saat aplikasi dibuka agar selalu memuat tampilan terbaru website.
    // (Jika ENABLE_OFFLINE_PWA_MODE = true, sebaiknya isi false agar cache offline tersimpan)
    const val CLEAR_CACHE_ON_START: Boolean = true

    // DUKUNGAN NATIVE WEB SHARE (AKSI BAGIKAN KE MEDSOS & HP):
    // Set 'true' agar tombol share di website pengguna otomatis membuka menu Bagikan bawaan HP (WhatsApp, IG, FB, dll).
    const val ENABLE_WEB_SHARE: Boolean = true

    // Custom User Agent string (Biarkan null untuk bawaan HP Android)
    val CUSTOM_USER_AGENT: String? = null
}
