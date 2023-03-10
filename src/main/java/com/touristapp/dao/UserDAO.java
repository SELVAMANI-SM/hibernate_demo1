package com.touristapp.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.touristapp.model.User;
import com.touristapp.util.HibernateUtil;

public class UserDAO {

	public static void save(User user) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		try {
			session.persist(user);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
		} finally {
			session.close();
		}
	}

	// select * from users where id = ?6
	public static User findById(int id) {
		Session session = HibernateUtil.getSession();
		User user = session.find(User.class, id);// find
		return user;
	}

	
	// select * from users : List<User>
	public static List<User> findAll() {
		Session session = HibernateUtil.getSession();
//		Query<User> user = session.createQuery("select u from com.touristapp.model.User u", User.class);
		Query<User> user = session.createNativeQuery("select u.* from users u", User.class);
		return user.getResultList();
	}

	// @Query("select u from User u")
	public static User findByEmailAndPassword(String email, String password) {
		Session session = HibernateUtil.getSession();
//		 Query<User> query = session.createQuery("select u from com.touristapp.model.User u where u.email =?1 and password =?2", User.class);
//		Query<User> query = session.createNativeQuery("select u.* from users u where email = ? and password = ?", User.class);
		Query<User> query = session.createNamedQuery("findByEmailAndPassword", User.class);
		query.setParameter(1, email);
		query.setParameter(2, password);
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("No record exists");
		}
		return user;

	}

	public static long count() {
		Session session = HibernateUtil.getSession();

		Query<Long> count = session.createNativeQuery("select count(*) from users u", Long.class);
		return count.uniqueResult();
	}

	public static List<String> findEmailIds() {
		Session session = HibernateUtil.getSession();

		Query<String> count = session.createNativeQuery("select email from users u", String.class);
		return count.getResultList();
	}

	public static void delete(int id) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		User user = session.find(User.class, id);
		session.remove(user);

		tx.commit();
		session.close();
	}

	public static void update(User user) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		session.merge(user);

		tx.commit();
		session.close();
	}

	public static List<User> getUsers(String role) {
		Session session = HibernateUtil.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class); // return user object

		Root<User> root = query.from(User.class);

		// select * from users where email = ?
		query.select(root).where(builder.equal(root.get("role"), role));
		// order by name asc;
		query.orderBy(builder.asc(root.get("name")));
		Query<User> createQuery = session.createQuery(query);

		// pagination 1 -10 rows
		createQuery.setFirstResult(0);
		createQuery.setMaxResults(10);

		List<User> userList = createQuery.getResultList();
		return userList;

	}
}
