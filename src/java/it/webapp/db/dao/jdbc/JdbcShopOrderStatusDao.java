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

import it.webapp.db.dao.ShopOrderStatusDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ShopOrderStatusEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcShopOrderStatusDao extends JdbcDao<ShopOrderStatusEntity> implements ShopOrderStatusDao {

	private static final String GET_BY_SHOP_ORDER_ID_PQUERY =
			"SELECT shop_order_id, shop_order_status_type_id, change_date\n" +
			"FROM shop_order_statuses\n" +
			"WHERE shop_order_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_order_statuses (shop_order_id, shop_order_status_type_id, change_date)\n" +
			"VALUES (?, ?, ?)";

	public JdbcShopOrderStatusDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public List<ShopOrderStatusEntity> getByShopOrderId(int shopOrderId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopOrderStatusEntity> results = getByShopOrderId(shopOrderId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrderStatus", e);
		}
	}
	
	public List<ShopOrderStatusEntity> getByShopOrderId(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopOrderStatusEntity> results = getByShopOrderId(shopOrderId, connection);
		return results;
	}
	
	public List<ShopOrderStatusEntity> getByShopOrderId(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopOrderStatusEntity> shopOrderStatuses = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ORDER_ID_PQUERY)) {
			
			query.setInt(1, shopOrderId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopOrderStatusEntity resultEntity = Entities.getShopOrderStatusFromResultSet(results, "");
					shopOrderStatuses.add(resultEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrderStatus", e);
		}
		
		return shopOrderStatuses;
	
	}

	@Override
	public void insert(ShopOrderStatusEntity shopOrderStatus) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(shopOrderStatus, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopShipmentType", e);
		}
	}
	
	public void insert(ShopOrderStatusEntity shopOrderStatus, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(shopOrderStatus, connection);
	}
	
	public void insert(ShopOrderStatusEntity shopOrderStatus, Connection connection) throws DaoException {
		if(shopOrderStatus == null) {
			throw new IllegalArgumentException("ShopOrderStatus cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Inserts the record
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setInt(1, shopOrderStatus.getShopOrderId());
			query.setInt(2, shopOrderStatus.getShopOrderStatus().getId());
			query.setDate(3, new java.sql.Date(shopOrderStatus.getChangeDate().getTime()));
			
			int insertedRecords = query.executeUpdate();
			if(insertedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + insertedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert ShopOrderStatus", e);
		}
	}

}
