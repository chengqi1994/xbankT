package com.hiaward.dataprocess.database.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateSessionFactory {
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	static {
	    try {
	        Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
	        serviceRegistry = new ServiceRegistryBuilder().applySettings(
	                configuration.getProperties()).buildServiceRegistry();
	        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    } catch (Throwable ex) {
	        // Make sure you log the exception, as it might be swallowed
	        System.err.println("Initial SessionFactory creation failed." + ex);
	        throw new ExceptionInInitializerError(ex);
	    }
	}
	
	public static SessionFactory getSessionFactory() {
	    return sessionFactory;
	}
	
    public static Session getSession()  
    {
        return sessionFactory.getCurrentSession();  
    }
    
    public static Transaction getTransaction()  
    {  
		return sessionFactory.getCurrentSession().beginTransaction();
    }

}
