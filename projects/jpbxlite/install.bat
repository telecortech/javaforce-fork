@echo off

echo This will install jPBXLite for Windows.
pause

java -cp javaforce.jar;derby.jar;jpbx-core.jar jpbx.core.Main create

md logs

echo Install complete, open http:\\localhost:8001\ in web browser to view control panel.

