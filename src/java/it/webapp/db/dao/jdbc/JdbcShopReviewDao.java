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

import it.webapp.db.dao.ShopReviewDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.Rating;
import it.webapp.db.entities.ShopReviewAggregateInfo;
import it.webapp.db.entities.ShopReviewEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcShopReviewDao extends JdbcDao<ShopReviewEntity> implements ShopReviewDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT shop_review_id, title, description, rating, date, shop_order_id\n" +
			"FROM shop_reviews\n" +
			"WHERE shop_review_id = ?";
	
	private static final String GET_BY_SHOP_ORDER_ID_PQUERY =
			"SELECT shop_review_id, title, description, rating, date, shop_order_id\n" +
			"FROM shop_reviews\n" +
			"WHERE shop_order_id = ?";
	
	private static final String GET_AVERAGE_RATING_PQUERY =
			"SELECT AVG(rating) AS average_rating, COUNT(*) AS reviews_count\n" +
			"FROM shop_reviews INNER JOIN shop_orders USING (shop_order_id)\n" +
			"WHERE shop_id = ?";
	
	private static final String GET_BY_SHOP_ID_PQUERY =
			"SELECT shop_reviews.shop_review_id, shop_reviews.title, shop_reviews.description, shop_reviews.rating, shop_reviews.date, shop_reviews.shop_order_id\n" +
			"FROM shop_reviews INNER JOIN shop_orders USING (shop_order_id)\n" +
			"WHERE shop_orders.shop_id = ?\n" +
			"ORDER BY date DESC\n" +
			"LIMIT ?, ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shop_reviews (shop_review_id, title, description, rating, date, shop_order_id)\n" +
			"VAlUES (?, ?, ?, ?, ?, ?)";
        
        private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(shop_review_id) + 1) AS next_id\n" +
			"FROM shop_reviews";
	
	public JdbcShopReviewDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopReviewEntity getById(int shopReviewId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopReviewEntity shopReview = getById(shopReviewId, connection);
			return shopReview;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReview", e);
		}
	}
	
	public ShopReviewEntity getById(int shopReviewId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopReviewEntity shopReview = getById(shopReviewId, connection);
		return shopReview;
	}
	
	public ShopReviewEntity getById(int shopReviewId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopReviewEntity shopReview = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, shopReviewId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopReview = Entities.getShopReviewFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReview", e);
		}
		
		return shopReview;
	}
	
	@Override
	public ShopReviewEntity getByShopOrderId(int shopOrderId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopReviewEntity shopReview = getByShopOrderId(shopOrderId, connection);
			return shopReview;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReview", e);
		}
	}
	
	public ShopReviewEntity getByShopOrderId(int shopOrderId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopReviewEntity shopReview = getByShopOrderId(shopOrderId, connection);
		return shopReview;
	}
		
	public ShopReviewEntity getByShopOrderId(int shopOrderId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopReviewEntity shopReview = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ORDER_ID_PQUERY)) {
			
			query.setInt(1, shopOrderId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopReview = Entities.getShopReviewFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReview", e);
		}
		
		return shopReview;
	}
	
	@Override
	public ShopReviewAggregateInfo getAverageRating(int shopId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ShopReviewAggregateInfo shopReviewsInfo = getAverageRating(shopId, connection);
			return shopReviewsInfo;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReviewAggregateInfo", e);
		}
	}
	
	public ShopReviewAggregateInfo getAverageRating(int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopReviewAggregateInfo shopReviewsInfo = getAverageRating(shopId, connection);
		return shopReviewsInfo;
	}
	
	public ShopReviewAggregateInfo getAverageRating(int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopReviewAggregateInfo shopReviewsInfo = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_AVERAGE_RATING_PQUERY)) {
			
			query.setInt(1, shopId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shopReviewsInfo = Entities.getShopReviewAggregateFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReviewAggregateInfo", e);
		}
		
		return shopReviewsInfo;
	}

	@Override
	public List<ShopReviewEntity> getByShopId(int shopId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ShopReviewEntity> results = getByShopId(shopId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get ShopReviews", e);
		}
	}
	
	public List<ShopReviewEntity> getByShopId(int shopId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ShopReviewEntity> results = getByShopId(shopId, offset, count, connection);
		return results;
	}
	
	public List<ShopReviewEntity> getByShopId(int shopId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ShopReviewEntity> resources = new ArrayList();	
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopReviewEntity shopReview = Entities.getShopReviewFromResultsSet(results, "shop_reviews");
					resources.add(shopReview);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve ShopReviews", e);
		}
		
		return resources;
	}

	@Override
	public int insert(ShopReviewEntity shopReview) throws DaoException {
		try(Connection connection = getConnection()) {
			
			int insertedId = insert(shopReview, connection);
			return insertedId;
		} catch(SQLException e) {
			throw new DaoException("Cannot insert ShopReview", e);
		}
	}
	
	public int insert(ShopReviewEntity shopReview, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(shopReview, connection);
                return insertedId;
	}
	
	public int insert(ShopReviewEntity shopReview, Connection connection) throws DaoException {
		if(shopReview == null) {
			throw new IllegalArgumentException("ShopReview cannot be null");
		}
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
                    
                    //Updates the record
                    try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setInt(1, nextId);
			query.setString(2, shopReview.getTitle());
			query.setString(3, shopReview.getDescription());
			query.setInt(4, shopReview.getRating().getRating());
			query.setDate(5, new java.sql.Date(shopReview.getDate().getTime()));
			query.setInt(6, shopReview.getShopOrderId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
                    }
                    
                    return nextId;
                	
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert ShopReview", e);
		}
	}
	
}
