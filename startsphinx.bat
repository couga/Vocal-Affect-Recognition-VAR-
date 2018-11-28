@echo off
rem title This is our Sphinx Window!
REM echo Enter a name for a folder to store the STT output:
REM echo Press enter once done to start Speech to text.

REM set /p x= 
REM mkdir %x%
REM echo Press enter once done.
REM :continue
REM echo You may close this window.

rem echo STT

echo Sphinx to run in the background
echo Minimise this window
cd "sphinx\pocketsphinx"
bin\Release\Win32\pocketsphinx_continuous.exe -inmic yes -hmm model\en-us\en-us -lm model\en-us\en-us.lm.bin -dict model\en-us\cmudict-en-us.dict -logfn NUL > words.csv

rem start powershell.exe -noexit -Command "&{$host.UI.RawUI.WindowTitle = 'Speech to Text';$pshost = get-host;$pswindow = $pshost.ui.rawui;$newsize = $pswindow.buffersize;$newsize.height = 3000;$newsize.width = 150;$pswindow.buffersize = $newsize;$newsize = $pswindow.buffersize;$pswindow.buffersize = $newsize;$newsize = $pswindow.buffersize;$pswindow.buffersize = $newsize;$newsize = $pswindow.windowsize;$newsize.height = 30;$newsize.width = 55;$pswindow.windowsize = $newsize; 'This window is used for Speech-to-Text translation.';'Live translation will be showed in this window'; 'Press CTRL+C to stop and write output to a log file';cd sphinx\pocketsphinx; bin\Release\Win32\pocketsphinx_continuous.exe -inmic yes -hmm model\en-us\en-us -lm model\en-us\en-us.lm.bin -dict model\en-us\cmudict-en-us.dict -logfn NUL;}"
rem $pswindow.foregroundcolor = 'Yellow';$pswindow.backgroundcolor = 'Black';
pause