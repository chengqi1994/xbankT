package com.hiaward.dataprocess.database.dao.impl;

import com.hiaward.dataprocess.database.dao.inter.IStructUserInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.StructUserInfo;
import com.hiaward.dataprocess.database.util.BaseDao;

public class StructUserInfoDao extends BaseDao<StructUserInfo, Integer> implements IStructUserInfoDao
{	
	public StructUserInfoDao(String dbName) {
		super(dbName);
	}

	public StructUserInfo getUserInfoById(Integer id) {
		return this.get(id);
	}

}
