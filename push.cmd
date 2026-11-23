@echo off
rem ============================================================
rem  Commit and push everything to GitHub.
rem  Usage: double click, or  push.cmd "commit message"
rem ============================================================
call D:\AndroidDev\env.cmd
cd /d D:\AndroidDev\repos\android-lab

set "MSG=%~1"
if "%MSG%"=="" set "MSG=update %DATE% %TIME%"

git add -A
git commit -m "%MSG%"
git push -u origin main

echo.
echo Done. Check build status at:
echo   https://github.com/muqiu/android-lab/actions
pause
