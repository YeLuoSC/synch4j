--PL/SQL Developer Test script 3.0
-- Created on 2016/4/3 by XIE 
declare 
  i integer;
  TAB_BLOBCLOB CONSTANT VARCHAR2(100):='P#SYNCH_T_BLOBCLOB';
  TAB_DECRYPTDATA CONSTANT VARCHAR2(100):='P#SYNCH_T_DECRYPTDATA';
  TAB_EXPORTLOG CONSTANT VARCHAR2(100):='P#SYNCH_T_EXPORTLOG';
  TAB_EXPORTSQL CONSTANT VARCHAR2(100):='P#SYNCH_T_EXPORTSQL';
  TAB_IMPORTLOG CONSTANT VARCHAR2(100):='P#SYNCH_T_IMPORTLOG';
  TAB_IMPORTSQL CONSTANT VARCHAR2(100):='P#SYNCH_T_IMPORTSQL';
  TAB_MAINLOG CONSTANT VARCHAR2(100):='P#SYNCH_T_MAINLOG';
  TAB_REMOTEPROCEDURE CONSTANT VARCHAR2(100):='P#SYNCH_T_REMOTEPROCEDURE';
  TAB_SETTING CONSTANT VARCHAR2(100):='P#SYNCH_T_SETTING';
  TAB_VIEWCODE CONSTANT VARCHAR2(100):='P#SYNCH_T_VIEWCODE';
  TAB_FILEID CONSTANT VARCHAR2(100):='P#SYNCH_T_FILEID';
