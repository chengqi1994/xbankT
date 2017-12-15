package com.hiaward.dataprocess.database.dao.impl;

import com.hiaward.dataprocess.database.dao.inter.IT2LatticeInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.T2LatticeInfo;
import com.hiaward.dataprocess.database.util.BaseDao;

public class T2LatticeInfoDao extends BaseDao<T2LatticeInfo, Integer> implements IT2LatticeInfoDao {

	public T2LatticeInfoDao(String dbName) {
		super(dbName);
	}
	
}
