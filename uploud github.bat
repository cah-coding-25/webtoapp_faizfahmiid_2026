@echo off
title Smart Upload - by: Faiz_Fahmi_ID
color 0a
echo =============================================
echo          SMART AUTO UPLOAD TO GITHUB
echo               by: Faiz_Fahmi_ID
echo =============================================

:: Cek Git Portable di Flashdisk Drive D sampai Z
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

if not exist ".git" git init
git config --global user.email "cahsantriit@gmail.com"
git config --global user.name "cah-coding-25"

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

