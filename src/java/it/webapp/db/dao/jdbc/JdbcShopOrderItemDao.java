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

import it.webapp.db.dao.ShopOrderItemDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ItemReviewEntity;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopOrderItemAggregateInfo;
import it.webapp.db.entities.ShopOrderItemEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcShopOrderItemDao extends JdbcDao<ShopOrderItemEntity> implements ShopOrderItemDao {

	private static final String GET_BY_ID_PQUERY =
		"SELECT shop_order_item_id, shop_order_id, item_id, price, quantity\n" +
		"FROM shop_order_items\n" +
		"WHERE shop_order_item_id = ?";
	
	private static final String GET_BY_SHOP_ORDER_ID_PQUERY =
			"SELECT shop_order_item_id, shop_order_id, item_id, price, quantity\n" +
			"FROM shop_order_items\n" +
			"WHERE shop_order_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_order_items (shop_order_item_id, shop_order_id, item_id, price, quantity)\n" +
			"VALUES (?, ?, ?, ?, ?)";
        
        private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(shop_order_item_id) + 1) AS next_id\n" +
			"FROM shop_order_items";
	
	public JdbcShopOrderItemDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopOrderItemEntity getById(int shopOrderItemId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopOrderItemEntity shopOrderItem = getById(shopOrderItemId, connection);
			return shopOrderItem;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrderItem", e);
		}
	}
	
	public ShopOrderItemEntity getById(int shopOrderItemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopOrderItemEntity shopOrderItem = getById(shopOrderItemId, connection);
		return shopOrderItem;
	}
	
	public ShopOrderItemEntity getById(int shopOrderItemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopOrderItemEntity shopOrderItem = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, shopOrderItemId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopOrderItem = Entities.getShopOrderItemFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrderItem", e);
		}
		
		return shopOrderItem;
	}
	
	@Override
	public List<ShopOrderItemEntity> getByShopOrderId(int shopOrderId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopOrderItemEntity> results = getByShopOrderId(shopOrderId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrderItems", e);
		}
	}
	
	public List<ShopOrderItemEntity> getByShopOrderId(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopOrderItemEntity> results = getByShopOrderId(shopOrderId, connection);
		return results;
	}
	
	public List<ShopOrderItemEntity> getByShopOrderId(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopOrderItemEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ORDER_ID_PQUERY)) {
			
			query.setInt(1, shopOrderId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopOrderItemEntity shopOrderItemEntity = Entities.getShopOrderItemFromResultsSet(results, "");
					resources.add(shopOrderItemEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopOrderItems", e);
		}
		
		return resources;
	}

	@Override
	public List<ShopOrderItemAggregateInfo> getAggregateInfoByShopOrderId(int shopOrderId) throws DaoException {
		//Operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				List<ShopOrderItemAggregateInfo> results = getAggregateInfoByShopOrderId(shopOrderId, connection);
				return results;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopOrderItemAggregateInfo", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public List<ShopOrderItemAggregateInfo> getAggregateInfoByShopOrderId(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopOrderItemAggregateInfo> results = getAggregateInfoByShopOrderId(shopOrderId, connection);
		return results;
	}
		
	public List<ShopOrderItemAggregateInfo> getAggregateInfoByShopOrderId(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try {
			JdbcItemDao itemDao = JdbcDaoFactory.getItemDao();
			JdbcItemReviewDao itemReviewDao = JdbcDaoFactory.getItemReviewDao();
			JdbcResourceDao resourceDao = JdbcDaoFactory.getResourceDao();

			List<ShopOrderItemAggregateInfo> resources = new ArrayList<>();

			//Obtains every shop order item
			List<ShopOrderItemEntity> shopOrderItems = getByShopOrderId(shopOrderId, connection);

			//For each shop order item obtains its related information
			for(ShopOrderItemEntity currentShopOrderItem : shopOrderItems) {

				ItemEntity item = itemDao.getById(currentShopOrderItem.getItemId(), connection);
				
				//Handles the case where there is no image available
				ResourceEntity image = null;
				List<ResourceEntity> images = resourceDao.getByItemId(item.getItemId(), connection);
				if(images.isEmpty() == false) {
					image = images.get(0);
				}
				
				ItemReviewEntity itemReview = itemReviewDao.getByShopOrderItemId(currentShopOrderItem.getShopOrderItemId(), connection);

				ShopOrderItemAggregateInfo shopOrderItemAggregate = new ShopOrderItemAggregateInfo(currentShopOrderItem, item, image, itemReview);
				resources.add(shopOrderItemAggregate);	
			}

			return resources;
		} catch(DaoFactoryException e) {
			throw new DaoException("Could not obtain necessary daos", e);
		}
	}
	
	@Override
	public int insert(ShopOrderItemEntity shopOrderItem) throws DaoException {
		try(Connection connection = getConnection()) {
			
			int insertedId = insert(shopOrderItem, connection);
			return insertedId;
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopOrderItem", e);
		}
	}
	
	public int insert(ShopOrderItemEntity shopOrderItem, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(shopOrderItem, connection);
                return insertedId;
	}
	
	public int insert(ShopOrderItemEntity shopOrderItem, Connection connection) throws DaoException {
		if(shopOrderItem == null) {
			throw new IllegalArgumentException("ShopOrderItem cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
                
                try {
                    
                    //The id to use for the next record
                    int nextId = 0;
                    
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
	
                    //Inserts the record
                    try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
                        query.setInt(1, nextId);
                        query.setInt(2, shopOrderItem.getShopOrderId());
                        query.setInt(3, shopOrderItem.getItemId());
                        query.setBigDecimal(4, shopOrderItem.getPrice());
                        query.setInt(5, shopOrderItem.getQuantity());

                        int updatedRecords = query.executeUpdate();
                        if(updatedRecords != 1) {
                                throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
                        }

                    }
                    
                    return nextId;

		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopOrderItem", e);
                }   
	
    }
       
}
