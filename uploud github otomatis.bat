@echo off
title Smart Auto Upload to GitHub - by: Faiz_Fahmi_ID
color 0a

echo ===================================================
echo           SMART AUTO UPLOAD TO GITHUB
echo                by: Faiz_Fahmi_ID
echo ===================================================
echo.

:: 1. Cek Git Portable di Flashdisk Drive D sampai Z
set "GIT_PATH="
for %%i in (D E F G H I J K L M N O P Q R S T U V W X Y Z) do (
    if exist "%%i:\GitPortable\cmd\git.exe" set "GIT_PATH=%%i:\GitPortable\cmd"
    if exist "%%i:\Aplikasi Portable\GitPortable\cmd\git.exe" set "GIT_PATH=%%i:\Aplikasi Portable\GitPortable\cmd"
)

if "%GIT_PATH%"=="" (
    echo [ERROR] Git tidak ditemukan di Flashdisk!
    pause
    exit
)

set "PATH=%GIT_PATH%;%PATH%"
echo [OK] Git Portable Aktif!
echo.

:: 2. Inisialisasi Git Lokal
if not exist ".git" git init

:: 3. Input Identitas (Hanya muncul jika file 'user_config.bat' belum ada)
if exist "user_config.bat" (
    echo [OK] Identitas Pengguna Terdeteksi!
    call user_config.bat
) else (
    echo === KONFIGURASI AWAL IDENTITAS GITHUB ===
    set /p INPUT_EMAIL="Masukkan Email GitHub Anda   : "
    set /p INPUT_NAME="Masukkan Username GitHub Anda: "
    
    :: Menyimpan langsung dalam bentuk file perintah agar tinggal dipanggil
    echo set "MY_EMAIL=%INPUT_EMAIL%" > user_config.bat
    echo set "MY_NAME=%INPUT_NAME%" >> user_config.bat
    
    set "MY_EMAIL=%INPUT_EMAIL%"
    set "MY_NAME=%INPUT_NAME%"
    echo [OK] Identitas berhasil disimpan!
    echo.
)

:: Menerapkan identitas ke Git
git config --global user.email "%MY_EMAIL%"
git config --global user.name "%MY_NAME%"

:: 4. Konfigurasi Penyimpanan Tautan GitHub
if not exist "repo.txt" (
    set /p REPO_URL="Masukkan URL Github: "
    echo %REPO_URL% > repo.txt
) else (
    set /p REPO_URL=nul 2>&1
git remote add origin %REPO_URL%

:: 5. Proses Pengemasan dan Pengunggahan Data
echo.
echo [PROSES] Menambahkan file proyek...
git add -A --force

echo [PROSES] Membuat catatan versi...
git commit -m "Upload otomatis via Script Faiz"

echo [PROSES] Mengunci jalur branch utama...
git branch -M main

echo [PROSES] Mendorong file menuju GitHub Server...
git push -u origin main --force

echo.
echo =============================================
echo      SUKSES TERUNGGAH! - BY: FAIZ_FAHMI_ID
echo =============================================
pause