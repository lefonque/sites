drop sequence seq_agent;
drop sequence seq_job;
drop sequence seq_log;
drop table COM_EPIS_EAI_AGENT;
drop table COM_EPIS_EAI_JOB;
drop table COM_EPIS_EAI_LOG;
create table COM_EPIS_EAI_AGENT (
	agent_id VARCHAR2(100) not null primary key
	,org_code VARCHAR2(100)
	,operating_system VARCHAR2(20)
	,charset VARCHAR2(20)
	,websvc_user VARCHAR2(50)
	,websvc_pass VARCHAR2(50)
	,officer_name VARCHAR2(50)
	,officer_contact VARCHAR2(20)
	,sms_use_yn CHAR(1)
	,sms_cell_no VARCHAR2(20)
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
);

create table COM_EPIS_EAI_JOB (
	job_id VARCHAR2(100) not null
	,agent_id VARCHAR2(100) not null
	,job_name VARCHAR2(100)
	,job_type CHAR(1)
	,exec_time VARCHAR2(10)
	,sql_main VARCHAR2(4000)
	,sql_pre VARCHAR2(4000)
	,sql_post VARCHAR2(4000)
	,jdbc_driver_class_name VARCHAR2(100)
	,jdbc_url VARCHAR2(100)
	,jdbc_username VARCHAR2(50)
	,jdbc_password VARCHAR2(50)
	,server_sql VARCHAR2(4000)
	,batch_select_count INTEGER
	,created_date TIMESTAMP
	,modified_date TIMESTAMP
	,primary key(job_id,agent_id)
);


CREATE TABLE COM_EPIS_EAI_LOG(
	log_id VARCHAR2(100) not null primary key
	,agent_id VARCHAR2(100)
	,job_id VARCHAR2(100)
	,row_count INTEGER
	,result_flag CHAR(1)
	,create_date TIMESTAMP
);

create sequence seq_agent start with 1 increment by 1 maxvalue 100000000;
create sequence seq_job start with 1 increment by 1 maxvalue 100000000;
create sequence seq_log start with 1 increment by 1 maxvalue 100000000;



--SMS
DROP TABLE EM_SMT_TRAN;
DROP SEQUENCE sq_em_smt_tran_01;
CREATE TABLE em_smt_tran (
	mt_pr NUMBER(30) NOT NULL PRIMARY KEY
	,date_client_req TIMESTAMP
	,content VARCHAR2(4000)
	,callback VARCHAR(20)
	,service_type CHAR(1)
	,broadcast_yn CHAR(1)
	,msg_status CHAR(1)
	,recipient_num VARCHAR(20)
	,recipient_net VARCHAR(200)
	,recipient_npsend VARCHAR(200)
	,country_code VARCHAR(10)
	,charset VARCHAR(10)
	,msg_type VARCHAR(10)
	,crypto_yn CHAR(1)
	,connectionid VARCHAR(50)
);

CREATE SEQUENCE sq_em_smt_tran_01 start with 1 increment by 1 maxvalue 100000000;

ALTER USER SA SET PASSWORD 'sa'



SELECT * FROM (SELECT ROWNUM rnum, temp.* FROM (SELECT log.log_id,log.agent_id,log.job_id,job.job_name,log.row_count,log.result_flag,DECODE(log.result_flag,'S','성공','F','실패',log.result_flag) result_flag_text,log.create_date FROM COM_EPIS_EAI_LOG log, COM_EPIS_EAI_JOB job WHERE log.job_id=job.job_id ORDER BY create_date desc) temp WHERE result_flag_text='실패') WHERE rnum BETWEEN 1 AND 10


SELECT COUNT(log_id) FROM (
		SELECT log.log_id,log.agent_id,log.job_id,job.job_name,log.row_count,log.result_flag,DECODE(log.result_flag,'S','성공','F','실패',log.result_flag) result_flag_text,log.create_date
		FROM COM_EPIS_EAI_LOG log, COM_EPIS_EAI_JOB job
		WHERE log.job_id=job.job_id
)
WHERE result_flag_text='성공'



shutdown;
/*
SELECT SYSDATE FROM DUAL
SELECT SYSTIMESTAMP FROM DUAL
SELECT CONCAT('ABC',1) FROM DUAL
jdbc:sqlserver://localhost:1433;databaseName=db0
SCHTASKS /Create /SC HOURLY /SD 2013/11/08 /ST 16:50 /TR EPIS_AGENT_SYNC /TN sync.bat 
*/