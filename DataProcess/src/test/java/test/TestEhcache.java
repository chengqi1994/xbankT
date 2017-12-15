package test;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hiaward.dataprocess.database.pojo.entity.StructUserInfo;
import com.hiaward.dataprocess.database.util.HibernateSessionFactory;

public class TestEhcache {
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
		Session session = HibernateSessionFactory.getSession();
    	Transaction ts = HibernateSessionFactory.getTransaction();
    	StructUserInfo structUserInfo = new StructUserInfo();
//    	structUserInfo.setIId(226);
    	String hql = "from StructUserInfo";
    	Query query = session.createQuery(hql);
    	query.setCacheable(true);//激活查询缓存 
    	query.setCacheRegion("StructUserInfo");//指定要使用的cacheRegion，可选 
    	structUserInfo = (StructUserInfo)query.list().get(0);
    	System.out.println("结果：" + structUserInfo.getStrAccount());
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("二级缓存开始");
    	Session s = HibernateSessionFactory.getSession();
    	String sql = "from StructUserInfo";
    	query = s.createQuery(sql);
    	query.setCacheable(true);//激活查询缓存 
    	query.setCacheRegion("StructUserInfo");//指定要使用的cacheRegion，可选 
    	StructUserInfo structUserInfo2 = new StructUserInfo();
    	structUserInfo2 = (StructUserInfo)query.list().get(0);
    	System.out.println("结果：" + structUserInfo2.getStrPassword());
	}

}
