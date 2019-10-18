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

import it.webapp.db.dao.ShopOrderDao;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ShopOrderAggregateInfo;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemAggregateInfo;
import it.webapp.db.entities.ShopOrderStatusEntity;
import it.webapp.db.entities.ShopReviewEntity;
import it.webapp.db.entities.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcShopOrderDao extends JdbcDao<ShopOrderEntity> implements ShopOrderDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT shop_order_id, date, user_id, shop_id, address_id, shipment_type_id, shipment_price\n" +
			"FROM shop_orders\n" +
			"WHERE shop_order_id = ?";
	
	private static final String GET_BY_SHOP_ID_PQUERY =
			"SELECT shop_order_id, date, user_id, shop_id, address_id, shipment_type_id, shipment_price\n" +
			"FROM shop_orders\n" +
			"WHERE shop_id = ?\n" +
			"ORDER BY date DESC\n" +
			"LIMIT ?, ?";
	
	private static final String GET_BY_USER_ID_PQUERY =
			"SELECT shop_order_id, date, user_id, shop_id, address_id, shipment_type_id, shipment_price\n" +
			"FROM shop_orders\n" +
			"WHERE user_id = ?\n" +
			"ORDER BY date DESC\n" +
			"LIMIT ?, ?";
	
	private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(shop_order_id) + 1) AS next_id\n" +
			"FROM shop_orders";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_orders (shop_order_id, date, user_id, shop_id, address_id, shipment_type_id, shipment_price)\n" +
			"VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	public JdbcShopOrderDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopOrderEntity getById(int shopOrderId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopOrderEntity shopOrder = getById(shopOrderId, connection);
			return shopOrder;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrder", e);
		}
	}
	
	public ShopOrderEntity getById(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopOrderEntity shopOrder = getById(shopOrderId, connection);
		return shopOrder;
	}
	
	public ShopOrderEntity getById(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopOrderEntity shopOrder = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, shopOrderId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopOrder = Entities.getShopOrderFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrder", e);
		}
		
		return shopOrder;
	}

	@Override
	public List<ShopOrderEntity> getByShopId(int shopId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopOrderEntity> results = getByShopId(shopId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrders", e);
		}
	}
	
	public List<ShopOrderEntity> getByShopId(int shopId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopOrderEntity> results = getByShopId(shopId, offset, count, connection);
		return results;
	}
		
	public List<ShopOrderEntity> getByShopId(int shopId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopOrderEntity> resources = new ArrayList();	
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopOrderEntity shopOrder = Entities.getShopOrderFromResultsSet(results, "");
					resources.add(shopOrder);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrders", e);
		}
		
		return resources;
	}

	@Override
	public List<ShopOrderEntity> getByUserId(int userId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopOrderEntity> results = getByUserId(userId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrders", e);
		}
	}
	
	public List<ShopOrderEntity> getByUserId(int userId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopOrderEntity> results = getByUserId(userId, offset, count, connection);
		return results;
	}
	
	public List<ShopOrderEntity> getByUserId(int userId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopOrderEntity> resources = new ArrayList();	
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USER_ID_PQUERY)) {
			
			query.setInt(1, userId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopOrderEntity shopOrder = Entities.getShopOrderFromResultsSet(results, "");
					resources.add(shopOrder);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrders", e);
		}
		
		return resources;
	}

	@Override
	public ShopOrderAggregateInfo getAggregateInfoById(int shopOrderId) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				ShopOrderAggregateInfo shopOrderAggregateInfo = getAggregateInfoById(shopOrderId, connection);
				return shopOrderAggregateInfo;
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
	
	public ShopOrderAggregateInfo getAggregateInfoById(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopOrderAggregateInfo shopOrderAggregateInfo = getAggregateInfoById(shopOrderId, connection);
		return shopOrderAggregateInfo;
	}
		
	public ShopOrderAggregateInfo getAggregateInfoById(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try {
			
			JdbcAddressDao addressDao = JdbcDaoFactory.getAddressDao();
			JdbcShopReviewDao shopReviewDao = JdbcDaoFactory.getShopReviewDao();
			JdbcShopOrderStatusDao shopOrderStatusDao = JdbcDaoFactory.getShopOrderStatusDao();
			JdbcShopOrderItemDao shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
			JdbcUserDao userDao = JdbcDaoFactory.getUserDao();
                        
			ShopOrderEntity shopOrder = getById(shopOrderId, connection);
			AddressEntity address = addressDao.getById(shopOrder.getAddressId());
			ShopReviewEntity shopReview = shopReviewDao.getByShopOrderId(shopOrderId, connection);
			List<ShopOrderStatusEntity> shopOrderStatuses = shopOrderStatusDao.getByShopOrderId(shopOrderId, connection);
			List<ShopOrderItemAggregateInfo> shopOrderItemAggregateInfo = shopOrderItemDao.getAggregateInfoByShopOrderId(shopOrderId, connection);
                        UserEntity user = userDao.getById(shopOrder.getShopId(), connection);
			
			ShopOrderAggregateInfo aggregateShopOrderInfo = new ShopOrderAggregateInfo(shopOrder, user, address, shopReview, shopOrderStatuses, shopOrderItemAggregateInfo);
			
			return aggregateShopOrderInfo;
		} catch(DaoFactoryException e) {
			throw new DaoException("Could not obtain the necessary daos", e);
		}

	}
	
	@Override
	public int insert(ShopOrderEntity shopOrder) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				int insertedId = insert(shopOrder, connection);
				return insertedId;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopOrder", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public int insert(ShopOrderEntity shopOrder, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(shopOrder, connection);
		return insertedId;
	}
	
	public int insert(ShopOrderEntity shopOrder, Connection connection) throws DaoException {
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
				query.setDate(2, new java.sql.Date(shopOrder.getDate().getTime()));
				query.setInt(3, shopOrder.getUserId());
				query.setInt(4, shopOrder.getShopId());
				query.setInt(5, shopOrder.getAddressId());
                                query.setInt(6, shopOrder.getShipmentType().getId());
                                query.setBigDecimal(7, shopOrder.getShipmentPrice());


				int insertedRecords = query.executeUpdate();
				if(insertedRecords != 1) {
					throw new DaoException("Unexpected number of inserted records, only 1 should be inserted: " + insertedRecords);
				}
			}
			
			return nextId;
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		}
	}

	
	
}
