DROP TABLE if_tgt_job1;
DROP TABLE if_tgt_job2;
CREATE TABLE if_tgt_job1(
	MARKET_CODE		VARCHAR2(4000) NOT NULL PRIMARY KEY
	,AREA_CODE		VARCHAR2(4000)
	,ITEM_CODE		VARCHAR2(4000)
	,KIND_CODE		VARCHAR2(4000)
	,SURVEY_DATE	TIMESTAMP
	,SURVEY_GRADE	VARCHAR2(4000)
	,TODAY_PRICE	NUMBER(15,3)
	,YESDAY_PRICE	NUMBER(15,3)
	,IN_QTY			NUMBER(15)
	,TRADE_QTY		NUMBER(15)
	,STEADY_TIMES	NUMBER(15)
	,BIGO			VARCHAR2(4000)
	,WORK_TYPE		VARCHAR2(4000)
	,INSPECT_SUM	NUMBER(15)
	,TOT_SURVEY		VARCHAR2(4000)
	,UPDATE_NO		VARCHAR2(4000)
	,COUNT_NO		VARCHAR2(4000)
	,UNIT_NO		VARCHAR2(4000)
	,RNF_CODE		VARCHAR2(4000)
	,EDATE			TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
	,EFLAG			CHAR(1) DEFAULT 'I'	NOT NULL
);

CREATE TABLE if_tgt_job2_com_seed (
	IDX 		DOUBLE NOT NULL PRIMARY KEY
	,COM_NAME	VARCHAR2(255)
	,ITEM_CD	DOUBLE
	,ITEM_NM	VARCHAR2(255)
	,ITEM_IMG	VARCHAR2(255)
	,DTL_IMG	VARCHAR2(255)
	,SUBJECT	VARCHAR2(1000)
	,ITEM_SPEC	VARCHAR2(4000)
	,CAUTION	VARCHAR2(4000)
	,EDATE		TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
	,EFLAG		CHAR(1) DEFAULT 'I' NOT NULL
);



/*
JOB1.
INSERT INTO if_tgt_job1 (
	MARKET_CODE, AREA_CODE, ITEM_CODE, KIND_CODE, SURVEY_DATE, SURVEY_GRADE, TODAY_PRICE, YESDAY_PRICE, IN_QTY, TRADE_QTY
	,STEADY_TIMES, BIGO, WORK_TYPE, INSPECT_SUM, TOT_SURVEY, UPDATE_NO, COUNT_NO, UNIT_NO, RNF_CODE, EDATE, EFLAG 
) VALUES(
	:MARKET_CODE, :AREA_CODE, :ITEM_CODE, :KIND_CODE, :SURVEY_DATE, :SURVEY_GRADE, :TODAY_PRICE, :YESDAY_PRICE, :IN_QTY, :TRADE_QTY
	,:STEADY_TIMES, :BIGO, :WORK_TYPE, :INSPECT_SUM, :TOT_SURVEY, :UPDATE_NO, :COUNT_NO, :UNIT_NO, :RNF_CODE, :EDATE, :EFLAG
);

JOB2.
INSERT INTO if_tgt_job2 (
	IDX, COM_NAME, ITEM_CD, ITEM_NM, ITEM_IMG, DTL_IMG, SUBJECT, ITEM_SPEC, CAUTION, EDATE, EFLAG
) VALUES (
	:IDX, :COM_NAME, :ITEM_CD, :ITEM_NM, :ITEM_IMG, :DTL_IMG, :SUBJECT, :ITEM_SPEC, :CAUTION, :EDATE, :EFLAG
)
*/