create table HIS_MANAGER.LOG_ERROR_PROCESS
(
    LOG_ERROR_ID NUMBER generated always as identity,
    TRANSACTION_ID VARCHAR2(255),
    JOB_ID   NUMBER,
    JOB_NAME VARCHAR2(500),
    JOB_GROUP VARCHAR2(500),
    JOB_DESCRIPTION VARCHAR2(500),
    IP_SERVER  VARCHAR2(255),
    JSON_PARAM CLOB,
    LOG_MESSAGE VARCHAR2(500),
    CREATE_DATE TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
    tablespace DBHIS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-------
create table HIS_MANAGER.LOG_SUCCESS_PROCESS
(
    LOG_SUCCESS_ID NUMBER generated always as identity,
    TRANSACTION_ID VARCHAR2(255),
    JOB_ID   NUMBER,
    JOB_NAME VARCHAR2(500),
    JOB_GROUP VARCHAR2(500),
    JOB_DESCRIPTION VARCHAR2(500),
    IP_SERVER  VARCHAR2(255),
    JSON_PARAM CLOB,
    SUCCESS_MESSAGE VARCHAR2(500),
    BODY_CONTENT CLOB,
    CREATE_DATE TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
)
    tablespace DBHIS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );


