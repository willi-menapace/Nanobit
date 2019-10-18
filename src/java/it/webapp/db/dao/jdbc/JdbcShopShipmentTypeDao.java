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

import it.webapp.db.dao.ShopShipmentTypeDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


public class JdbcShopShipmentTypeDao extends JdbcDao<ShopShipmentTypeEntity> implements ShopShipmentTypeDao {

	private static final String GET_BY_SHOP_AND_SHYPMENT_TYPE_PQUERY =
			"SELECT shop_id, shipment_type_id, price\n" +
			"FROM shop_shipment_types\n" +
			"WHERE shop_id = ? AND shipment_type_id = ?";
	
	private static final String GET_BY_SHOP_ID_PQUERY =
			"SELECT shop_id, shipment_type_id, price\n" +
			"FROM shop_shipment_types\n" +
			"WHERE shop_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_shipment_types (shop_id, shipment_type_id, price)\n" +
			"VALUES (?, ?, ?)";
	
	private static final String DELETE_PQUERY =
			"DELETE FROM shop_shipment_types\n" +
			"WHERE shop_id = ? AND shipment_type_id = ?";
	
	private static final String UPDATE_PQUERY =
			"UPDATE shop_shipment_types SET\n" +
			"price = ?\n" +
			"WHERE shop_id = ? AND shipment_type_id = ?";
	
	public JdbcShopShipmentTypeDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopShipmentTypeEntity getByShopAndShipmentType(int shopId, ShipmentType shipmentType) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopShipmentTypeEntity result = getByShopAndShipmentType(shopId, shipmentType, connection);
			return result;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopShipmentType", e);
		}
	}
	
	public ShopShipmentTypeEntity getByShopAndShipmentType(int shopId, ShipmentType shipmentType, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopShipmentTypeEntity result = getByShopAndShipmentType(shopId, shipmentType, connection);
		return result;
	}
	
	public ShopShipmentTypeEntity getByShopAndShipmentType(int shopId, ShipmentType shipmentType, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopShipmentTypeEntity shopShipmentType = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_AND_SHYPMENT_TYPE_PQUERY)) {
			
			query.setInt(1, shopId);
			query.setInt(2, shipmentType.getId());
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopShipmentType = Entities.getShopShipmentTypeFromResultSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopShipmentType", e);
		}
		
		return shopShipmentType;
	}
	
	@Override
	public List<ShopShipmentTypeEntity> getByShopId(int shopId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopShipmentTypeEntity> results = getByShopId(shopId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopShipmentType", e);
		}
	}
	
	public List<ShopShipmentTypeEntity> getByShopId(int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopShipmentTypeEntity> results = getByShopId(shopId, connection);
		return results;
	}
	
	public List<ShopShipmentTypeEntity> getByShopId(int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopShipmentTypeEntity> shopShipmentTypes = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopShipmentTypeEntity resultEntity = Entities.getShopShipmentTypeFromResultSet(results, "");
					shopShipmentTypes.add(resultEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopShipmentType", e);
		}
		
		return shopShipmentTypes;
	}

	@Override
	public void insert(ShopShipmentTypeEntity shopShipmentType) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(shopShipmentType, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopShipmentType", e);
		}
	}
	
	public void insert(ShopShipmentTypeEntity shopShipmentType, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(shopShipmentType, connection);
	}
	
	public void insert(ShopShipmentTypeEntity shopShipmentType, Connection connection) throws DaoException {
		if(shopShipmentType == null) {
			throw new IllegalArgumentException("ShopShipmentType cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Inserts the record
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setInt(1, shopShipmentType.getShopId());
			query.setInt(2, shopShipmentType.getShipmentType().getId());
			query.setBigDecimal(3, shopShipmentType.getPrice());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert ShopShipmentType", e);
		}
	}

	@Override
	public void delete(int shopId, ShipmentType shipmentType) throws DaoException {
		try(Connection connection = getConnection()) {
			
			delete(shopId, shipmentType, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot delete ShopShipmentType", e);
		}
	}
	
	public void delete(int shopId, ShipmentType shipmentType, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		delete(shopId, shipmentType, connection);
	}
	
	public void delete(int shopId, ShipmentType shipmentType, Connection connection) throws DaoException {

		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Deletes the record
		try(PreparedStatement query = connection.prepareStatement(DELETE_PQUERY)) {
			
			query.setInt(1, shopId);
			query.setInt(2, shipmentType.getId());
			
			int removedRecords = query.executeUpdate();
			if(removedRecords != 1) {
				throw new DaoException("An unexpected number of records have been deleted: " + removedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot delete ShopShipmentType", e);
		}
	}

	@Override
	public void update(ShopShipmentTypeEntity shopShipmentType) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(shopShipmentType, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update ShopShipmentType", e);
		}
	}
	
	public void update(ShopShipmentTypeEntity shopShipmentType, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(shopShipmentType, connection);
	}
	
	public void update(ShopShipmentTypeEntity shopShipmentType, Connection connection) throws DaoException {
		if(shopShipmentType == null) {
			throw new IllegalArgumentException("ShopShipmentType cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			
			query.setBigDecimal(1, shopShipmentType.getPrice());
			query.setInt(2, shopShipmentType.getShopId());
			query.setInt(3, shopShipmentType.getShipmentType().getId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update ShopShipmentType", e);
		}
	}

}
