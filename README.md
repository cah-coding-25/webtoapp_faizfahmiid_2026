# 📱 Web To App Converter (Full Page Web2App, PWA Offline & Play Store Ready)

Aplikasi Android profesional yang mengubah website/web app biasa maupun **PWA (Progressive Web App)** menjadi aplikasi Android (**APK** & **AAB**) secara instan dengan tampilan **Full Screen Clean Webview** (tanpa bar hitam/header), serta dilengkapi sistem **Automated Build GitHub Actions** untuk membuat APK (install HP), AAB (Google Play Store), Keystore, dan Source Code secara otomatis.

---

## 🌟 Fitur Utama Aplikasi

- 📱 **Clean Full-Page WebView**: Secara default **Top Bar / Header Hitam telah dinonaktifkan**, sehingga tampilan website Anda tampil 100% penuh seperti aplikasi native.
- ⚡ **Full Hardware Acceleration & Modern Engine**: Mendukung WebGL, Animasi CSS, HTML5, dan JavaScript berkecepatan tinggi.
- 🌐 **PWA & Offline Service Worker Support**: Opsi khusus untuk mendukung website PWA sehingga aplikasi dapat dibuka dan dijalankan tanpa koneksi internet (offline).
- 📁 **Media & File Upload**: Mendukung upload foto, video, dan dokumen dari galeri HP.
- 🎬 **HTML5 Fullscreen Video**: Mendukung pemutaran video mode layar penuh (YouTube, MP4, dll).
- 🔗 **Intent Routing Otomatis**: Otomatis membuka link WhatsApp (`wa.me`), Telepon (`tel:`), Email (`mailto:`), dan Google Maps di aplikasi luar.
- 🚀 **Quad Output GitHub Actions (APK + AAB + Keystore + Source Code)**: Sekali push ke GitHub, sistem langsung menerbitkan 4 jenis file lengkap di GitHub Releases.

---

## 🛠️ Panduan 1: Mengubah Link Website & Pengaturan `AppConfig.kt`

Cukup edit file konfigurasi utama berikut di GitHub atau editor favorit Anda:

📍 **`app/src/main/java/com/example/AppConfig.kt`**

```kotlin
object AppConfig {
    // 1. UBAH LINK WEBSITE ANDA DI SINI
    const val WEB_URL: String = "https://website-anda.com"

    // 2. UBAH JUDUL APLIKASI
    const val APP_TITLE: String = "Nama App Anda"

    // 3. PENGATURAN TAMPILAN & FITUR (TRUE / FALSE)
    const val SHOW_TOP_BAR: Boolean = false        
    const val ENABLE_PULL_TO_REFRESH: Boolean = true 
    const val ALLOW_EXTERNAL_INTENTS: Boolean = true 
    const val DESKTOP_MODE: Boolean = false        
    const val ENABLE_OFFLINE_PWA_MODE: Boolean = false 
    const val CLEAR_CACHE_ON_START: Boolean = true
    const val ENABLE_WEB_SHARE: Boolean = true
    const val ENABLE_GOOGLE_LOGIN_FIX: Boolean = true
    const val SHOW_SPLASH_SCREEN: Boolean = true
}
```

---

## 📖 Penjelasan Lengkap Pengaturan `true` / `false` (Untuk Orang Awam)

Berikut adalah panduan detail setiap opsi pengaturan di `AppConfig.kt` agar Anda bisa menyesuaikan perilaku aplikasi sesuai kebutuhan:

