@echo off
title Smart Upload - by: Faiz_Fahmi_ID
color 0a
echo =============================================
echo          SMART AUTO UPLOAD TO GITHUB
echo               by: Faiz_Fahmi_ID
echo =============================================

:: ====================================================================
:: DETEKSI JALUR GIT
:: PRIORITAS: GIT DI PC/LAPTOP, BARU GIT PORTABLE DI FLASHDISK
:: ====================================================================

:: 1. Cek apakah Git sudah tersedia di Sistem PC/Laptop
where git >nul 2>&1
if not errorlevel 1 (
    echo [OK] Git resmi terdeteksi aktif di Sistem PC!
    goto SelesaiCekGit
)

:: 2. Jika Git PC tidak ditemukan, cari Git Portable di Flashdisk D-Z
echo [INFO] Git tidak ditemukan di PC.
echo [INFO] Mencari Git Portable di Flashdisk...

set "GIT_PATH="

for %%i in (D E F G H I J K L M N O P Q R S T U V W X Y Z) do (
    if exist "%%i:\GitPortable\cmd\git.exe" set "GIT_PATH=%%i:\GitPortable\cmd"
    if exist "%%i:\Aplikasi Portable\GitPortable\cmd\git.exe" set "GIT_PATH=%%i:\Aplikasi Portable\GitPortable\cmd"
)

:: 3. Jika Git PC dan Git Portable tidak ditemukan
if "%GIT_PATH%"=="" (
    echo [ERROR] Git tidak ditemukan di PC maupun Flashdisk!
    pause
    exit
)

:: 4. Aktifkan Git Portable untuk CMD ini
set "PATH=%GIT_PATH%;%PATH%"
echo [OK] Git Portable ditemukan aktif di Flashdisk!

:SelesaiCekGit
echo.

:: ====================================================================
:: PROSES ASLI
:: ====================================================================

if not exist ".git" git init
git config --global user.email "masukkan e-mail yang terdaftar pada github"
git config --global user.name "masukkan username github anda"

set /p REPO_URL="Masukkan URL Github: "
git remote remove origin >nul 2>&1
git remote add origin %REPO_URL%

git add -A --force
git commit -m "Upload otomatis via Script Faiz"
git branch -M main
git push -u origin main --force

echo.
echo =============================================
echo      SUKSES TERUNGGAH! - BY: FAIZ_FAHMI_ID
echo =============================================
pause