begin
  SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_BLOBCLOB;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_BLOBCLOB
    (
      logid      VARCHAR2(32),
      physdbname VARCHAR2(50),
      coluname   VARCHAR2(50),
      condition  VARCHAR2(4000),
      bdata      BLOB,
      cdata      CLOB,
      dbversion  TIMESTAMP(6) default SYSDATE,
      status     CHAR(1) default 1,
      guid       VARCHAR2(32) default SYS_GUID() not null
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_BLOBCLOB add constraint PK_P#SYNCH_T_BLOBCLOB primary key (GUID)';
   END IF;
   
  SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_DECRYPTDATA;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_DECRYPTDATA
    (
      logid      VARCHAR2(32) not null,
      physdbname VARCHAR2(50) not null,
      synchdata  CLOB,
      dbversion  TIMESTAMP(6) default SYSDATE,
      status     CHAR(1) default 1,
      guid       VARCHAR2(32) default SYS_GUID() not null
    )
    }';
     EXECUTE IMMEDIATE 'alter table P#SYNCH_T_DECRYPTDATA add constraint PK_P#SYNCH_T_DECRYPTDATA primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_EXPORTLOG;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_EXPORTLOG
    (
      logid         VARCHAR2(32),
      expfilename   VARCHAR2(100),
      expphysdbname VARCHAR2(50),
      expdatas      NUMBER(10),
      lastexpdate   TIMESTAMP(6),
      remark        VARCHAR2(500),
      dbversion     TIMESTAMP(6) default SYSDATE,
      status        CHAR(1) default 1 not null,
      guid          VARCHAR2(32) default SYS_GUID() not null,
      districtid    VARCHAR2(32)
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_EXPORTLOG add constraint PK_P#SYNCH_T_EXPORTLOG primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_EXPORTSQL;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_EXPORTSQL
    (
      logid      VARCHAR2(32) not null,
      physdbname VARCHAR2(50) not null,
      expsql     VARCHAR2(4000),
      dbversion  TIMESTAMP(6) default SYSDATE,
      guid       VARCHAR2(32) default SYS_GUID() not null,
      status     CHAR(1) default '1' not null
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_EXPORTSQL add constraint PK_P#SYNCH_T_EXPORTSQL primary key (LOGID, PHYSDBNAME)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_IMPORTLOG;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_IMPORTLOG
    (
      logid         VARCHAR2(32) not null,
      expfilename   VARCHAR2(100),
      expdatas      VARCHAR2(10),
      expphysdbname VARCHAR2(50) not null,
      impuseddate   VARCHAR2(10),
      insertdatas   NUMBER(10),
      updatedatas   NUMBER(10),
      faildatas     NUMBER(10),
      remark        VARCHAR2(500),
      dbversion     TIMESTAMP(6) default SYSDATE,
      status        CHAR(1) default 1 not null,
      guid          VARCHAR2(32) default SYS_GUID() not null
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_IMPORTLOG add constraint PK_P#SYNCH_T_IMPORTLOG primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_IMPORTSQL;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_IMPORTSQL
    (
      logid      VARCHAR2(32),
      physdbname VARCHAR2(50),
      impsql     VARCHAR2(4000),
      dbversion  TIMESTAMP(6) default SYSDATE,
      guid       VARCHAR2(32) default SYS_GUID() not null,
      status     CHAR(1) default '1' not null
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_IMPORTSQL add constraint PK_P#SYNCH_T_IMPORTSQL primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_MAINLOG;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_MAINLOG
    (
      logid      VARCHAR2(32) not null,
      userid     VARCHAR2(32),
      ipaddress  VARCHAR2(15),
      filename   VARCHAR2(100),
      startdate  VARCHAR2(100),
      enddate    VARCHAR2(100),
      useddate   VARCHAR2(10),
      direction  VARCHAR2(2),
      remark     VARCHAR2(500),
      dbversion  TIMESTAMP(6) default SYSDATE,
      status     CHAR(1) default 1 not null,
      guid       VARCHAR2(32) default SYS_GUID() not null,
      synstatus  CHAR(1),
      districtid VARCHAR2(32),
      fileguid   VARCHAR2(32)
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_MAINLOG add constraint PK_P#SYNCH_T_MAINLOG primary key (LOGID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_REMOTEPROCEDURE;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_REMOTEPROCEDURE
    (
      guid        VARCHAR2(32) default SYS_GUID() not null,
      name        VARCHAR2(500) not null,
      describe    VARCHAR2(2000),
      available   CHAR(1) default 0 not null,
      status      CHAR(1) default 1 not null,
      programcode CLOB not null,
      dbversion   TIMESTAMP(6) default SYSDATE
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_REMOTEPROCEDURE add constraint PK_P#SYNCH_T_REMOTEPROCEDURE primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_SETTING;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_SETTING
    (
      physdbname     VARCHAR2(50) not null,
      synchorder     NUMBER(10) default 0,
      synchcondition VARCHAR2(4000),
      pkcol          VARCHAR2(500),
      synchrecogcol  VARCHAR2(50),
      tabletype      VARCHAR2(2) default '1',
      remark         VARCHAR2(500),
      dbversion      TIMESTAMP(6) default SYSDATE,
      status         CHAR(1) default 1 not null,
      guid           VARCHAR2(32) default SYS_GUID(),
      filtercol      VARCHAR2(200),
      isalwaysexport CHAR(1) default '1'
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_SETTING add constraint PK_P#SYNCH_T_SETTING primary key (PHYSDBNAME)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_VIEWCODE;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_VIEWCODE
    (
      guid      VARCHAR2(32) default SYS_GUID() not null,
      viewcode  CLOB,
      dbversion TIMESTAMP(6) default SYSDATE
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_VIEWCODE add  constraint PK_P#SYNCH_T_VIEWCODE primary key (GUID)';
   END IF;
   
   SELECT COUNT(1) INTO i FROM User_Tables WHERE TABLE_NAME=TAB_FILEID;
  IF i=0 THEN
    EXECUTE IMMEDIATE Q'{
    create table P#SYNCH_T_FILEID
    (
      guid   VARCHAR2(32) default SYS_GUID(),
      fileid VARCHAR2(32) not null
    )
    }';
    EXECUTE IMMEDIATE 'alter table P#SYNCH_T_FILEID add  constraint PK_P#SYNCH_T_FILEID primary key (GUID)';
   END IF;
   
   EXECUTE IMMEDIATE Q'{
   CREATE OR REPLACE VIEW SYNCH_V_USERTABLE AS
    SELECT A.TABLE_NAME PHYSDBNAME, B.COMMENTS TABLENAME, A.BIGDATA
      FROM (SELECT OBJECT_NAME TABLE_NAME,
                   NVL((SELECT '1'
                         FROM USER_TAB_COLUMNS C
                        WHERE C.TABLE_NAME = OBJECT_NAME
                          AND C.DATA_TYPE IN ('BLOB', 'CLOB')
                          AND ROWNUM < 2),
                       '0') BIGDATA,
                   NVL((SELECT 1
                         FROM USER_TAB_COLUMNS C
                        WHERE C.TABLE_NAME = OBJECT_NAME
                          AND C.DATA_TYPE = 'TIMESTAMP(6)'
                          AND ROWNUM < 2),
                       0) ISDBVERSION
              FROM (SELECT OBJECT_NAME
                      FROM USER_OBJECTS
                     WHERE OBJECT_TYPE = 'TABLE')) A,
           USER_TAB_COMMENTS B
     WHERE A.TABLE_NAME = B.TABLE_NAME
       AND A.ISDBVERSION > 0
}';
end;
0
0
