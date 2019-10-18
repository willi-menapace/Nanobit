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

import it.webapp.db.dao.IssueDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.IssueAggregateInfo;
import it.webapp.db.entities.IssueEntity;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JdbcIssueDao extends JdbcDao<IssueEntity> implements IssueDao {

	private static final String GET_BY_ID_PQUERY
			= "SELECT issue_id, description, open_date, issue_result_id, motivation, close_date, shop_order_item_id\n"
			+ "FROM issues\n"
			+ "WHERE issue_id = ?";
	
	public static final String GET_AGGREGATE_BY_ID_PQUERY =
			"SELECT issues.issue_id, issues.description, issues.open_date, issues.issue_result_id, issues.motivation, issues.close_date, issues.shop_order_item_id,\n" +
			"       shop_order_items.shop_order_item_id, shop_order_items.shop_order_id, shop_order_items.item_id, shop_order_items.price, shop_order_items.quantity,\n" +
			"       items.item_id, items.title, items.description, items.insert_date, items.department_id\n" +
			"FROM issues INNER JOIN shop_order_items USING(shop_order_item_id)\n" +
			"            INNER JOIN items USING(item_id)\n" +
			"WHERE issue_id = ?";

	private static final String GET_BY_USER_ID_PQUERY =
			"SELECT issue_id, description, open_date, issue_result_id, motivation, close_date, shop_order_item_id\n" +
			"FROM issues INNER JOIN shop_order_items USING (shop_order_item_id)\n" +
			"			INNER JOIN shop_orders USING (shop_order_id)\n" +
			"WHERE shop_orders.user_id = ?\n" +
			"ORDER BY open_date DESC\n" +
			"LIMIT ?, ?";

	private static final String GET_BY_SHOP_ID_PQUERY =
			"SELECT issue_id, description, open_date, issue_result_id, motivation, close_date, shop_order_item_id\n" +
			"FROM issues INNER JOIN shop_order_items USING (shop_order_item_id)\n" +
			"			INNER JOIN shop_orders USING (shop_order_id)\n" +
			"WHERE shop_orders.shop_id = ?\n" +
			"ORDER BY open_date DESC\n" +
			"LIMIT ?, ?";

	private static final String GET_CLOSE_ORDERED_PQUERY
			= "SELECT issue_id, description, open_date, issue_result_id, motivation, close_date, shop_order_item_id\n"
			+ "FROM issues\n"
			+ "ORDER BY close_date IS NOT NULL, open_date DESC\n"
			+ "LIMIT ?, ?";

	private static final String GET_NEXT_ID_PQUERY
			= "SELECT (MAX(issue_id) + 1) AS next_id\n"
			+ "FROM issues";
	
	private static final String INSERT_PQUERY
			= "INSERT INTO issues (issue_id, description, shop_order_item_id, open_date)\n"
			+ "VALUES (?, ?, ?, ?)";

	private static final String UPDATE_PQUERY
			= "UPDATE issues SET\n"
			+ "description = ?,\n"
			+ "open_date = ?,\n"
			+ "issue_result_id = ?,\n"
			+ "motivation = ?,\n"
			+ "close_date = ?,\n"
			+ "shop_order_item_id = ?\n"
			+ "WHERE issue_id = ?";

	public JdbcIssueDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public IssueEntity getById(int issueId) throws DaoException {
		try (Connection connection = getConnection()) {

			IssueEntity issue = getById(issueId, connection);
			return issue;

		} catch (SQLException e) {
			throw new DaoException("Cannot get Issue", e);
		}
	}

	public IssueEntity getById(int issueId, Transaction transaction) throws DaoException {
		if (transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();

		IssueEntity issue = getById(issueId, connection);
		return issue;
	}

	public IssueEntity getById(int issueId, Connection connection) throws DaoException {
		if (connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}

		IssueEntity issue = null;

		try (PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {

			query.setInt(1, issueId);
			try (ResultSet results = query.executeQuery()) {

				if (results.next()) {
					issue = Entities.getIssueFromResultsSet(results, "");

				}
			}
		} catch (SQLException e) {
			throw new DaoException("Cannot retrieve Issue", e);
		}

		return issue;
	}
	
	@Override
	public IssueAggregateInfo getAggregateInfoById(int issueId) throws DaoException {
		try (Connection connection = getConnection()) {

			IssueAggregateInfo issue = getAggregateInfoById(issueId, connection);
			return issue;

		} catch (SQLException e) {
			throw new DaoException("Cannot get Issue", e);
		}
	}

	public IssueAggregateInfo getAggregateInfoById(int issueId, Transaction transaction) throws DaoException {
		if (transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();

		IssueAggregateInfo issue = getAggregateInfoById(issueId, connection);
		return issue;
	}

	public IssueAggregateInfo getAggregateInfoById(int issueId, Connection connection) throws DaoException {
		if (connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}

		IssueAggregateInfo aggregateIssueInfo = null;

		try (PreparedStatement query = connection.prepareStatement(GET_AGGREGATE_BY_ID_PQUERY)) {

			query.setInt(1, issueId);
			try (ResultSet results = query.executeQuery()) {

				if (results.next()) {
					IssueEntity issue = Entities.getIssueFromResultsSet(results, "issues");
					ShopOrderItemEntity shopOrderItem = Entities.getShopOrderItemFromResultsSet(results, "shop_order_items");
					ItemEntity item = Entities.getItemFromResultSet(results, "items");
					
					aggregateIssueInfo = new IssueAggregateInfo(issue, shopOrderItem, item);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Cannot retrieve Issue", e);
		}

		return aggregateIssueInfo;
	}

	@Override
	public List<IssueEntity> getByUserId(int userId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<IssueEntity> results = getByUserId(userId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Issues", e);
		}
	}
	
	public List<IssueEntity> getByUserId(int userId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<IssueEntity> results = getByUserId(userId, offset, count, connection);
		return results;
	}
	
	public List<IssueEntity> getByUserId(int userId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<IssueEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_USER_ID_PQUERY)) {
			
			query.setInt(1, userId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					IssueEntity resourceEntity = Entities.getIssueFromResultsSet(results, "");
					resources.add(resourceEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Issues", e);
		}
		
		return resources;
	}

	@Override
	public List<IssueEntity> getByShopId(int shopId, int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<IssueEntity> results = getByShopId(shopId, offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Issues", e);
		}
	}
	
	public List<IssueEntity> getByShopId(int shopId, int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<IssueEntity> results = getByShopId(shopId, offset, count, connection);
		return results;
	}
	
	public List<IssueEntity> getByShopId(int shopId, int offset, int count, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<IssueEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			query.setInt(2, offset);
			query.setInt(3, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					IssueEntity resourceEntity = Entities.getIssueFromResultsSet(results, "");
					resources.add(resourceEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Issues", e);
		}
		
		return resources;
	}

	@Override
	public List<IssueEntity> getCloseOrderedIssues(int offset, int count) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<IssueEntity> results = getCloseOrderedIssues(offset, count, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Issues", e);
		}
	}
	
	public List<IssueEntity> getCloseOrderedIssues(int offset, int count, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<IssueEntity> results = getCloseOrderedIssues(offset, count, connection);
		return results;
	}
	
	public List<IssueEntity> getCloseOrderedIssues(int offset, int count, Connection connection) throws DaoException {
		if(offset < 0 || count < 0) {
			throw new IllegalArgumentException("Invalid offset or count parameters");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<IssueEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_CLOSE_ORDERED_PQUERY)) {
			
			query.setInt(1, offset);
			query.setInt(2, count);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					IssueEntity resourceEntity = Entities.getIssueFromResultsSet(results, "");
					resources.add(resourceEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Issues", e);
		}
		
		return resources;
	}

	@Override
	public int insert(IssueEntity issue) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				int insertedId = insert(issue, connection);
				return insertedId;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Issue", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public int insert(IssueEntity issue, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		return insert(issue, connection);
	}
	
	public int insert(IssueEntity issue, Connection connection) throws DaoException {
		if(issue == null) {
			throw new IllegalArgumentException("Issue cannot be null");
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

			try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {

				query.setInt(1, nextId);
				query.setString(2, issue.getDescription());
				query.setInt(3, issue.getShopOrderItemId());
				query.setDate(4, new java.sql.Date(issue.getOpenDate().getTime()));

				int rowsChanged = query.executeUpdate();
				if(rowsChanged != 1) {
					throw new SQLException("More or less than 1 record have been updated: " + rowsChanged);
				}
			}
			
			return nextId;
			
		} catch(SQLException e) {
			throw new DaoException("Could not insert Issue");
		}
	}

	@Override
	public void update(IssueEntity issue) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(issue, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update Issue", e);
		}
	}
	
	public void update(IssueEntity issue, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(issue, connection);
	}
	
	public void update(IssueEntity issue, Connection connection) throws DaoException {
		if(issue == null) {
			throw new IllegalArgumentException("Issue cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			query.setString(1, issue.getDescription());
			query.setDate(2, new java.sql.Date(issue.getOpenDate().getTime()));
                        if(issue.getIssueResult() == null) {
                            query.setNull(3, java.sql.Types.INTEGER);
                        } else {
                            query.setInt(3, issue.getIssueResult().getId());
                        }
			if(issue.getMotivation() == null) {
                            query.setNull(4, java.sql.Types.VARCHAR);
                        } else {
                            query.setString(4, issue.getMotivation());
                        }
			if(issue.getCloseDate() == null) {
                            query.setNull(5, java.sql.Types.DATE);
                        } else {
                            query.setDate(5, new java.sql.Date(issue.getCloseDate().getTime()));
                        }		
			query.setInt(6, issue.getShopOrderItemId());
			query.setInt(7, issue.getIssueId());
			
			int rowsChanged = query.executeUpdate();
			if(rowsChanged != 1) {
				throw new SQLException("More or less than 1 record have been updated: " + rowsChanged);
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot update Issue", e);
		}
	}

}
