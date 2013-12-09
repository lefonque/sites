
INSERT INTO IFR_IF_MGARAK (SURVEY_DATE,STAN_CODE,TRADE_UNIT,SPEC,GRADE,INTERVAL,SMALL,LARGE,AVERAGE,DUQTY,WEIGHT,SET_VALUE,REG_DATE)
VALUES(:SURVEY_DATE,:STAN_CODE,:TRADE_UNIT,:SPEC,:GRADE,:INTERVAL,:SMALL,:LARGE,:AVERAGE,:DUQTY,:WEIGHT,:SET_VALUE,SYSDATE)

INSERT INTO IFR_IF_MUTONG (
	SURVEY_DATE,ITEM_CODE,KIND_CODE,SURVEY_GRADE,TODAY_PRICE,YESDAY_PRICE,IN_QTY,TRADE_QTY,STEADY_TIMES,BIGO,WORK_TYPE
	,INSPECT_SUM,TOT_SURVEY,UPDATE_NO,COUNT_NO,UNIT_NO,RNF_CODE,AREA_CODE,MARKET_CODE,SURVEY_TYPE,SEND_FLAG,REG_DATE
) VALUES (
	:SURVEY_DATE,:ITEM_CODE,:KIND_CODE,:SURVEY_GRADE,:TODAY_PRICE,:YESDAY_PRICE,:IN_QTY,:TRADE_QTY,:STEADY_TIMES,:BIGO,:WORK_TYPE
	,:INSPECT_SUM,:TOT_SURVEY,:UPDATE_NO,:COUNT_NO,:UNIT_NO,:RNF_CODE,:AREA_CODE,:MARKET_CODE,:SURVEY_TYPE,'Y',:REG_DATE
)


SELECT * FROM ifs_if_mgarak WHERE SURVEY_DATE >=TO_CHAR(SYSDATE-1,'YYYYMMDD')


--RESET JOB-1
UPDATE ifs_if_mgarak SET SURVEY_DATE=TO_CHAR(SYSDATE-1,'YYYYMMDD');
select count(*) from IFR_IF_MGARAK;
delete from IFR_IF_MGARAK;




--RESET JOB-2
UPDATE IFS_IF_MUTONG SET SEND_FLAG='N'
WHERE (
	SURVEY_DATE
	,ITEM_CODE
	,KIND_CODE
	,SURVEY_GRADE
	,AREA_CODE
	,MARKET_CODE
	,SURVEY_TYPE
) IN (
	select
		SURVEY_DATE
		,ITEM_CODE
		,KIND_CODE
		,SURVEY_GRADE
		,AREA_CODE
		,MARKET_CODE
		,SURVEY_TYPE
	from IFR_IF_MUTONG
);
select count(*) from IFS_IF_MUTONG where send_flag='N';
delete from IFR_IF_MUTONG;



select
	r.SURVEY_DATE
	,r.ITEM_CODE
	,r.KIND_CODE
	,r.SURVEY_GRADE
	,r.AREA_CODE
	,r.MARKET_CODE
	,r.SURVEY_TYPE
from IFR_IF_MUTONG r, IFS_IF_MUTONG s
WHERE
r.SURVEY_DATE=s.SURVEY_DATE
AND r.ITEM_CODE=s.ITEM_CODE
AND r.KIND_CODE=s.KIND_CODE
AND r.SURVEY_GRADE=s.SURVEY_GRADE
AND r.AREA_CODE=s.AREA_CODE
AND r.MARKET_CODE=s.MARKET_CODE
AND r.SURVEY_TYPE=s.SURVEY_TYPE;
--select count(*) from IFS_IF_MUTONG
--ALTER TABLE IFS_IF_MUTONG DROP COLUMN EDATE;