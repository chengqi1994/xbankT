package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hiaward.dataprocess.database.dao.impl.StructUserInfoDao;
import com.hiaward.dataprocess.database.dao.inter.IStructUserInfoDao;
import com.hiaward.dataprocess.database.pojo.entity.StructUserInfo;

//多线程测试数据库查询
public class TheThread {
	
	public static void main(String[] args) {
		final IStructUserInfoDao structUserinfoDao = new StructUserInfoDao("xbankCur");
		StructUserInfo structUserInfo = new StructUserInfo();
		TheThread testThread = new TheThread();
		//存放STRUCT_USER_INFO ID的集合
		final List<Integer> theId = new ArrayList<Integer>();
		Thread t0 = new Thread(new Runnable() {
		StructUserInfo structUserInfo = new StructUserInfo();
			@Override
			public void run() {
				// 循环调用查询
				for(int i = 0 ;i < theId.size() ;i++){
					//开始查询时的时间
					long begainTime = new Date().getTime();
					structUserInfo	= structUserinfoDao.getUserInfoById(theId.get(i));
					//输出线程名称，时间
					long endTime = new Date().getTime();
					System.out.println(Thread.currentThread().getName() + "-=-运行时间：" + (endTime - begainTime));
				}
				
			}
		} ,"第一个线程");
		Thread t1 = new Thread(new Runnable() {
			StructUserInfo structUserInfo = new StructUserInfo();
			@Override
			public void run() {
				// 循环调用查询
				for(int i = 0 ;i < theId.size() ;i++){
					//开始查询时的时间
					long begainTime = new Date().getTime();
					structUserInfo	= structUserinfoDao.getUserInfoById(theId.get(i));
					//输出线程名称，时间
					long endTime = new Date().getTime();
					System.out.println(Thread.currentThread().getName() + "-=-运行时间：" + (endTime - begainTime));
				}
				
			}
		},"第二个线程");		
		t0.start();
		t1.start();
		
	}

}
