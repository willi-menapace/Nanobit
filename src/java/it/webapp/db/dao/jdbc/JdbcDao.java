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

import it.webapp.db.dao.Dao;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Object for accessing data entities through Jdbc
 * Every subclass must have a constructor that accepts esactly one parameter of type DataSource
 * 
 * @param <E> The entity type for the Dao 
 */
public abstract class JdbcDao<E> implements Dao<E> {
	
	private DataSource dataSource;
	
	/**
	 * Builds a JdbcDao that will interface with the database using the proviced dataSource
	 * 
	 * @param dataSource the source from which to obtain connections
	 */
	JdbcDao(DataSource dataSource) {
		if(dataSource == null) {
			throw new IllegalArgumentException("The data source cannot be null");
		}
		
		this.dataSource = dataSource;
	}
	
	Connection getConnection() throws DaoException {
		Connection connection = null;
			
		try {
			connection = dataSource.getConnection();
		} catch(SQLException e) {
			throw new DaoException("Cannot open a new connection", e);
		}
		
		return connection;
	}
	
}
