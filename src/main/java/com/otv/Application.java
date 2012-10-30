package com.otv;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.otv.hbm.User;
import com.otv.util.HibernateUtil;

/**
 * @author onlinetechvision.com
 * @since 30 Sept 2011
 * @version 1.0.0
 *
 */
public class Application {

	private static Logger log = Logger.getLogger(Application.class);
	
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public static void main(String[] args) {		
		Application app = new Application();
		
		//A new User is created...
		User user  = new User("Nathalie", "West");
		
		//user record is inserted and id information returns...
		Integer userId = app.saveUser(user);
		
		//user record is updated...
		app.updateUser(userId, "Michael", "East");
		
		//user record is deleted...
		app.deleteUser(userId);
		
		//SessionFactory is closed...
		sessionFactory.close();
	}
	
	private Integer saveUser(User user) {
		Session session = null;		
		Transaction tx = null;
		Integer userId = null;
		try {
			//session is gotten...
			session = sessionFactory.openSession();
			
			//transaction is started...
			tx = session.beginTransaction();
			
			//user record is inserted...
			userId = (Integer) session.save(user);
			
			//transaction is committed...
			tx.commit();
			log.debug("New Record : " + user + ", wasCommitted : " + tx.wasCommitted());
		} catch (Exception e) {
			if (tx != null) {
				//transaction will be rollbacked when an exception is thrown...
				tx.rollback();
				log.error("New Record : " + user + " can not be inserted!!!");
				e.printStackTrace();
			}
		} finally {
			//session is closed
			session.close();
		}
		return userId;
	}
	
	private void updateUser(Integer userId, String name, String surname) {
		Session session = null;		
		Transaction tx = null;
		try {
			//session is gotten...
			session = sessionFactory.openSession();
			
			//transaction is started...
			tx = session.beginTransaction();
			
			//User record which will be updated is gotten from session and new name and surname values are set...
			User user = (User) session.get(User.class, userId);
			user.setName(name);
			user.setSurname(surname);
			
			//user record is updated...
			session.update(user);
			
			//transaction is committed...
			tx.commit();
			log.debug("Updated Record : " + user + ", wasCommitted : " + tx.wasCommitted());
		} catch (Exception e) {
			if (tx != null) {
				//transaction will be rollbacked when an exception is thrown...
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			//session is closed
			session.close();
		}
	}
	
	private void deleteUser(Integer userId) {
		Session session = null;		
		Transaction tx = null;
		try {
			//session is gotten...
			session = sessionFactory.openSession();
			
			//transaction is started...
			tx = session.beginTransaction();
			
			//User record which will be deleted is gotten from session...
			User user = (User) session.get(User.class, userId);
			
			//user record is deleted...
			session.delete(user);
			
			//transaction is committed...
			tx.commit();
			log.debug("Deleted Record : " + user + ", wasCommitted : " + tx.wasCommitted());
		} catch (Exception e) {
			if (tx != null) {
				//transaction will be rollbacked when an exception is thrown...
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			//session is closed
			session.close();
		}
	}
}
