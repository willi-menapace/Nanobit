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

import it.webapp.db.dao.ResourceDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ResourceEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


public class JdbcResourceDao extends JdbcDao<ResourceEntity> implements ResourceDao {

	private static final String GET_BY_SHOP_ID_PQUERY = 
			"SELECT resource_id, filename, friendly_name\n" +
			"FROM resources INNER JOIN shop_images USING (resource_id)\n" +
			"WHERE shop_id = ?";
	
	private static final String GET_BY_ITEM_ID_PQUERY = 
			"SELECT resource_id, filename, friendly_name\n" +
			"FROM resources INNER JOIN item_images USING (resource_id)\n" +
			"WHERE item_id = ?";
		
	private static final String INSERT_RESOURCE_PQUERY = 
			"INSERT INTO resources (filename, friendly_name)\n" +
			"VALUES (?, ?)";
			
	private static final String GET_ID_BY_FILENAME_PQUERY = 
			"SELECT resource_id \n" +
			"FROM resources\n" +
			"WHERE filename = ?";
				
	private static final String INSERT_SHOP_IMAGE_PQUERY = 
			"INSERT INTO shop_images (resource_id, shop_id)\n" +
			"VALUES (?, ?)";
					
	private static final String INSERT_ITEM_IMAGE_PQUERY = 
			"INSERT INTO item_images (resource_id, item_id)\n" +
			"VALUES (?, ?)";
	
	private static final String DELETE_RESOURCE_PQUERY = 
			"DELETE FROM resources\n" +
			"WHERE resource_id = ?";
	
	private static final String UPDATE_RESOURCE_PQUERY = 
			"UPDATE resources SET\n" +
			"filename = ?,\n" +
			"friendly_name = ?\n" +
			"WHERE resource_id = ?";
	
	public JdbcResourceDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public List<ResourceEntity> getByShopId(int shopId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ResourceEntity> results = getByShopId(shopId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Resources", e);
		}
	}
	
	public List<ResourceEntity> getByShopId(int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ResourceEntity> results = getByShopId(shopId, connection);
		return results;
	}
	
	public List<ResourceEntity> getByShopId(int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ResourceEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_SHOP_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ResourceEntity resourceEntity = Entities.getResourceFromResultSet(results, "");
					resources.add(resourceEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Resources", e);
		}
		
		return resources;
	}

	@Override
	public List<ResourceEntity> getByItemId(int itemId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ResourceEntity> results = getByItemId(itemId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Resources", e);
		}
	}
	
	public List<ResourceEntity> getByItemId(int itemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ResourceEntity> results = getByItemId(itemId, connection);
		return results;
	}
	
	public List<ResourceEntity> getByItemId(int itemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ResourceEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ITEM_ID_PQUERY)) {
			
			query.setInt(1, itemId);
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ResourceEntity resourceEntity = Entities.getResourceFromResultSet(results, "");
					resources.add(resourceEntity);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Resources", e);
		}
		
		return resources;
	}

	/**
	 * Inserts the given resource setting its id
	 * 
	 * @param resource The resource to insert
	 * @param connection The connection to the db
	 * @return The id of the inserted resource
	 * @throws DaoException In case it is not possible to complete the operation
	 */
	private int insertResource(ResourceEntity resource, Connection connection) throws DaoException {
		if(resource == null) {
			throw new IllegalArgumentException("Resource cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try {
			//Inserts the resource in the resources table
			try(PreparedStatement query = connection.prepareStatement(INSERT_RESOURCE_PQUERY)) {
				query.setString(1, resource.getFilename());
				query.setString(2, resource.getFriendlyName());

				query.executeUpdate();

			}

			//Gets the id of the inserted row
			try(PreparedStatement query = connection.prepareStatement(GET_ID_BY_FILENAME_PQUERY)) {
				query.setString(1, resource.getFilename());

				try(ResultSet results = query.executeQuery()) {

					if(results.next()) {
						resource.setResourceId(results.getInt("resource_id"));
					} else {
						throw new DaoException("Cannot insert Resource");
					}
				}
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		}
				
		return resource.getResourceId();
	}

	@Override
	public void insertShopImage(ResourceEntity resource, int shopId) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				insertShopImage(resource, shopId, connection);
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public void insertShopImage(ResourceEntity resource, int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insertShopImage(resource, shopId, connection);
	}

	public void insertShopImage(ResourceEntity resource, int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		//Inserts the resource and sets its id
		insertResource(resource, connection);
		
		try {
			
			//Associates the resource with its shop
			try(PreparedStatement query = connection.prepareStatement(INSERT_SHOP_IMAGE_PQUERY)) {
				query.setInt(1, resource.getResourceId());
				query.setInt(2, shopId);

				query.executeUpdate();
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		}
	}
	
	@Override
	public void insertItemImage(ResourceEntity resource, int itemId) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				insertItemImage(resource, itemId, connection);
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public void insertItemImage(ResourceEntity resource, int itemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insertItemImage(resource, itemId, connection);
	}
	
	public void insertItemImage(ResourceEntity resource, int itemId, Connection connection) throws DaoException {
		if(resource == null) {
			throw new IllegalArgumentException("Resource cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		//Inserts the resource and sets its id
		insertResource(resource, connection);
		
		try {
			
			//Associates the resource with its shop
			try(PreparedStatement query = connection.prepareStatement(INSERT_ITEM_IMAGE_PQUERY)) {
				query.setInt(1, resource.getResourceId());
				query.setInt(2, itemId);

				query.executeUpdate();
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		}
	}

	@Override
	public void delete(int resourceId) throws DaoException {
		//A Transaction is not strictly needed but we use it anyway
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				delete(resourceId, connection);
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot delete Resource", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public void delete(int resourceId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		delete(resourceId, connection);
	}
	
	public void delete(int resourceId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Deletes the resource
		try(PreparedStatement query = connection.prepareStatement(DELETE_RESOURCE_PQUERY)) {
			query.setInt(1, resourceId);

			query.executeUpdate();
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Resource", e);
		}
	}

	@Override
	public void update(ResourceEntity resource) throws DaoException {
		try(Connection connection = getConnection()) {
			
			update(resource, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update Resource", e);
		}
	}
	
	public void update(ResourceEntity resource, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(resource, connection);
	}
	
	public void update(ResourceEntity resource, Connection connection) throws DaoException {
		if(resource == null) {
			throw new IllegalArgumentException("Resource cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
	
		//Updates the record
		try(PreparedStatement query = connection.prepareStatement(UPDATE_RESOURCE_PQUERY)) {
			
			query.setString(1, resource.getFilename());
			query.setString(2, resource.getFriendlyName());
			query.setInt(3, resource.getResourceId());
			
			int updatedRecords = query.executeUpdate();
			if(updatedRecords != 1) {
				throw new DaoException("An unexpected number of records have been updated: " + updatedRecords);
			}
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot update Resource", e);
		}
	}

}
