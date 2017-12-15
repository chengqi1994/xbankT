package com.hiaward.dataprocess.database.util;
 
import java.io.Serializable;
import java.util.List;
 
 
/**
 * @ClassName: IBaseDao
 * @Description: Dao层基类，实现增删改查
 * @author 系统开发部
 * @date 2017年11月9日
 */
public interface IBaseDao<T, ID extends Serializable> {
	/**
     * <保存实体>
     * <完整保存实体>
     * @param t 实体参数
	 * @return 
     */
    public abstract void save(T t);
 
    /**
     * <update>
     * @param t 实体
     */
    public abstract void update(T t);
    
    /**
     * <delete>
     * <删除表中的t数据>
     * @param t 实体
     */
    public abstract void delete(T t);
    
    /**
     * <get>
     * <查找的get方法>
     * @param id 实体的id
     * @return 查询出来的实体
     */
    public abstract T get(ID id);
    
	/**
     * <执行Hql语句>
     * @param hqlString hql
     * @param values 不定参数数组
     */
	T getByHQL(String hqlString, Object[] values);
}