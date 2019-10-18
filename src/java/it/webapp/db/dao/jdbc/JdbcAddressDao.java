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

import it.webapp.db.dao.AddressDao;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.Entities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcAddressDao extends JdbcDao<AddressEntity> implements AddressDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT address_id, street, street_number, city, district, zip, country, latitude, longitude\n" +
			"FROM addresses\n" +
			"WHERE address_id = ?";
	
	private static final String GET_BY_USER_ID_PQUERY =
		"SELECT address_id, street, street_number, city, district, zip, country, latitude, longitude\n" +
		"FROM users INNER JOIN addresses USING (address_id)\n" +
		"WHERE user_id = ?";
	
	private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(address_id) + 1) AS next_id\n" +
			"FROM addresses";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO addresses (address_id, street, street_number, city, district, zip, country, latitude, longitude)\n" +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PQUERY =
			"UPDATE addresses SET\n" +
			"street = ?,\n" +
			"street_number = ?,\n" +
			"city = ?,\n" +
			"district = ?,\n" +
			"zip = ?,\n" +
			"country = ?,\n" +
			"latitude = ?,\n" +
			"longitude = ?\n" +
			"WHERE address_id = ?";
	
	public JdbcAddressDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public AddressEntity getById(int addressId) throws DaoException {
		AddressEntity address = null;
		
		try(Connection connection = getConnection()) {
			
			address = getById(addressId, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Address", e);
		}
		
		return address;
		
	}
	
	public AddressEntity getById(int addressId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		try {
			AddressEntity address = getById(addressId, connection);
		
			return address;
			
		} catch (SQLException e) {
			throw new DaoException("Cannot retrieve Address", e);
		}
	}
	
	private AddressEntity getById(int addressId, Connection connection) throws SQLException {
		
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		AddressEntity address = null;
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, addressId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					address = Entities.getAddressFromResultSet(results, "");
				}
			}
		}
		
		return address;
		
	}
	
	@Override
	public AddressEntity getByUserId(int userId) throws DaoException {
		AddressEntity address = null;
		
		try(Connection connection = getConnection()) {
			
			address = getByUserId(userId, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Address", e);
		}
		
		return address;
		
	}
	
	public AddressEntity getByUserId(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		try {
			AddressEntity address = getByUserId(userId, connection);
		
			return address;
			
		} catch (SQLException e) {
			throw new DaoException("Cannot retrieve Address", e);
		}
	}
	
	private AddressEntity getByUserId(int userId, Connection connection) throws SQLException {
		
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		AddressEntity address = null;
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USER_ID_PQUERY)) {
			
			query.setInt(1, userId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					address = Entities.getAddressFromResultSet(results, "");
				}
			}
		}
		
		return address;
		
	}

	@Override
	public int insert(AddressEntity address) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				int insertedId = insert(address, connection);
				return insertedId;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Address", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public int insert(AddressEntity address, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(address, connection);
		return insertedId;
	}
	
	public int insert(AddressEntity address, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try {
			
			//The id to use for the next record
			int nextId = 0;
			
			//Associates the resource with its shop
			try(PreparedStatement query = connection.prepareStatement(GET_NEXT_ID_PQUERY)) {
				try(ResultSet results = query.executeQuery()) {
				
					if(results.next()) {
						nextId = results.getInt("next_id");
					} else {
						throw new DaoException("Cannot retrieve next id to use");
					}
				}
			}
                        
                        //If nextId == 0 it means that table is empty
                        if(nextId == 0) {
                            nextId = 1;
                        }
			
			//Associates the resource with its shop
			try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
				query.setInt(1, nextId);
				query.setString(2, address.getStreet());
				query.setString(3, address.getStreetNumber());
				query.setString(4, address.getCity());
				query.setString(5, address.getDistrict());
				query.setString(6, address.getZip());
				query.setString(7, address.getCountry());
				query.setBigDecimal(8, address.getLatitude());
				query.setBigDecimal(9, address.getLongitude());

				int insertedRecords = query.executeUpdate();
				if(insertedRecords != 1) {
					throw new DaoException("Unexpected number of inserted records, only 1 should be inserted: " + insertedRecords);
				}
			}
			
			return nextId;
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Address", e);
		}
	}
	
	@Override
	public void update(AddressEntity address) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(address, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update Address", e);
		}
	}
	
	public void update(AddressEntity address, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		try {
			update(address, connection);
		} catch(SQLException e) {
			throw new DaoException("Cannot update Address", e);
		}
	}
	
	private void update(AddressEntity address, Connection connection) throws SQLException {
		if(address == null) {
			throw new IllegalArgumentException("Address cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			query.setString(1, address.getStreet());
			query.setString(2, address.getStreetNumber());
			query.setString(3, address.getCity());
			query.setString(4, address.getDistrict());
			query.setString(5, address.getZip());
			query.setString(6, address.getCountry());
			query.setBigDecimal(7, address.getLatitude());
			query.setBigDecimal(8, address.getLongitude());
			query.setInt(9, address.getAddressId());
			
			int rowsChanged = query.executeUpdate();
			if(rowsChanged != 1) {
				throw new SQLException("More or less than 1 record have been updated: " + rowsChanged);
			}
		}
	}

}
