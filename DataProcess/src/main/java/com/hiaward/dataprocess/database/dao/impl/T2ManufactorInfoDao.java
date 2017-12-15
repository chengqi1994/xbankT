package com.hiaward.dataprocess.database.dao.impl;

import com.hiaward.dataprocess.database.dao.inter.IT2ManufactorInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.T2ManufactorInfo;
import com.hiaward.dataprocess.database.util.BaseDao;

public class T2ManufactorInfoDao extends BaseDao<T2ManufactorInfo, Integer> implements IT2ManufactorInfoDao {

	public T2ManufactorInfoDao(String dbName) {
		super(dbName);
	}

}
