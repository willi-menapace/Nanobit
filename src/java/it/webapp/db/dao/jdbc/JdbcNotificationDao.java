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

import it.webapp.db.dao.NotificationDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.NotificationEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcNotificationDao extends JdbcDao<NotificationEntity> implements NotificationDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT notification_id, date, is_new, notification_type_id, user_id, shop_review_id, shop_order_id, issue_id\n" +
			"FROM notifications\n" +
			"WHERE notification_id = ?";
	
	private static final String GET_BY_USER_ID_PQUERY =
			"SELECT notification_id, date, is_new, notification_type_id, user_id, shop_review_id, shop_order_id, issue_id\n" +
			"FROM notifications\n" +
			"WHERE user_id = ?\n" +
			"ORDER BY date DESC\n" +
			"LIMIT ?, ?";
	
	private static final String GET_BY_USER_ID_ADMIN_PQUERY =
		"SELECT notification_id, date, is_new, notification_type_id, user_id, shop_review_id, shop_order_id, issue_id\n" +
		"FROM notifications\n" +
		"WHERE user_id = ? OR user_id IS NULL\n" +
		"ORDER BY date DESC\n" +
		"LIMIT ?, ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO notifications (date, is_new, notification_type_id, user_id, shop_review_id, shop_order_id, issue_id)\n" +
			"VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PQUERY =
			"UPDATE notifications SET\n" +
			"date = ?,\n" +
			"is_new = ?,\n" +
			"notification_type_id = ?,\n" +
			"user_id = ?,\n" +
			"shop_review_id = ?,\n" +
			"shop_order_id = ?,\n" +
			"issue_id = ?\n" +
			"WHERE notification_id = ?";

	public JdbcNotificationDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public NotificationEntity getById(int notificationId) throws DaoException {
		try(Connection connection = getConnection()) {
			NotificationEntity notification = getById(notificationId, connection);
			return notification;
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve NotificationEntity", e);
		}
	}
	
	public NotificationEntity getById(int notificationId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		NotificationEntity notification = getById(notificationId, connection);
		return notification;
	}
		
	public NotificationEntity getById(int notificationId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		NotificationEntity notification = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, notificationId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					notification = Entities.getNotificationFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve NotificationEntity", e);
		}
		
		return notification;
	}

	@Override
	public List<NotificationEntity> getByUserId(int userId, int offset, int count, boolean includeAdmin) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<NotificationEntity> results = getByUserId(userId, offset, count, includeAdmin, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Notification", e);
		}
	}
	
	public List<NotificationEntity> getByUserId(int userId, int offset, int count, boolean includeAdmin, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<NotificationEntity> results = getByUserId(userId, offset, count, includeAdmin, connection);
		return results;
	}
		
	public List<NotificationEntity> getByUserId(int userId, int offset, int count, boolean includeAdmin, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<NotificationEntity> resources = new ArrayList();
		
		//Selects wether to include or not admin notifications in the results
		String queryString = GET_BY_USER_ID_PQUERY;
		if(includeAdmin == true) {
			queryString = GET_BY_USER_ID_ADMIN_PQUERY;
		}
		try(PreparedStatement query = connection.prepareStatement(queryString)) {
			
			query.setInt(1, userId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					NotificationEntity notificationEntity = Entities.getNotificationFromResultsSet(results, "");
					resources.add(notificationEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Notification", e);
		}
		
		return resources;
	}

	@Override
	public void insert(NotificationEntity notification) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(notification, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Notification", e);
		}
	}
	
	public void insert(NotificationEntity notification, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(notification, connection);
	}
	
	public void insert(NotificationEntity notification, Connection connection) throws DaoException {
		if(notification == null) {
			throw new IllegalArgumentException("CartEntry cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Inserts the record
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			
			query.setDate(1, new java.sql.Date(notification.getDate().getTime()));
			query.setBoolean(2, notification.getIsNew());
			query.setInt(3, notification.getType().getId());
			if(notification.getUserId() == null) {
                            query.setNull(4, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(4, notification.getUserId());
                        }                               
                        if(notification.getShopReviewId() == null) {
                            query.setNull(5, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(5, notification.getShopReviewId()); 
                        }
                        if(notification.getShopOrderId() == null) {
                            query.setNull(6, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(6, notification.getShopOrderId()); 
                        }
                        if(notification.getIssueId() == null) {
                            query.setNull(7, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(7, notification.getIssueId()); 
                        }
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Notification", e);
		}
	}

	@Override
	public void update(NotificationEntity notification) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(notification, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update Notification", e);
		}
	}
	
	public void update(NotificationEntity notification, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(notification, connection);
	}
	
	public void update(NotificationEntity notification, Connection connection) throws DaoException {
		if(notification == null) {
			throw new IllegalArgumentException("CartEntry cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			
			query.setDate(1, new java.sql.Date(notification.getDate().getTime()));
			query.setBoolean(2, notification.getIsNew());
			query.setInt(3, notification.getType().getId());
			if(notification.getUserId() == null) {
                            query.setNull(4, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(4, notification.getUserId());
                        }
			if(notification.getShopReviewId() == null) {
                            query.setNull(5, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(5, notification.getShopReviewId()); 
                        }
                        if(notification.getShopOrderId() == null) {
                            query.setNull(6, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(6, notification.getShopOrderId()); 
                        }
                        if(notification.getIssueId() == null) {
                            query.setNull(7, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(7, notification.getIssueId()); 
                        }
			query.setInt(8, notification.getNotificationId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update Notification", e);
		}
	}

	
	
}
