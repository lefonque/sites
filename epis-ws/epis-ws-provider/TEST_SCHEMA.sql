drop sequence seq_agent;
drop sequence seq_job;
drop sequence seq_log;
drop table COM_EPIS_EAI_AGENT;
drop table COM_EPIS_EAI_JOB;
drop table COM_EPIS_EAI_LOG;
create table COM_EPIS_EAI_AGENT (
	agent_id VARCHAR2(4000) not null primary key
	,org_code VARCHAR2(4000)
	,operating_system VARCHAR2(4000)
	,charset VARCHAR2(4000)
	,websvc_user VARCHAR2(4000)
	,websvc_pass VARCHAR2(4000)
	,officer_name VARCHAR2(4000)
	,officer_contact VARCHAR2(4000)
	,sms_use_yn CHAR
	,sms_cell_no VARCHAR2(4000)
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
);

create table COM_EPIS_EAI_JOB (
	job_id VARCHAR2(4000) not null
	,agent_id VARCHAR2(4000)
	,job_name VARCHAR2(4000)
	,job_type VARCHAR2(4000)
	,agent_exec_time VARCHAR2(4000)
	,sql_main VARCHAR2(4000)
	,sql_pre VARCHAR2(4000)
	,sql_post VARCHAR2(4000)
	,jdbc_driver_class_name VARCHAR2(4000)
	,jdbc_url VARCHAR2(4000)
	,jdbc_username VARCHAR2(4000)
	,jdbc_password VARCHAR2(4000)
	,server_sql VARCHAR2(4000)
	,batch_select_count INTEGER
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
	,primary key(job_id,agent_id)
);


CREATE TABLE COM_EPIS_EAI_LOG(
	log_id VARCHAR2(4000) not null primary key
	,agent_id VARCHAR2(4000)
	,job_id VARCHAR2(4000)
	,row_count INTEGER
	,result_flag CHAR(1)
	,create_date TIMESTAMP
);

create sequence seq_agent start with 1 increment by 1 maxvalue 1000000;
create sequence seq_job start with 1 increment by 1 maxvalue 1000000;
create sequence seq_log start with 1 increment by 1 maxvalue 1000000;

ALTER USER SA SET PASSWORD 'sa'



SELECT * FROM (SELECT ROWNUM rnum, temp.* FROM (SELECT log.log_id,log.agent_id,log.job_id,job.job_name,log.row_count,log.result_flag,DECODE(log.result_flag,'S','성공','F','실패',log.result_flag) result_flag_text,log.create_date FROM COM_EPIS_EAI_LOG log, COM_EPIS_EAI_JOB job WHERE log.job_id=job.job_id ORDER BY create_date desc) temp WHERE result_flag_text='실패') WHERE rnum BETWEEN 1 AND 10


SELECT COUNT(log_id) FROM (
		SELECT log.log_id,log.agent_id,log.job_id,job.job_name,log.row_count,log.result_flag,DECODE(log.result_flag,'S','성공','F','실패',log.result_flag) result_flag_text,log.create_date
		FROM COM_EPIS_EAI_LOG log, COM_EPIS_EAI_JOB job
		WHERE log.job_id=job.job_id
)
WHERE result_flag_text='성공'
/*
SELECT SYSDATE FROM DUAL
SELECT SYSTIMESTAMP FROM DUAL
SELECT CONCAT('ABC',1) FROM DUAL
jdbc:sqlserver://localhost:1433;databaseName=db0
SCHTASKS /Create /SC HOURLY /SD 2013/11/08 /ST 16:50 /TR EPIS_AGENT_SYNC /TN sync.bat 
*/