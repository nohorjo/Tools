@echo off
set n=%2
if "%n%"=="" (
set n=500
)
tail \\bisuksdapp02\c$\ADAPT\system%1\jboss\standalone\log\server.log %n%