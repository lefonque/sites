:: SETTING VARIABLES
set JAVA_HOME=C:\Java\jdk6\jdk1.6.0_45
set AGENT_ROOT_DIR=%~dp0

:: REMOVE SUFFIX '\'
IF %AGENT_ROOT_DIR:~-1%==\ set AGENT_ROOT_DIR=%AGENT_ROOT_DIR:~0,-1%

:: JAVA EXECUTION
java -Dconsumer.root.dir="%AGENT_ROOT_DIR%" -jar "%AGENT_ROOT_DIR%"/epis-ws-agent-scheduler.jar
::java -Dconsumer.root.dir="%AGENT_ROOT_DIR%" -jar "%AGENT_ROOT_DIR%"/target/epis-ws-agent-scheduler.jar


::Install as Windows Service
::JavaService.exe -install "EPIS AGENT Service" %JAVA_HOME%\jre\bin\server\jvm.dll -Djava.class.path=.;"%JAVA_HOME%"\lib\tools.jar;"%AGENT_ROOT_DIR%"\epis-ws-agent-scheduler.jar -Dconsumer.root.dir="%AGENT_ROOT_DIR%" -start org.epis.ws.agent.scheduler.ScheduleRun -out "%AGENT_ROOT_DIR%"\logs\JavaService.log
