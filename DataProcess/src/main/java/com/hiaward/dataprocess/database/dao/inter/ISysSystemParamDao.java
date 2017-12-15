package com.hiaward.dataprocess.database.dao.inter;

import java.util.List;

import com.hiaward.dataprocess.database.pojo.entity.SysSystemParam;

public interface ISysSystemParamDao {

	SysSystemParam querySystemParamByName(String name);
	
}
