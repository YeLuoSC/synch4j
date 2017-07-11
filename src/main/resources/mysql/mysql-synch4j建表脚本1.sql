DROP PROCEDURE IF EXISTS SYNCH_INIT;
CREATE PROCEDURE SYNCH_INIT()
BEGIN
	DECLARE i INT;
	DECLARE v_sql VARCHAR(1000);
	DECLARE TABLE_BLOBCLOB VARCHAR(100);
	DECLARE TAB_DECRYPTDATA VARCHAR(100);
	DECLARE TAB_EXPORTLOG VARCHAR(100);
	DECLARE TAB_EXPORTSQL VARCHAR(100);
	DECLARE TAB_IMPORTLOG VARCHAR(100);
	DECLARE TAB_IMPORTSQL VARCHAR(100);
	DECLARE TAB_MAINLOG VARCHAR(100);
	DECLARE TAB_REMOTEPROCEDURE VARCHAR(100);
	DECLARE TAB_SETTING VARCHAR(100);
	DECLARE TAB_VIEWCODE VARCHAR(100);
	DECLARE TAB_FILEID VARCHAR(100);
	SET TABLE_BLOBCLOB = 'P#SYNCH_T_BLOBCLOB';
	SET TAB_DECRYPTDATA = 'P#SYNCH_T_DECRYPTDATA';
	SET TAB_EXPORTLOG = 'P#SYNCH_T_EXPORTLOG';
	SET	TAB_EXPORTSQL = 'P#SYNCH_T_EXPORTSQL';
	SET TAB_IMPORTLOG = 'P#SYNCH_T_IMPORTLOG';
	SET	TAB_IMPORTSQL = 'P#SYNCH_T_IMPORTSQL';
	SET	TAB_MAINLOG = 'P#SYNCH_T_MAINLOG';
	SET	TAB_REMOTEPROCEDURE = 'P#SYNCH_T_REMOTEPROCEDURE';
	SET	TAB_SETTING = 'P#SYNCH_T_SETTING';
	SET	TAB_VIEWCODE = 'P#SYNCH_T_VIEWCODE';

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TABLE_BLOBCLOB;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_BLOBCLOB`
    (
      logid      VARCHAR(32),
      physdbname VARCHAR(50),
      coluname   VARCHAR(50),
      `condition`  VARCHAR(4000),
      bdata      BLOB,
      cdata      TEXT,
      dbversion  TIMESTAMP default CURRENT_TIMESTAMP,
      status     CHAR(1) default 1,
      guid       int auto_increment PRIMARY KEY
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_DECRYPTDATA;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_DECRYPTDATA`
    (
      logid      VARCHAR(32) not null,
      physdbname VARCHAR(50) not null,
      synchdata  TEXT,
      dbversion  TIMESTAMP default CURRENT_TIMESTAMP,
      status     CHAR(1) default 1,
      guid       int auto_increment PRIMARY KEY
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_EXPORTLOG;
	IF i=0 THEN
    SET v_sql=' create table `P#SYNCH_T_EXPORTLOG`
    (
      logid         VARCHAR(32),
      expfilename   VARCHAR(100),
      expphysdbname VARCHAR(50),
      expdatas      numeric,
      lastexpdate   TIMESTAMP default 0,
      remark        VARCHAR(500),
      dbversion     TIMESTAMP default CURRENT_TIMESTAMP,
      status        CHAR(1) default 1 not null,
      guid          int auto_increment PRIMARY KEY,
      districtid    VARCHAR(32)
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_EXPORTSQL;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_EXPORTSQL`
    (
      logid      VARCHAR(32) not null,
      physdbname VARCHAR(50) not null,
      expsql     VARCHAR(4000),
      dbversion  TIMESTAMP default CURRENT_TIMESTAMP,
      guid       int auto_increment PRIMARY KEY,
      status     CHAR(1) default 1 not null
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_IMPORTLOG;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_IMPORTLOG`
    (
      logid         VARCHAR(32) not null,
      expfilename   VARCHAR(100),
      expdatas      VARCHAR(10),
      expphysdbname VARCHAR(50) not null,
      impuseddate   VARCHAR(10),
      insertdatas   numeric,
      updatedatas   numeric,
      faildatas     numeric,
      remark        VARCHAR(500),
      dbversion     TIMESTAMP default CURRENT_TIMESTAMP,
      status        CHAR(1) default 1 not null,
      guid          int auto_increment PRIMARY KEY
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_IMPORTSQL;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_IMPORTSQL`
    (
      logid      VARCHAR(32),
      physdbname VARCHAR(50),
      impsql     VARCHAR(4000),
      dbversion  TIMESTAMP default CURRENT_TIMESTAMP,
      guid       int auto_increment PRIMARY KEY,
      status     CHAR(1) default 1 not null
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_MAINLOG;
	IF i=0 THEN
    SET v_sql='create table `P#SYNCH_T_MAINLOG`
    (
      logid      VARCHAR(32) not null,
      userid     VARCHAR(32),
      ipaddress  VARCHAR(15),
      filename   VARCHAR(100),
      startdate  VARCHAR(100),
      enddate    VARCHAR(100),
      useddate   VARCHAR(10),
      direction  VARCHAR(2),
      remark     VARCHAR(500),
      dbversion  TIMESTAMP default CURRENT_TIMESTAMP,
      status     CHAR(1) default 1 not null,
      guid       int auto_increment PRIMARY KEY,
      synstatus  CHAR(1),
      districtid VARCHAR(32),
      fileguid   VARCHAR(32)
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_REMOTEPROCEDURE;
	IF i=0 THEN
    SET v_sql=' create table `P#SYNCH_T_REMOTEPROCEDURE`
    (
      guid        VARCHAR(32) primary key,
      name        VARCHAR(500) not null,
      `describe`    VARCHAR(2000),
      available   CHAR(1) default 0 not null,
      status      CHAR(1) default 1 not null,
      programcode text not null,
      dbversion   TIMESTAMP default CURRENT_TIMESTAMP
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;

	SELECT COUNT(*) INTO i FROM information_schema.TABLES WHERE TABLE_NAME=TAB_SETTING;
	IF i=0 THEN
    SET v_sql=' create table `P#SYNCH_T_SETTING`
    (
      physdbname     VARCHAR(50) not null,
      synchorder     numeric default 0,
      synchcondition VARCHAR(4000),
      pkcol          VARCHAR(500),
      synchrecogcol  VARCHAR(50),
      tabletype      CHAR(1) default 1,
      remark         VARCHAR(500),
      dbversion      TIMESTAMP default CURRENT_TIMESTAMP,
      status         CHAR(1) default 1 not null,
      guid           VARCHAR(32) primary key,
      filtercol      VARCHAR(200),
      isalwaysexport CHAR(1) default 1
    )';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	END IF;
	
	SET v_sql='CREATE OR REPLACE VIEW SYNCH_V_V1 AS
    SELECT TABLE_NAME,CASE WHEN DATA_TYPE=\'BLOB\' THEN \'1\' WHEN DATA_TYPE=\'TEXT\' THEN \'1\' ELSE \'0\' END AS BIGDATA FROM information_schema.COLUMNS';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	
	SET v_sql='CREATE OR REPLACE VIEW SYNCH_V_USERTABLE AS
   SELECT TABLE_NAME,MAX(BIGDATA) FROM SYNCH_V_V1 GROUP BY TABLE_NAME';
	set @v_sql=v_sql;
	PREPARE stmt from @v_sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END