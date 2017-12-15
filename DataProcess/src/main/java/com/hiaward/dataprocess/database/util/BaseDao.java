package com.hiaward.dataprocess.database.util;
 
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
 
/**
 * @ClassName: BaseDao
 * @Description: baseDao实现
 * @author 系统开发部
 * @date 2017年11月9日
 *
 */
public class BaseDao<T, ID extends Serializable> implements IBaseDao<T, ID> {
    protected Class<T> entityClass;
    private String dbName;//要连接的数据库
    
    public BaseDao() {
    }
    
    public BaseDao(String dbName) {
    	this.dbName = dbName;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Class getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return entityClass;
    }
    
    /**
     * 
     * @return session
     */
    public Session getSession() {
    	if(dbName == null) {
    		return null;
    	} else if (dbName.equals("cur")) {
    		return HibernateSessionFactory.getSession();//连接当前库
    	} else if (dbName.equals("his")) {
    		return HibernateSessionFactoryHis.getSession();//连接历史库
    	} else
    	    return null;
    }
    
    /**
     * 
     * @return transaction
     */
    public Transaction getTransaction() {
    	if(dbName == null) {
    		return null;
    	} else if (dbName.equals("cur")) {
    		return HibernateSessionFactory.getTransaction();//连接当前库
    	} else if (dbName.equals("his")) {
    		return HibernateSessionFactoryHis.getTransaction();//连接历史库
    	} else
    	    return null;
    }

    /**
     * <保存实体>
     * <完整保存实体>
     * @param t 实体参数
     * @see com.itv.hiaward.IBaseDao#save(java.lang.Object)
     */
    @Override
    public void save(T t) {
    	Session session = this.getSession();
    	Transaction ts = this.getTransaction();
    	session.save(t);
    	ts.commit();
    }

    /**
     * <update>
     * @param t 实体
     * @see com.itv.hiaward.IBaseDao#update(java.lang.Object)
     */
    @Override
    public void update(T t) {
    	Session session = this.getSession();
    	Transaction ts = this.getTransaction();
    	session.update(t);
    	ts.commit();
    }

    /**
     * <delete>
     * <删除表中的t数据>
     * @param t 实体
     * @see com.itv.hiaward.IBaseDao#delete(java.lang.Object)
     */
    @Override
    public void delete(T t) {
    	Session session = this.getSession();
    	Transaction ts = this.getTransaction();
    	session.delete(t);
    	ts.commit();
    }

    /**
     * <get>
     * <查找的get方法>
     * @param id 实体的id
     * @return 查询出来的实体
     * @see com.itv.hiaward.IBaseDao#get(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
	@Override
    public T get(ID id) {
    	Session session = this.getSession();
    	Transaction ts = this.getTransaction();
    	T get = (T) session.get(getEntityClass(), id);
    	ts.commit();
        return get;
    }

    /**
     * <根据HQL语句查找唯一实体>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     * @see com.itv.hiaward.IBaseDao#getByHQL(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
	@Override
    public T getByHQL(String hqlString, Object... values) {
    	Session session = this.getSession();
    	Transaction ts = this.getTransaction();
    	Query query = session.createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        T get = (T) query.uniqueResult();
        ts.commit();
        return get;
    }
}