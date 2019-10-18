/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.db.dao.jdbc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * Factory for JdbcDao objects
 */
public class JdbcDaoFactory {

	private static BasicDataSource dataSource;
	private static Map<Class<? extends JdbcDao<?>>, JdbcDao<?>> instances;
	
	private JdbcDaoFactory() {
		//Enforces non instantiability
	}
	
	/**
	 * Inizializes the factory. Cannot be initialized twice without freeing its resources first.
	 * 
	 * @param driverClassName Name of the Jdbc driver class
	 * @param dbUrl complete uri of the db to which connect
	 * @param username db username
	 * @param password db password
	 * @throws DaoFactoryException if the driver is unable to create a conncetion to the db with the provided parameters
	 */
	public static synchronized void init(String driverClassName, String dbUrl, String username, String password) throws DaoFactoryException {
		if(dataSource != null) {
			throw new IllegalStateException("JdbcDaoFactory must be initialized exactly once");
		}
		
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		instances = new HashMap<>();
	}
	
	/**
	 * Releases all the resources associated with the database connection.
	 * The Factory must not be already closed
	 */
	public static synchronized void close() throws DaoFactoryException {
		if(dataSource == null) {
			throw new IllegalStateException("JdbcDaoFactory is not open");
		}
		
		try {
			dataSource.close();
		} catch(SQLException e) {
			throw new DaoFactoryException("Cannot close factory", e);
		}
	}
	
	/**
	 * Begins a new Transaction that can be used with the Dao objects obtained through the current class
	 * 
	 * @return a new Transaction instance
	 * @throws DaoFactoryException In case it is not possible to begin a new Transaction
	 */
	public static Transaction beginTransaction() throws DaoFactoryException {
		Transaction transaction = null;
		try {
			transaction = new Transaction(dataSource);
		} catch(SQLException e) {
			throw new DaoFactoryException("Cannot begin a new transaction", e);
		}

		return transaction;
	}
	
	/**
	 * Obtains an instance of a given JdbcDao.
	 * The required JdbcDao must provide a constructor that accepts DataSource as its only argument
	 * 
	 * @param <E> A subclass of JdbcDao for which an instance is needed
	 * @param requiredClass The class of the required instance
	 * @return An instance of the required subclass of JdbcDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	private static synchronized <E extends JdbcDao<?>> E getDao(Class<E> requiredClass) throws DaoFactoryException {
		if(dataSource == null) {
			throw new IllegalStateException("JdbcDaoFactory must be initialized before instantiating a Dao");
		}
		
		try {
			//If the instance does not exist a new one is created
			if(!instances.containsKey(requiredClass)) {
				//The Dao to create must have a constructor with exactly one parameter of type DataSource
				E instance = requiredClass.getConstructor(DataSource.class).newInstance(dataSource); 
				instances.put(requiredClass, instance);
			}
		} catch(Exception e) {
			throw new DaoFactoryException("Cannot create a new instance of the required Dao", e);
		} 
		
		return (E) instances.get(requiredClass);
	}
	
	/**
	 * Obtains an instance of JdbcAddressDao
	 * 
	 * @return An instance of JdbcAddressDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcAddressDao getAddressDao() throws DaoFactoryException {
		return getDao(JdbcAddressDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcAdministratorDao
	 * 
	 * @return An instance of JdbcAdministratorDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcAdministratorDao getAdministratorDao() throws DaoFactoryException {
		return getDao(JdbcAdministratorDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcCartEntryDao
	 * 
	 * @return An instance of JdbcCartEntryDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcCartEntryDao getCartEntryEntityDao() throws DaoFactoryException {
		return getDao(JdbcCartEntryDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcIssueDao
	 * 
	 * @return An instance of JdbcIssueDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcIssueDao getIssueDao() throws DaoFactoryException {
		return getDao(JdbcIssueDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcItemDao
	 * 
	 * @return An instance of JdbcItemDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcItemDao getItemDao() throws DaoFactoryException {
		return getDao(JdbcItemDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcItemReviewDao
	 * 
	 * @return An instance of JdbcItemReviewDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcItemReviewDao getItemReviewDao() throws DaoFactoryException {
		return getDao(JdbcItemReviewDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcNotificationDao
	 * 
	 * @return An instance of JdbcNotificationDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcNotificationDao getNotificationDao() throws DaoFactoryException {
		return getDao(JdbcNotificationDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcResourceDao
	 * 
	 * @return An instance of JdbcResourceDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcResourceDao getResourceDao() throws DaoFactoryException {
		return getDao(JdbcResourceDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopDao
	 * 
	 * @return An instance of JdbcShopDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopDao getShopDao() throws DaoFactoryException {
		return getDao(JdbcShopDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopItemDao
	 * 
	 * @return An instance of JdbcShopItemDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopItemDao getShopItemDao() throws DaoFactoryException {
		return getDao(JdbcShopItemDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopOrderDao
	 * 
	 * @return An instance of JdbcShopOrderDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopOrderDao getShopOrderDao() throws DaoFactoryException {
		return getDao(JdbcShopOrderDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopOrderItemDao
	 * 
	 * @return An instance of JdbcShopOrderItemDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopOrderItemDao getShopOrderItemDao() throws DaoFactoryException {
		return getDao(JdbcShopOrderItemDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopOrderStatusDao
	 * 
	 * @return An instance of JdbcShopOrderStatusDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopOrderStatusDao getShopOrderStatusDao() throws DaoFactoryException {
		return getDao(JdbcShopOrderStatusDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopReviewDao
	 * 
	 * @return An instance of JdbcShopReviewDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopReviewDao getShopReviewDao() throws DaoFactoryException {
		return getDao(JdbcShopReviewDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcShopShipmentTypeDao
	 * 
	 * @return An instance of JdbcShopShipmentTypeDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcShopShipmentTypeDao getShopShipmentTypeDao() throws DaoFactoryException {
		return getDao(JdbcShopShipmentTypeDao.class);
	}
	
	/**
	 * Obtains an instance of JdbcUserDao
	 * 
	 * @return An instance of JdbcUserDao
	 * @throws DaoFactoryException In case it is not possible to obtain the instance
	 */
	public static synchronized JdbcUserDao getUserDao() throws DaoFactoryException {
		return getDao(JdbcUserDao.class);
	}
	
}
