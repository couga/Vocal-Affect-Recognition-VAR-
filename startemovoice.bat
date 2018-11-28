@echo off

title Emotional Analysis main window
echo Press enter in this window to stop the analysis.
cd "emovoice-master"
do_run linsvm compare

pause