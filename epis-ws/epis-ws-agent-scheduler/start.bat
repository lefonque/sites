:: SETTING VARIABLES
::set JAVA_HOME=C:\Java\jdk6\jdk1.6.0_45
set AGENT_ROOT_DIR=%~dp0

:: REMOVE SUFFIX '\'
IF %AGENT_ROOT_DIR:~-1%==\ set AGENT_ROOT_DIR=%AGENT_ROOT_DIR:~0,-1%

:: JAVA EXECUTION
java -Dconsumer.root.dir="%AGENT_ROOT_DIR%" -jar "%AGENT_ROOT_DIR%"/epis-ws-agent-scheduler.jar
