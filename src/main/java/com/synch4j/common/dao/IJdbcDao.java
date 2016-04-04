package com.synch4j.common.dao;

import java.sql.Connection;

public interface IJdbcDao {
	  public void excute(String paramString);

	  public Connection getCurrentConnection();
}
