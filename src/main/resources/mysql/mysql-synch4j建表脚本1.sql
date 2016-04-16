DROP PROCEDURE IF EXISTS SYNCH_INIT;
CREATE PROCEDURE SYNCH_INIT()
BEGIN
	DECLARE i INT;
	DECLARE v_sql VARCHAR(1000);
	DECLARE TABLE_BLOBCLOB VARCHAR(100);
	SET TABLE_BLOBCLOB = 'P#SYNCH_T_BLOBCLOB';
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
END