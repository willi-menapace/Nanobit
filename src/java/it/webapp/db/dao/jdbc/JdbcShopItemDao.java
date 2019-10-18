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

import it.webapp.db.dao.ShopItemDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ShopItemEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcShopItemDao extends JdbcDao<ShopItemEntity> implements ShopItemDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT shop_item_id, shop_id, item_id, price, availability\n" +
			"FROM shop_items\n" +
			"WHERE shop_item_id = ?";
	
	private static final String GET_BY_SHOP_ID_PQUERY =
			"SELECT shop_item_id, shop_id, item_id, price, availability\n" +
			"FROM shop_items\n" +
			"WHERE shop_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_items (shop_item_id, shop_id, item_id, price, availability)\n" +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PQUERY =
			"UPDATE shop_items SET\n" +
			"shop_id = ?,\n" +
			"item_id = ?,\n" +
			"price = ?,\n" +
			"availability = ?\n" +
			"WHERE shop_item_id = ?";
			
	private static final String DELETE_PQUERY =
			"DELETE from shop_items\n" +
			"WHERE shop_item_id = ?";
        
        private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(shop_item_id) + 1) AS next_id\n" +
			"FROM shop_items";
	
	public JdbcShopItemDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopItemEntity getById(int shopItemId) throws DaoException {
		
		try(Connection connection = getConnection()) {
			ShopItemEntity shopItem = getById(shopItemId, connection);
			return shopItem;
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopItem", e);
		}
	}
	
	public ShopItemEntity getById(int shopItemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopItemEntity shopItem = getById(shopItemId, connection);
		return shopItem;
	}
	
	public ShopItemEntity getById(int shopItemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopItemEntity shopItem = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, shopItemId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopItem = Entities.getShopItemFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopItem", e);
		}
		
		return shopItem;
	}

	@Override
	public List<ShopItemEntity> getByShopId(int shopId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopItemEntity> results = getByShopId(shopId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopItem", e);
		}
	}
	
	public List<ShopItemEntity> getByShopId(int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopItemEntity> results = getByShopId(shopId, connection);
		return results;
	}
	
	public List<ShopItemEntity> getByShopId(int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopItemEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopItemEntity shopItemEntity = Entities.getShopItemFromResultsSet(results, "");
					resources.add(shopItemEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopItems", e);
		}
		
		return resources;
	}

	@Override
	public int insert(ShopItemEntity shopItem) throws DaoException {
		try(Connection connection = getConnection()) {
			
			int insertedId = insert(shopItem, connection);
			return insertedId;
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopItem", e);
		}
	}
	
	public int insert(ShopItemEntity shopItem, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(shopItem, connection);
                return insertedId;
	}
	
	public int insert(ShopItemEntity shopItem, Connection connection) throws DaoException {
		if(shopItem == null) {
			throw new IllegalArgumentException("ShopItem cannot be null");
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
                        query.setInt(2, shopItem.getShopId());
                        query.setInt(3, shopItem.getItemId());
                        query.setBigDecimal(4, shopItem.getPrice());
                        query.setInt(5, shopItem.getAvailability());

                        int updatedRecords = query.executeUpdate();
                        if(updatedRecords != 1) {
                                throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
                        }

                    }  
                   
                    return nextId;
                    
                } catch(SQLException e) {
                    throw new DaoException("Cannot insert ShopItem", e);
                }
	}

	@Override
	public void update(ShopItemEntity shopItem) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(shopItem, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update ShopItem", e);
		}
	}
	
	public void update(ShopItemEntity shopItem, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(shopItem, connection);
	}
	
	public void update(ShopItemEntity shopItem, Connection connection) throws DaoException {
		if(shopItem == null) {
			throw new IllegalArgumentException("ShopItem cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			
			query.setInt(1, shopItem.getShopId());
			query.setInt(2, shopItem.getItemId());
			query.setBigDecimal(3, shopItem.getPrice());
			query.setInt(4, shopItem.getAvailability());
			query.setInt(5, shopItem.getShopItemId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update ShopItem", e);
		}
	}

	@Override
	public void delete(int shopItemId) throws DaoException {
			try(Connection connection = getConnection()) {
			
			delete(shopItemId, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot delete ShopItem", e);
		}
	}
	
	public void delete(int shopItemId, Transaction transaction) throws DaoException {
			if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		delete(shopItemId, connection);
	}
	
	public void delete(int shopItemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Deletes the record
		try(PreparedStatement query = connection.prepareStatement(DELETE_PQUERY)) {
			
			query.setInt(1, shopItemId);
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been deleted: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot delete ShopItem", e);
		}
	}

}
