package com.hiaward.dataprocess.database.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.hiaward.dataprocess.database.dao.inter.ISysSystemParamDao;
import com.hiaward.dataprocess.database.pojo.entity.SysSystemParam;
import com.hiaward.dataprocess.database.util.BaseDao;

public class SysSystemParamDao extends BaseDao<SysSystemParam, Integer> implements ISysSystemParamDao {
	
	public SysSystemParamDao(String dbName) {
		super(dbName);
	}
	
	@Override
	public SysSystemParam querySystemParamByName(String name)
	{
		List<Object> paras = new ArrayList<Object>();
		paras.add(name);
		String hql = "from SysSystemParam where IRange=4 and strName=?";
		SysSystemParam sysinfo = super.getByHQL(hql, paras.toArray());
		
		return sysinfo;
	}

}
