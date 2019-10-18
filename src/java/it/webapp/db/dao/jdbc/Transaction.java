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

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Class that represents a transaction.
 * Can be used to perform multiple actions as atomic ones.
 */
public class Transaction implements AutoCloseable {
	
	private Connection connection;
	
	/**
	 * Begins a transaction
	 * 
	 * @param dataSource The source to use to obtain a connection object
	 * @throws SQLException In case it is not possible to obtain the connection or initiate the transaction
	 */
	Transaction(DataSource dataSource) throws SQLException{
		if(dataSource == null) {
			throw new IllegalArgumentException("Connection must be non null");
		}
		
		this.connection = dataSource.getConnection();
		connection.setAutoCommit(false);
	}
	
	/**
	 * Makes the changes performed from the last commit, the last rollback or the beginning of the transaction permanent
	 * 
	 * @throws SQLException In case it is not possible to perform the commit
	 */
	public void commit() throws SQLException {
		connection.commit();
	}
	
	/**
	 * Reverts all the changes made since the transaction was created or commit was calles
	 * 
	 * @throws SQLException In case it is not possible to perform the rollback
	 */
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
	/**
	 * Commits and frees the resources associated with the transaction
	 * 
	 * @throws SQLException In case the resources cannot be freed
	 */
	@Override
	public void close() throws SQLException {
		//Commit must be explicit before closing the connection
		connection.commit();
		connection.close();
	}
	
	/**
	 * Returns the connection to user to perform actions in the current transaction
	 * 
	 * @return The connection to use, must not be closed
	 */
	Connection getConnection() {
		return connection;
	}
	
}
