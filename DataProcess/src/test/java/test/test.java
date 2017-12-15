package test;

import com.hiaward.dataprocess.database.dao.impl.LogTransHisDao;
import com.hiaward.dataprocess.database.dao.impl.StructUserInfoDao;
import com.hiaward.dataprocess.database.dao.impl.T2DevInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.LogTransHis;
import com.hiaward.dataprocess.database.pojo.entity.StructUserInfo;
import com.hiaward.dataprocess.database.pojo.entity.T2DevInfo;

public class test {
	public static void main(String[] args) {
		StructUserInfoDao user = new StructUserInfoDao("cur");
		StructUserInfo structUserInfo = user.getUserInfoById(323);
		System.out.println(structUserInfo.getStrName());
		
		/*T2DevInfoDao dev = new T2DevInfoDao();
		T2DevInfo devinfo = new T2DevInfo();
		devinfo.setIModelId(333);
		devinfo.setStrDevSid("444");
		devinfo.setIDevType(1);
		devinfo.setIDevStatus(1);
		devinfo.setStrOrgNum("12345");
		devinfo.setStrContractName("24小时终端");
		dev.save1(devinfo);*/
		
		LogTransHisDao transHisDao = new LogTransHisDao("his");
		LogTransHis transHis = transHisDao.get(1);
		System.out.println(transHis.getStrPan());
		System.out.println(transHis.getDtOccur());
	}
}
