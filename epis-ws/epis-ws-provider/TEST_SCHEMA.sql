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
	job_id VARCHAR2(4000) not null primary key
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
);

CREATE TABLE COM_EPIS_EAI_LOG(
	log_id VARCHAR2(4000) primary key
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
/*
SELECT SYSDATE FROM DUAL
SELECT SYSTIMESTAMP FROM DUAL
SELECT CONCAT('ABC',1) FROM DUAL
jdbc:sqlserver://localhost:1433;databaseName=db0
SCHTASKS /Create /SC HOURLY /SD 2013/11/08 /ST 16:50 /TR EPIS_AGENT_SYNC /TN sync.bat 
*/