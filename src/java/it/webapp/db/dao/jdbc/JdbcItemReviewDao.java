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

import it.webapp.db.dao.ItemReviewDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ItemReviewEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcItemReviewDao extends JdbcDao<ItemReviewEntity> implements ItemReviewDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT item_review_id, title, description, rating, date, shop_order_item_id\n" +
			"FROM item_reviews\n" +
			"WHERE item_review_id = ?";
	
	private static final String GET_BY_SHOP_ORDER_ITEM_ID_PQUERY =
			"SELECT item_review_id, title, description, rating, date, shop_order_item_id\n" +
			"FROM item_reviews\n" +
			"WHERE shop_order_item_id = ?";
	
	private static final String GET_BY_ITEM_ID_PQUERY =
			"SELECT item_review_id, title, description, rating, date, shop_order_item_id\n" +
			"FROM item_reviews INNER JOIN shop_order_items USING (shop_order_item_id)\n" +
			"WHERE shop_order_items.item_id = ?\n" +
			"ORDER BY date DESC\n" +
			"LIMIT ?, ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO item_reviews (title, description, rating, date, shop_order_item_id)\n" +
			"VALUES (?, ?, ?, ?, ?)";
	
	public JdbcItemReviewDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ItemReviewEntity getById(int itemReviewId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ItemReviewEntity itemReview = getById(itemReviewId, connection);
			return itemReview;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemReview", e);
		}
	}
	
	public ItemReviewEntity getById(int itemReviewId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ItemReviewEntity itemReview = getById(itemReviewId, connection);
		return itemReview;
	}
	
	public ItemReviewEntity getById(int itemReviewId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ItemReviewEntity itemReview = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, itemReviewId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					itemReview = Entities.getItemReviewFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemReview", e);
		}
		
		return itemReview;
	}
	
	@Override
	public ItemReviewEntity getByShopOrderItemId(int shopOrderItemId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ItemReviewEntity itemReview = getByShopOrderItemId(shopOrderItemId, connection);
			return itemReview;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemReview", e);
		}
	}
	
	public ItemReviewEntity getByShopOrderItemId(int shopOrderItemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ItemReviewEntity itemReview = getByShopOrderItemId(shopOrderItemId, connection);
		return itemReview;
	}
	
	public ItemReviewEntity getByShopOrderItemId(int shopOrderItemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ItemReviewEntity itemReview = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ORDER_ITEM_ID_PQUERY)) {
			
			query.setInt(1, shopOrderItemId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					itemReview = Entities.getItemReviewFromResultsSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemReview", e);
		}
		
		return itemReview;
	}

	@Override
	public List<ItemReviewEntity> getByItemId(int itemId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ItemReviewEntity> results = getByItemId(itemId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ItemReviews", e);
		}
	}
	
	public List<ItemReviewEntity> getByItemId(int itemId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ItemReviewEntity> results = getByItemId(itemId, offset, count, connection);
		return results;
	}
	
	public List<ItemReviewEntity> getByItemId(int itemId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ItemReviewEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ITEM_ID_PQUERY)) {
			
			query.setInt(1, itemId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ItemReviewEntity itemReview = Entities.getItemReviewFromResultsSet(results, "");
					resources.add(itemReview);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ItemReviews", e);
		}
		
		return resources;
	}

	@Override
	public void insert(ItemReviewEntity itemReview) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(itemReview, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ItemReview", e);
		}
	}
	
	public void insert(ItemReviewEntity itemReview, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(itemReview, connection);
	}
	
	public void insert(ItemReviewEntity itemReview, Connection connection) throws DaoException {
		if(itemReview == null) {
			throw new IllegalArgumentException("ItemReview cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setString(1, itemReview.getTitle());
			query.setString(2, itemReview.getDescription());
			query.setInt(3, itemReview.getRating().getRating());
			query.setDate(4, new java.sql.Date(itemReview.getDate().getTime()));
			query.setInt(5, itemReview.getShopOrderItemId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
                                System.out.println(updatedRecords);
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert ItemReview", e);
		}
	}

}