| Pengaturan | Pilihan | Penjelasan & Fungsi |
| :--- | :--- | :--- |
| **`SHOW_TOP_BAR`** | **`false`** *(Rekomendasi)* | **BERSIH FULL SCREEN**: Menyembunyikan bar/header hitam di bagian atas. Website akan tampil 100% penuh dari atas ke bawah seperti aplikasi native profesional. |
| | **`true`** | **TAMPILKAN HEADER**: Menampilkan bar atas berisi judul aplikasi, tombol kembali, maju, reload, serta tombol bagikan URL. |
| **`ENABLE_PULL_TO_REFRESH`** | **`true`** *(Rekomendasi)* | **TARIK UNTUK RELOAD**: Pengguna bisa menggeser/menarik layar ke bawah untuk memuat ulang (refresh) halaman website secara manual. |
| | **`false`** | **MATIKAN REFRESH**: Fitur tarik layar ke bawah dinonaktifkan. |
| **`ALLOW_EXTERNAL_INTENTS`** | **`true`** *(Rekomendasi)* | **BUKA APP LUAR**: Ketika ada link WhatsApp (`wa.me`), Telepon (`tel:`), Email (`mailto:`), atau Google Maps di website, aplikasi akan otomatis memanggil dan membuka aplikasi WhatsApp/Telepon bawaan di HP. |
| | **`false`** | **PAKSA DI DALAM APP**: Memaksa semua link dibuka dalam webview (Link WhatsApp/Telepon tidak akan merespon jika tidak ada app luar yang dibuka). |
| **`DESKTOP_MODE`** | **`false`** *(Rekomendasi)* | **TAMPILAN HP (MOBILE)**: Memuat website dalam format tampilan HP/smartphone yang pas dengan lebar layar. |
| | **`true`** | **TAMPILAN PC (DESKTOP)**: Memaksa website tampil seperti ketika dibuka dari komputer/laptop monitor besar. |
| **`ENABLE_OFFLINE_PWA_MODE`** | **`true`** | **MODE PWA OFFLINE**: Jika website Anda adalah PWA (memiliki Service Worker / Offline Storage), aktifkan opsi ini agar aplikasi **tetap bisa dibuka dan dijalankan tanpa koneksi internet (Offline)**. |
| | **`false`** *(Default)* | **MODE STANDAR**: Aplikasi memerlukan koneksi internet aktif untuk membuka halaman website. |
| **`CLEAR_CACHE_ON_START`** | **`true`** *(Default)* | **HAPUS CACHE LAMA**: Menghapus cache web lama setiap kali aplikasi dibuka dari awal. Sangat berguna agar pengguna selalu mendapatkan tampilan dan update website paling baru. |
| | **`false`** | **SIMPAN CACHE**: Tidak menghapus cache (*Disarankan di-set `false` jika `ENABLE_OFFLINE_PWA_MODE = true`*). |
| **`ENABLE_WEB_SHARE`** | **`true`** *(Rekomendasi)* | **NATIVE SHARE SHEET HP**: Ketika ada tombol share/bagikan di website Anda (`navigator.share`), aplikasi akan otomatis memanggil menu **Bagikan bawaan HP** (menampilkan ikon WhatsApp, Facebook, Instagram, Twitter, Telegram, Email, dll yang ada di HP pengguna). |
| | **`false`** | **MATIKAN SHARE NATIVE**: Menonaktifkan integrasi menu Bagikan bawaan HP. |
| **`ENABLE_GOOGLE_LOGIN_FIX`** | **`true`** *(Rekomendasi)* | **LOGIN GOOGLE/OAUTH LANCAR**: Secara default Google memblokir login OAuth dari WebView biasa (`403 disallowed_useragent`). Jika opsi ini `true`, aplikasi otomatis menyamarkan User-Agent menjadi Chrome Browser resmi, mengizinkan Cookie Pihak Ketiga & Popup Window, sehingga **tombol Login Google Auth di website Anda bisa diklik dan login dengan sukses tanpa error!** |
| | **`false`** | **MATIKAN FIX GOOGLE OAUTH**: Menggunakan User-Agent WebView standar bawaan HP Android. |
| **`SHOW_SPLASH_SCREEN`** | **`true`** *(Rekomendasi)* | **SPLASH SCREEN PREMIUM**: Menampilkan layar pembuka (Splash Screen) full screen yang elegan berisi Logo Aplikasi, Judul, dan Animasi Loading saat pertama kali aplikasi dibuka sampai website selesai dimuat. Memberikan kesan aplikasi native yang mewah & profesional. |
| | **`false`** | **GARIS LOADING TIPIS**: Hanya menampilkan garis progress loading tipis di bagian paling atas layar saat memuat website. |

---

### 🏷️ Mengubah Nama Aplikasi di Home Screen HP
Edit file: **`app/src/main/res/values/strings.xml`**
```xml
<resources>
    <string name="app_name">Nama App Anda</string>
</resources>
```

---

## 🖼️ Panduan 2: Mengubah Logo / Icon Aplikasi

Anda dapat mengganti logo aplikasi dengan 2 cara:

### Cara A: Mengganti File Gambar di GitHub (Sangat Mudah)
1. Upload file gambar logo baru Anda (PNG/SVG) ke folder `app/src/main/res/drawable/`.
2. Edit file **`app/src/main/res/drawable/ic_launcher_foreground.xml`**:
   ```xml
   <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
       <item
           android:width="72dp"
           android:height="72dp"
           android:drawable="@drawable/nama_logo_baru_anda"
           android:gravity="center" />
   </layer-list>
   ```

### Cara B: Lewat Android Studio (Rekomendasi)
1. Buka project di Android Studio.
2. Klik kanan folder **`app/src/main/res`** -> **New** -> **Image Asset**.
3. Pada tab **Foreground Layer**, pilih file gambar logo dari HP/Laptop Anda.
4. Klik **Next** -> **Finish**. Icon otomatis disesuaikan untuk seluruh tipe HP.

---

## 📦 Panduan 3: Cara Menggunakan Berulang Kali Lewat GitHub (Tanpa PC / Android Studio)

Anda dapat membuat puluhan APK & AAB untuk berbagai website langsung dari HP / Browser tanpa perlu install Android Studio:

1. **Download ZIP / Fork / Copy Repositori Ini**:
   - Bagikan repositori ini ke siapa saja. Cukup klik **Use this template** atau download ZIP dan re-upload ke GitHub masing-masing.

