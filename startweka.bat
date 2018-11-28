@echo off

echo Starting Weka
cd Weka-3-8
call RunWeka.bat
cd %HOMEPATH%
REM  just reset to where ever you were before.
exit