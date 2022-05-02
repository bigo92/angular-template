-- Create table
create table SCHEDULE_PROCESSING
(
    processing_id  NUMBER generated always as identity,
    setting_id     NUMBER,
    transaction_id VARCHAR2(100),
    server_ip      VARCHAR2(255),
    is_last        NUMBER(1),
    create_date    TIMESTAMP(6) default current_timestamp
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
-- Create table
create table SCHEDULE_SETTING
(
    setting_id NUMBER generated always as identity,
    name_job   VARCHAR2(100),
    time_cross VARCHAR2(100),
    ip_server  VARCHAR2(255),
    json_param CLOB,
    type_job   NUMBER(1),
    type_run   NUMBER(1),
    job_group  VARCHAR2(255)
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


