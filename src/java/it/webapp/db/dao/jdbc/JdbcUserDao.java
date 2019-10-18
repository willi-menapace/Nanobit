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

import it.webapp.db.dao.UserDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;

public class JdbcUserDao extends JdbcDao<UserEntity> implements UserDao {

	private static final String GET_BY_ID_PQUERY = 
			"SELECT user_id, username, password_hash, first_name, last_name, address_id, email, registration_date, registration_code, activated, password_reset_code\n" +
			"FROM users\n" +
			"WHERE user_id = ?";
	
	private static final String GET_BY_USERNAME_PQUERY = 
			"SELECT user_id, username, password_hash, first_name, last_name, address_id, email, registration_date, registration_code, activated, password_reset_code\n" +
			"FROM users\n" +
			"WHERE username = ?";
	
	private static final String GET_BY_USERNAME_AND_PASSWORD_PQUERY =
			"SELECT user_id, username, password_hash, first_name, last_name, address_id, email, registration_date, registration_code, activated, password_reset_code\n" +
			"FROM users\n" +
			"WHERE username = ? AND password_hash = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO users (username, password_hash, first_name, last_name, address_id, email, registration_date, registration_code, activated, password_reset_code)\n" +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String GET_INSERTED_ID =
			"SELECT user_id\n" +
			"FROM users\n" +
			"WHERE username = ?";
	
	private static final String UPDATE_PQUERY =
			"UPDATE users SET\n" +
			"username = ?,\n" +
			"password_hash = ?,\n" +
			"first_name = ?,\n" +
			"last_name = ?,\n" +
			"address_id = ?,\n" +
			"email = ?,\n" +
			"registration_date = ?,\n" +
			"registration_code = ?,\n" +
			"activated = ?,\n" +
			"password_reset_code = ?\n" +
			"WHERE user_id = ?";
	
	public JdbcUserDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public UserEntity getById(int userId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			UserEntity user = getById(userId, connection);
			return user;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
	}
	
	public UserEntity getById(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		UserEntity user = getById(userId, connection);
		return user;
	}
	
	public UserEntity getById(int userId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		UserEntity user = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, userId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					user = Entities.getUserFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
		
		return user;
	}
	
	
	@Override
	public UserEntity getByUsername(String username) throws DaoException {
		try(Connection connection = getConnection()) {
			
			UserEntity user = getByUsername(username, connection);
			return user;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
	}
	
	public UserEntity getByUsername(String username, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		UserEntity user = getByUsername(username, connection);
		return user;
	}
	
	public UserEntity getByUsername(String username, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		UserEntity user = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USERNAME_PQUERY)) {
			
			query.setString(1, username);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					user = Entities.getUserFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
		
		return user;
	}
	

	@Override
	public UserEntity getByUsernameAndPassword(String username, HashDigest password) throws DaoException {
		try(Connection connection = getConnection()) {
			
			UserEntity user = getByUsernameAndPassword(username, password, connection);
			return user;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
	}
	
	public UserEntity getByUsernameAndPassword(String username, HashDigest password, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		UserEntity user = getByUsernameAndPassword(username, password, connection);
		return user;
	}
		
	public UserEntity getByUsernameAndPassword(String username, HashDigest password, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		UserEntity user = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USERNAME_AND_PASSWORD_PQUERY)) {
			
			query.setString(1, username);
			query.setBytes(2, password.getBytes());
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					user = Entities.getUserFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve User", e);
		}
		
		return user;
	}
	
	@Override
	public int insert(UserEntity user) throws DaoException {
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				int insertedId = insert(user, connection);
				return insertedId;
				
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert User", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public int insert(UserEntity user, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(user, connection);
		return insertedId;
	}
	
	public int insert(UserEntity user, Connection connection) throws DaoException {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		try {
			//Inserts the record
			try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {

				query.setString(1, user.getUsername());
				query.setBytes(2, user.getPasswordHash().getBytes());
				query.setString(3, user.getFirstName());
				query.setString(4, user.getLastName());
				query.setInt(5, user.getAddressId());
				query.setString(6, user.getEmail());
				query.setDate(7, new java.sql.Date(user.getRegistrationDate().getTime()));
				query.setBytes(8, user.getRegistrationCode().getBytes());
				query.setBoolean(9, user.isActivated());
				//Handles the case in which the code is null
				if(user.getPasswordResetCode() != null) {
					query.setBytes(10, user.getPasswordResetCode().getBytes());
				} else {
					query.setNull(10, Types.BINARY);
				}

				int updatedRecords = query.executeUpdate();
				if(updatedRecords != 1) {
					throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
				}

			}

			//Gets the inserted user id
			try(PreparedStatement query = connection.prepareStatement(GET_INSERTED_ID)) {
				
				query.setString(1, user.getUsername());
				
				try(ResultSet results = query.executeQuery()) {
					if(results.next()) {
						int insertedId = results.getInt("user_id");
						return insertedId;
					} else {
						throw new DaoException("Cannot insert User");
					}
				}
				
			}
		
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert User", e);
		}
	}

	@Override
	public void update(UserEntity user) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(user, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update User", e);
		}
	}
	
	public void update(UserEntity user, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(user, connection);
	}
	
	public void update(UserEntity user, Connection connection) throws DaoException {
		if(user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			
			query.setString(1, user.getUsername());
			query.setBytes(2, user.getPasswordHash().getBytes());
			query.setString(3, user.getFirstName());
			query.setString(4, user.getLastName());
			query.setInt(5, user.getAddressId());
			query.setString(6, user.getEmail());
			query.setDate(7, new java.sql.Date(user.getRegistrationDate().getTime()));
			query.setBytes(8, user.getRegistrationCode().getBytes());
			query.setBoolean(9, user.isActivated());
			
			//Handles the case in which the code is null
			if(user.getPasswordResetCode() != null) {
				query.setBytes(10, user.getPasswordResetCode().getBytes());
			} else {
				query.setNull(10, Types.BINARY);
			}
			
			query.setInt(11, user.getUserId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update User", e);
		}
	}

}
