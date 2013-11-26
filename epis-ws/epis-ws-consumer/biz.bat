:: setting variables
set AGENT_ROOT_DIR=%~dp0
:: remove suffix '\'
IF %AGENT_ROOT_DIR:~-1%==\ set AGENT_ROOT_DIR=%AGENT_ROOT_DIR:~0,-1%

set JOB_NAME=%1

::java -Dconsumer.root.dir=%AGENT_ROOT_DIR% -Djob.name=%JOB_NAME% -jar %AGENT_ROOT_DIR%/target/epis-ws-consumer.jar Biz >> %AGENT_ROOT_DIR%/%Date:~-0,4%%Date:~5,2%%Date:~8,2%_%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%.debug.log
java -Dconsumer.root.dir=%AGENT_ROOT_DIR% -Djob.name=%JOB_NAME% -jar %AGENT_ROOT_DIR%/target/epis-ws-consumer.jar Debug >> %AGENT_ROOT_DIR%/%Date:~-0,4%%Date:~5,2%%Date:~8,2%_%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%.debug.log