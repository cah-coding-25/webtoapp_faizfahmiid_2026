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
# 🧠 Panduan Lengkap Pengaturan Application ID (Package Name) Android

Dokumen ini berisi panduan komprehensif mengenai fungsi, aturan penulisan, serta efek perubahan **Application ID** (`applicationId`) pada baris ke-17 di file `app/build.gradle.kts`.

---

## 📋 1. Apa itu `applicationId`?

`applicationId` (sering disebut *Package Name*) adalah **KTP atau Identitas Unik Resmi** sebuah aplikasi di dalam sistem operasi Android. 

Sistem Android tidak membedakan aplikasi berdasarkan nama tampilannya (misalnya: "Web To App"), melainkan berdasarkan kode `applicationId` ini. Dua aplikasi berbeda di dunia tidak boleh memiliki ID yang sama jika ingin berjalan di satu perangkat yang sama.

---

## 🛠️ 2. Aturan Wajib Mengubah Nama `applicationId`

Meskipun Anda **bebas mengganti namanya sesuka hati** agar bisa membuat banyak aplikasi baru, Anda harus mematuhi aturan penulisan dari Google Android berikut ini. Jika dilanggar, proses *build* APK di GitHub Actions akan **Gagal / Error**:

1. **Wajib Menggunakan Huruf Kecil Semuanya (`a-z`):**
   * Tidak boleh ada satu pun huruf kapital/besar di dalam kode.
   * *Benar:* `"com.faiz.appdua"`
   * *Salah:* `"com.Faiz.AppDua"`
2. **Harus Menggunakan Struktur Titik (`.`):**
   * Nama identitas minimal harus terdiri dari dua atau tiga kata yang dipisahkan oleh tanda titik (seperti format domain internet terbalik).
   * *Benar:* `"com.cahcoding.myapp"`
   * *Salah:* `"com-cahcoding-myapp"` atau `"cahcodingmyapp"`
3. **Karakter Pertama Setelah Titik Harus Huruf:**
   * Angka (`0-9`) boleh digunakan, tetapi tidak boleh ditaruh langsung setelah tanda titik atau di awal kata.
   * *Benar:* `"com.app2.versi"` atau `"com.faiz.appv2"`
   * *Salah:* `"com.2app.versi"`
4. **Dilarang Menggunakan Simbol Khusus dan Spasi:**
   * Jangan gunakan spasi, tanda hubung (`-`), garis bawah (`_`), atau simbol seperti `@`, `#`, `$`, dan lainnya.
5. **Wajib Diapit Tanda Kutip Dua (`""`):**
   * Jangan sampai menghapus tanda petik dua di awal dan di akhir nama identitas tersebut.

### 💡 Contoh Penggantian yang Aman:
* *Bawaan awal:* `applicationId = "com.aistudio.web2app.app"`
* *Alternatif 1:* `applicationId = "com.aistudio.web2app.kedua"`
* *Alternatif 2:* `applicationId = "com.faiz.aplikasibaru"`

---

## ⚙️ 3. Fungsi Utama `applicationId`

* **Pembeda Aplikasi di HP:** Menghindari bentrok sistem. Jika ID berbeda, Android akan mendeteksinya sebagai aplikasi yang berbeda dan mengizinkannya terinstal bersamaan.
* **Alamat Folder Penyimpanan:** Android memakai ID ini untuk membuat folder penyimpanan internal terisolasi khusus untuk aplikasi tersebut di dalam HP (`data/data/nama.application.id/`).
* **Identitas Resmi Play Store:** Jika nanti aplikasi diunggah ke Google Play Store, link URL aplikasi Anda akan menggunakan nama ini (Contoh: `https://google.com`).

---

## 📊 4. Efek yang Terjadi Jika `applicationId` Diganti

Ketika Anda mengubah kode di baris 17 menjadi nama baru, berikut dampak langsung yang akan Anda rasakan:

### ✅ Efek Positif (Tujuan Utama)
* **Bisa Diinstal Bersamaan:** Anda bisa langsung menginstal APK hasil *build* terbaru tanpa perlu menghapus APK pertama yang sudah terpasang di HP Anda.
* **Manajemen Ikon Mandiri:** Di layar HP Anda akan muncul dua ikon aplikasi baru yang terpisah dan bisa dibuka secara bersamaan tanpa saling mengganggu.

