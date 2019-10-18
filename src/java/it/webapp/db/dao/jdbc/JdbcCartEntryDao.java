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

import it.webapp.db.dao.CartEntryDao;
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.CartEntryEntity;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopItemEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcCartEntryDao extends JdbcDao<CartEntryEntity> implements CartEntryDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT cart_entry_id, user_id, shop_item_id, quantity\n" +
			"FROM cart_entries\n" +
			"WHERE cart_entry_id = ?";
	
	private static final String GET_BY_USER_SHOP_ITEM_ID_PQUERY =
			"SELECT cart_entry_id, user_id, shop_item_id, quantity\n" +
			"FROM cart_entries\n" +
			"WHERE user_id = ? AND shop_item_id = ?";
	
	private static final String GET_BY_USER_ID_PQUERY =
			"SELECT cart_entry_id, user_id, shop_item_id, quantity\n" +
			"FROM cart_entries\n" +
			"WHERE user_id = ?";
	
	private static final String GET_AGGREGATE_FROM_UER_ID_PQUERY =
			"SELECT cart_entries.cart_entry_id, cart_entries.user_id, cart_entries.shop_item_id, cart_entries.quantity,\n" +
			"	   shop_items.shop_item_id, shop_items.shop_id, shop_items.item_id, shop_items.price, shop_items.availability,\n" +
			"       items.item_id, items.title, items.description, items.insert_date, items.department_id,\n" +
			"       shops.shop_id, shops.description, shops.vat_number, shops.phone_number, shops.upgrade_date\n" +
			"FROM cart_entries INNER JOIN shop_items ON cart_entries.shop_item_id = shop_items.shop_item_id\n" +
			"				  INNER JOIN shops ON shop_items.shop_id = shops.shop_id\n" +
			"				  INNER JOIN items ON shop_items.item_id = items.item_id\n" +
			"WHERE cart_entries.user_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO cart_entries (user_id, shop_item_id, quantity)\n" +
			"VALUES (?, ?, ?)";
	
	private static final String UPDATE_PQUERY =
			"UPDATE cart_entries SET\n" +
			"user_id = ?,\n" +
			"shop_item_id = ?,\n" +
			"quantity = ?\n" +
			"WHERE cart_entry_id = ?";
	
	private static final String GET_NO_AVAILABLE_PQUERY = 
			"SELECT items.item_id, items.title, items.description, items.insert_date, items.department_id\n" +
			"FROM items INNER JOIN shop_items ON shop_items.item_id = items.item_id\n" +
			"		   INNER JOIN cart_entries ON cart_entries.shop_item_id = shop_items.shop_item_id\n" +
			"WHERE cart_entries.user_id = ? AND cart_entries.quantity > shop_items.availability";
	
	private static final String DELETE_NO_AVAILABLE_PQUERY =
			"DELETE cart1 FROM cart_entries AS cart1\n" +
			"WHERE cart1.cart_entry_id IN (\n" +
			"	SELECT cart_entry_id\n" +
			"    FROM (\n" +
			"		SELECT cart2.cart_entry_id\n" +
			"		FROM cart_entries AS cart2 INNER JOIN shop_items ON cart2.shop_item_id = shop_items.shop_item_id\n" +
			"		WHERE cart2.user_id = ? AND cart2.quantity > shop_items.availability\n" +
			"	) x\n" +
			")";
	
	private static final String DELETE_PQUERY =
			"DELETE FROM cart_entries\n" +
			"WHERE cart_entry_id = ?";

	public JdbcCartEntryDao(DataSource dataSource) {
		super(dataSource);
	}	
	
	@Override
	public CartEntryEntity getById(int cartEntryId) throws DaoException {
		try(Connection connection = getConnection()) {
			CartEntryEntity cartEntry = getById(cartEntryId, connection);
			return cartEntry;
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
	}
	
	public CartEntryEntity getById(int cartEntryId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		CartEntryEntity cartEntry = getById(cartEntryId, connection);
		return cartEntry;
	}
	
	public CartEntryEntity getById(int cartEntryId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		CartEntryEntity cartEntry = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, cartEntryId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					cartEntry = Entities.getCartEntryFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
		
		return cartEntry;
	}

	@Override
	public CartEntryEntity getByUserShopItemId(int userId, int shopItemId) throws DaoException {
		try(Connection connection = getConnection()) {
			CartEntryEntity cartEntry = getByUserShopItemId(userId, shopItemId, connection);
			return cartEntry;
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
	}
	
	public CartEntryEntity getByUserShopItemId(int userId, int shopItemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		CartEntryEntity cartEntry = getByUserShopItemId(userId, shopItemId, connection);
		return cartEntry;
	}
		
	public CartEntryEntity getByUserShopItemId(int userId, int shopItemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		CartEntryEntity cartEntry = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USER_SHOP_ITEM_ID_PQUERY)) {
			
			query.setInt(1, userId);
			query.setInt(2, shopItemId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					cartEntry = Entities.getCartEntryFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
		
		return cartEntry;
	}
	
	@Override
	public List<CartEntryEntity> getByUserId(int userId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<CartEntryEntity> results = getByUserId(userId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get CartEntry", e);
		}
	}
	
	public List<CartEntryEntity> getByUserId(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<CartEntryEntity> results = getByUserId(userId, connection);
		return results;
	}
	
	public List<CartEntryEntity> getByUserId(int userId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<CartEntryEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USER_ID_PQUERY)) {
			
			query.setInt(1, userId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					CartEntryEntity cartEntryEntity = Entities.getCartEntryFromResultsSet(results, "");
					resources.add(cartEntryEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
		
		return resources;
	}
	
	@Override
	public List<CartEntryAggregateInfo> getAggregateByUserId(int userId) throws DaoException {
		//Query takes multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				List<CartEntryAggregateInfo> results = getAggregateByUserId(userId, connection);
				return results;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot get CartEntryAggregateInfo", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public List<CartEntryAggregateInfo> getAggregateByUserId(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<CartEntryAggregateInfo> results = getAggregateByUserId(userId, connection);
		return results;
	}
	
	public List<CartEntryAggregateInfo> getAggregateByUserId(int userId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<CartEntryAggregateInfo> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_AGGREGATE_FROM_UER_ID_PQUERY)) {
			
			query.setInt(1, userId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					
					CartEntryEntity cartEntryEntity = Entities.getCartEntryFromResultsSet(results, "cart_entries");
					ItemEntity itemEntity = Entities.getItemFromResultSet(results, "items");
					ShopItemEntity shopItemEntity = Entities.getShopItemFromResultsSet(results, "shop_items");
					ShopEntity shopEntity = Entities.getShopFromResultsSet(results, "shops");
					
					JdbcResourceDao resourceDao = JdbcDaoFactory.getResourceDao();
					
					//Gets the first image if any is available
					ResourceEntity imageEntity = null;
					List<ResourceEntity> associatedResources = resourceDao.getByItemId(itemEntity.getItemId());
					if(associatedResources.isEmpty() == false) {
						imageEntity = associatedResources.get(0);
					}
					
					CartEntryAggregateInfo aggregateEntity = new CartEntryAggregateInfo(itemEntity, imageEntity, shopEntity, shopItemEntity, cartEntryEntity);
					
					resources.add(aggregateEntity);
				}
			}
		} catch(SQLException | DaoFactoryException e) {
			throw new DaoException("Cannot retrieve CartEntry", e);
		}
		
		return resources;
	}

	@Override
	public void insert(CartEntryEntity cartEntry) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(cartEntry, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert CartEntry", e);
		}
	}
	
	public void insert(CartEntryEntity cartEntry, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(cartEntry, connection);
	}
	
	public void insert(CartEntryEntity cartEntry, Connection connection) throws DaoException {
		if(cartEntry == null) {
			throw new IllegalArgumentException("CartEntry cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Inserts the record
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setInt(1, cartEntry.getUserId());
			query.setInt(2, cartEntry.getShopItemId());
			query.setInt(3, cartEntry.getQuantity());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert CartEntry", e);
		}
	}

	@Override
	public void update(CartEntryEntity cartEntry) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(cartEntry, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update CartEntry", e);
		}
	}
	
	public void update(CartEntryEntity cartEntry, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(cartEntry, connection);
	}
	
	public void update(CartEntryEntity cartEntry, Connection connection) throws DaoException {
		if(cartEntry == null) {
			throw new IllegalArgumentException("CartEntry cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			
			query.setInt(1, cartEntry.getUserId());
			query.setInt(2, cartEntry.getShopItemId());
			query.setInt(3, cartEntry.getQuantity());
			query.setInt(4, cartEntry.getCartEntryId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update CartEntry", e);
		}
	}
	
	@Override
	public List<ItemEntity> getNoLongerAvailable(int userId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ItemEntity> results = getNoLongerAvailable(userId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ItemEntity", e);
		}
	}
	
	public List<ItemEntity> getNoLongerAvailable(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ItemEntity> results = getNoLongerAvailable(userId, connection);
		return results;
	}
	
	public List<ItemEntity> getNoLongerAvailable(int userId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ItemEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_NO_AVAILABLE_PQUERY)) {
			
			query.setInt(1, userId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ItemEntity itemEntity = Entities.getItemFromResultSet(results, "items");
					resources.add(itemEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemEntity", e);
		}
		
		return resources;
	}

	@Override
	public int deleteNoLongerAvailable(int userId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			int deletedRecords = deleteNoLongerAvailable(userId, connection);
			return deletedRecords;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot delete CartEntry", e);
		}
	}
	
	public int deleteNoLongerAvailable(int userId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int deletedRecords = deleteNoLongerAvailable(userId, connection);
		return deletedRecords;
	}
	
	public int deleteNoLongerAvailable(int userId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Deletes the record
		try(PreparedStatement query = connection.prepareStatement(DELETE_NO_AVAILABLE_PQUERY)) {
			
			query.setInt(1, userId);
			
			int deletedRecords = query.executeUpdate();
			return deletedRecords;
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot delete CartEntry", e);
		}
	}
	
	@Override
	public void delete(int cartEntryId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			delete(cartEntryId, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot delete CartEntry", e);
		}
	}
	
	public void delete(int cartEntryId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		delete(cartEntryId, connection);
	}
	
	public void delete(int cartEntryId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Deletes the record
		try(PreparedStatement query = connection.prepareStatement(DELETE_PQUERY)) {
			
			query.setInt(1, cartEntryId);
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot delete CartEntry", e);
		}
	}
	
}
