package test;


import com.hiaward.dataprocess.database.dao.impl.StructUserInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.StructUserInfo;

public class MyThread extends Thread{

	StructUserInfoDao user = new StructUserInfoDao("cur");
	TheThread testThread = new TheThread();
	//存放STRUCT_USER_INFO ID的集合
	final Integer theId[] = {266,303,343,222,283,284,264,265};
	@Override
	public void run() {
		long theBegainTime = System.currentTimeMillis();
		// 循环调用查询
		for(int i = 0 ;i < theId.length ;i++){
			//开始查询时的时间
			long begainTime = System.currentTimeMillis();
			StructUserInfo structUserInfo = user.getUserInfoById(theId[i]);
			long endTime = System.currentTimeMillis();
			long runTime = endTime - begainTime;
			System.out.println(Thread.currentThread().getName()+"theId:" + theId[i]  + "线程运行时间：" + runTime);
			if(null == structUserInfo){//查询结果为空
				continue;
			}
		}
		
		long theEndTime = System.currentTimeMillis();
		long sumTime = theEndTime - theBegainTime;
		System.out.println(Thread.currentThread().getName()+"sumTime:" + sumTime);
	}
	
	

}
