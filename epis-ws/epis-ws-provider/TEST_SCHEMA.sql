drop sequence seq_agent;
drop sequence seq_job;
drop table TBL_CONFIG_AGENT;
drop table TBL_CONFIG_JOB;
create table TBL_CONFIG_AGENT (
	agent_id VARCHAR(4000) not null primary key
	,org_code VARCHAR(4000)
	,operating_system VARCHAR(4000)
	,charset VARCHAR(4000)
	,websvc_user VARCHAR(4000)
	,websvc_pass VARCHAR(4000)
	,officer_name VARCHAR(4000)
	,officer_contact VARCHAR(4000)
	,sms_use_yn CHAR
	,sms_cell_no VARCHAR(4000)
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
);

create table TBL_CONFIG_JOB (
	job_id VARCHAR(4000) not null primary key
	,agent_id VARCHAR(4000)
	,job_name VARCHAR(4000)
	,job_type VARCHAR(4000)
	,agent_exec_time VARCHAR(4000)
	,sql_main VARCHAR(4000)
	,sql_pre VARCHAR(4000)
	,sql_post VARCHAR(4000)
	,jdbc_driver_class_name VARCHAR(4000)
	,jdbc_url VARCHAR(4000)
	,jdbc_username VARCHAR(4000)
	,jdbc_password VARCHAR(4000)
	,server_sql VARCHAR(4000)
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
);

create sequence seq_agent start with 1 increment by 1 maxvalue 1000000;
create sequence seq_job start with 1 increment by 1 maxvalue 1000000;




/*
SELECT SYSDATE FROM DUAL
SELECT SYSTIMESTAMP FROM DUAL
SELECT CONCAT('ABC',1) FROM DUAL
jdbc:sqlserver://localhost:1433;databaseName=db0
SCHTASKS /Create /SC HOURLY /SD 2013/11/08 /ST 16:50 /TR EPIS_AGENT_SYNC /TN sync.bat 
*/