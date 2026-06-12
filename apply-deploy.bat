@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0apply-deploy.ps1" %*
