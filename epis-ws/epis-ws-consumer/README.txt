I. Agent
1. jar 파일 실행
	1) Program Argument : [Config] or [Biz]
	2) JVM Argument 
		- -Dconsumer.root.dir={프로그램이 실행되는 실제 위치}
		- -Djob.name={job이름 (job.properties 파일 안에 job.names의 값의 각 요소)}

	예) Configuration 동기화 작업을 실행할 경우
		=> java -Dconsumer.root.dir=C:\Sites\Works\episWorks\epis-ws\epis-ws-consumer -Djob.id=JOB-1 -jar epis-ws-consumer.jar Config
	
	※문자열 상수 표시는 '[',']'으로 감싸서 표시하며, 실제 적용시에는 '[',']'기호는 들어가지 않음
	※값에대한 설명은 '{','}'으로 감싸서 표시하며, 실제 적용시에는 '{','}'기호는 들어가지 않음

2. Script 파일(*.bat, *.sh)
	- consumer.root.dir 값은 script파일이 존재하는 디렉토리 경로임
	- 따라서 Script파일 실행 인자로 job.name 값만 주입하면 됨
		예) biz.bat job1
	- config.bat(or sh) 파일의 경우에도 job.name필요함
		=> 차후 property 동기화처리를 요구할 경우, job.name단위로 동기화처리를 수행하도록 구현예정.
		
3. DataSource 처리
	- multi job처리 : job 갯수만큼 jvm process생성
	- system property (-Djob.id)을 이용하여 job name에 따라 db접속처리 함
	  => spring el 내에 ${}이용가능함.(context:property-placeholder 선언되어야 함)
	- 스케쥴링 : script파일 자신이 실행되는 위치를 알 수 있는가? 없다면 변수 세팅을 수동으로 해주어야 함.