### ⚠️ Efek Samping yang Perlu Diketahui
* **Data Tidak Berbagi (Terisolasi):** Karena folder sistemnya dibuat baru, aplikasi kedua tidak akan bisa membaca riwayat *login*, *cache*, atau data dari aplikasi pertama.
* **Push Notification Firebase Terputus:** Jika *source code* Anda menggunakan layanan Firebase Cloud Messaging (Notifikasi otomatis), fitur notifikasi tidak akan masuk ke APK kedua ini. Anda harus mendaftarkan ulang `applicationId` yang baru tersebut ke dalam *dashboard* Firebase Anda jika ingin notifikasinya kembali aktif.

---
# 🔄 Panduan Lengkap Pengaturan Versi Aplikasi (Version Management) Android

Dokumen ini berisi panduan komprehensif mengenai fungsi, aturan penulisan, serta mekanisme pembaruan (*update*) menggunakan **Version Code** (`versionCode`) dan **Version Name** (`versionName`) pada baris ke-19 dan 20 di file `app/build.gradle.kts`.

---

## 📋 1. Perbedaan `versionCode` dan `versionName`

Di dalam sistem Android, sebuah aplikasi memiliki dua jenis identitas versi yang berbeda fungsi:

### A. `versionCode` (Identitas Versi untuk Sistem Android)
* **Fungsi:** Sebagai acuan mutlak bagi sistem HP Android untuk menentukan apakah sebuah file APK merupakan versi baru (pembaruan) atau versi lama.
* **Aturan Penulisan:** **Wajib berupa angka bulat positif** (1, 2, 3, 4, dst.) dan nilainya harus selalu naik (lebih besar) setiap kali Anda merilis pembaruan. Tidak boleh menggunakan angka desimal/koma.
* *Contoh bawaan:* `versionCode = 1`

### B. `versionName` (Identitas Versi untuk Pengguna/Manusia)
* **Fungsi:** Sebagai teks informasi versi yang bisa dilihat oleh pengguna di menu pengaturan HP atau toko aplikasi (Play Store).
* **Aturan Penulisan:** Berupa teks bebas yang diapit tanda kutip dua (`""`), biasanya menggunakan format desimal untuk menandai skala perubahan aplikasi.
* *Contoh bawaan:* `versionName = "1.0"`

---

## ⚠️ 2. Aturan Penting Saat Melakukan Update (Pembaruan) Aplikasi

Karena Anda menginstal APK ini secara mandiri (bukan dari Google Play Store), proses pembaruan **tidak akan terjadi secara otomatis** di HP pengguna. Anda harus mengikuti aturan berikut agar proses instalasi manual berjalan lancar:

### 1. Wajib Menaikkan Angka `versionCode`
Jika Anda melakukan perubahan pada website atau kode aplikasi di GitHub, Anda **wajib** mengubah nilai `versionCode` menjadi lebih tinggi dari versi yang saat ini terinstal di HP.
* Jika versi di HP memiliki `versionCode = 1`, maka di GitHub harus diubah menjadi `versionCode = 2`.
* **Dampak jika lupa dinaikkan:** HP Android akan memblokir instalasi APK baru tersebut dengan memunculkan pesan *error* "Aplikasi tidak terinstal" karena sistem menganggap file tersebut sama persis dengan yang sudah terpasang.

### 2. Menyesuaikan Teks `versionName`
Ubah teks ini agar Anda tidak bingung membedakan riwayat perilisan aplikasi Anda.
* Perubahan kecil (perbaikan teks/bug web): `"1.0"` menjadi `"1.1"` atau `"1.2"`
* Perubahan besar (ganti link website/desain total): `"1.0"` menjadi `"2.0"`

---

## 🚀 3. Alur Kerja (Workflow) Melakukan Update Aplikasi secara Manual

Untuk memperbarui aplikasi yang sudah terlanjur terinstal di HP Anda tanpa menghilangkan data lama, ikuti langkah demi langkah ini:


---
## 📑 Ringkasan Spesifikasi Build GitHub Actions

- **Java Version**: Temurin JDK 17
- **Gradle Version**: `9.3.1`
- **Output Artifacts**: APK (Debug), AAB (Signed Release Bundle), Keystore Backup (`my-upload-key.jks`), Source Code Zip (`web2app-source-code.zip`)
- **Automated Tagging**: `build-{RUN_NUMBER}-{RUN_ATTEMPT}`
