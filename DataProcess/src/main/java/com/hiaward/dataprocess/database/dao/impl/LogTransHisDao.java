package com.hiaward.dataprocess.database.dao.impl;

import com.hiaward.dataprocess.database.dao.inter.ILogTransHisDao;
import com.hiaward.dataprocess.database.pojo.entity.LogTransHis;
import com.hiaward.dataprocess.database.util.BaseDao;

public class LogTransHisDao extends BaseDao<LogTransHis, Integer> implements ILogTransHisDao {

	public LogTransHisDao(String dbName) {
		super(dbName);
	}

	public LogTransHis getTransHis(Integer id) {
		return super.get(id);
	}
}