2. **Edit Konfigurasi di GitHub**:
   - Buka file `app/src/main/java/com/example/AppConfig.kt`.
   - Ubah `WEB_URL` menjadi link website baru.
   - Atur nilai `true` / `false` sesuai panduan di atas.
   - Klik **Commit changes**.

3. **Otomatis Build di GitHub Actions**:
   - GitHub Actions akan otomatis berjalan setelah Anda commit file.
   - Atau pemicu manual: Masuk ke tab **Actions** -> **Build APK and Release AAB** -> **Run workflow**.

4. **Download Hasil Lengkap (APK + AAB + Keystore + Source Code)**:
   - Tunggu proses build selesai (~1-2 menit).
   - Buka halaman **Releases** di repositori GitHub Anda.
   - Anda akan mendapatkan 4 file output utama:
     - 📱 `[nama-repo]-debug-build-X.apk` : File **APK** langsung install di HP.
     - 🛒 `[nama-repo]-release-build-X.aab` : File **AAB** untuk di-upload ke Google Play Console.
     - 🔑 `my-upload-key.jks` : File **Keystore** (Simpan file kunci ini jika ingin update app di Play Store nanti).
     - 📦 `[nama-repo]-source-code.zip` & Format Bawaan GitHub (`Source code.zip` & `Source code.tar.gz`) : Source code project komplit.

---

## 📑 Ringkasan Spesifikasi Build GitHub Actions

- **Java Version**: Temurin JDK 17
- **Gradle Version**: `9.3.1`
- **Output Artifacts**: APK (Debug), AAB (Signed Release Bundle), Keystore Backup (`my-upload-key.jks`), Source Code Zip (`web2app-source-code.zip`)
- **Automated Tagging**: `build-{RUN_NUMBER}-{RUN_ATTEMPT}`


# 🚀 Smart Auto Upload to GitHub
Dokumen ini menjelaskan penggunaan skrip otomatis berbasis Windows Batch (`.bat`) untuk mempermudah proses inisialisasi dan pengunggahan seluruh file proyek Anda menuju repositori GitHub secara instan tanpa terkecuali.

> **Created By:** Faiz_Fahmi_ID

---

## ✨ Fitur Utama
- 🔍 **Smart Detection:** Otomatis mendeteksi keberadaan Git yang terinstal di sistem laptop atau mencari Git Portable di Flashdisk (`Drive D:` sampai `Z:`).
- 🔓 **Bypass `.gitignore`:** Memaksa pengunggahan seluruh komponen file proyek tanpa ada yang terlewat.
- 🔄 **Auto Config & Sync:** Mengatur remote URL secara otomatis tanpa perlu input manual setelah konfigurasi pertama selesai.

---

## 🛠️ Persyaratan Sistem
Supaya skrip ini berjalan dengan lancar, komputer pendownload wajib memiliki salah satu opsi di bawah ini:
1. **Git Terinstal** secara resmi pada sistem operasi Windows lokal, **ATAU**
2. **Git Portable** yang disimpan di dalam Flashdisk dengan nama folder khusus **`GitPortable`** (letakkan folder langsung di akar utama/paling depan flashdisk atau di dalam folder `Aplikasi Portable\GitPortable`).

---

## 📖 Panduan Cara Penggunaan untuk Pendownload

Jika Anda mengunduh file proyek ini dalam bentuk `.zip`, ikuti langkah-langkah berikut agar bisa mengunggah ulang ke repositori GitHub milik Anda sendiri:

### 1. Sesuaikan Identitas Akun Git Anda
- Klik kanan file **`uploud github.bat`** di folder lokal Anda, lalu pilih **Edit** atau buka menggunakan aplikasi **Notepad**.
- Temukan baris kode konfigurasi identitas dan ubah teks di dalam tanda kutip sesuai data akun GitHub Anda:
  ```bat
  git config --global user.email "email_anda_yang_terdaftar_di_github@gmail.com"
  git config --global user.name "username_github_anda"
  ```
- Simpan perubahan dokumen (**Ctrl + S**) lalu tutup Notepad.

### 2. Atur Ulang Target Repositori Anda
- Cari file konfigurasi bernama **`repo.txt`** di dalam folder utama proyek ini, lalu **Hapus (Delete)** file tersebut. 
- *Catatan: Penghapusan ini wajib dilakukan agar skrip tidak mencoba mengirim file ke repositori bawaan milik pembuat aslinya.*

### 3. Jalankan Pengunggahan Otomatis
- **Klik dua kali** pada file **`uploud github.bat`**.
- Masukkan URL target repositori GitHub Anda yang baru ketika program memunculkan perintah `Masukkan URL Github:`.
- Tekan **Enter** dan tunggu hingga proses sinkronisasi serta transfer data selesai 100%.

---
⭐ Jika alat bantu ini mempermudah pekerjaan Anda, jangan lupa berikan dukungan terbaik Anda!
