package com.synch4j.common.dao.impl;

import java.sql.Connection;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.synch4j.common.dao.IJdbcDao;

public class JdbcDao extends JdbcDaoSupport implements IJdbcDao{
	public Connection getCurrentConnection()
	  {
	    return getConnection();
	  }

	  public void excute(String sqlString)
	  {
	    getJdbcTemplate().execute(sqlString);
	  }
}
