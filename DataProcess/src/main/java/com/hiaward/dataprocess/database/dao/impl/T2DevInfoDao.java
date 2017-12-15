package com.hiaward.dataprocess.database.dao.impl;

import com.hiaward.dataprocess.database.dao.inter.IT2DevInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.T2DevInfo;
import com.hiaward.dataprocess.database.util.BaseDao;

public class T2DevInfoDao extends BaseDao<T2DevInfo, Integer> implements IT2DevInfoDao {

	public T2DevInfoDao(String dbName) {
		super(dbName);
	}

	@Override
	public void save1(T2DevInfo devinfo) {
		super.save(devinfo);
	}
	
}
