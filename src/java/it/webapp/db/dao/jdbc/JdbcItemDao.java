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

import it.webapp.db.dao.ItemDao;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.Department;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ItemAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ItemShopAvailabilityInfo;
import it.webapp.db.entities.Rating;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopItemEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.db.indexing.LuceneIndexHelper;
import it.webapp.spatial.GeoFence;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;
import javax.sql.DataSource;

public class JdbcItemDao extends JdbcDao<ItemEntity> implements ItemDao {

	//The number of results to ask lucene indexes on lookup
	private static final int LUCENE_QUERY_RESULTS = 300;
	
	private static final String GET_ALL =
			"SELECT item_id, title, description, insert_date, department_id\n" +
			"FROM items";
	
	private static final String GET_BY_ID_PQUERY =
			"SELECT item_id, title, description, insert_date, department_id\n" +
			"FROM items\n" +
			"WHERE item_id = ?";
	
	private static final String GET_AGGREGATE_INFO_BY_ID_PQUERY =
			"SELECT items.item_id, items.title, items.description, items.insert_date, items.department_id,\n" +
			"	   AVG(item_reviews.rating) as item_rating,\n" +
			"       COUNT(item_reviews.item_review_id) as reviews_count,\n" +
			"       (\n" +
			"           SELECT MIN(price)\n" +
			"           FROM shop_items\n" +
			"           WHERE shop_items.item_id = items.item_id\n" +
			"       ) AS minimum_price\n" +
			"FROM items LEFT JOIN (\n" +
			"		 shop_order_items INNER JOIN item_reviews ON shop_order_items.shop_order_item_id = item_reviews.shop_order_item_id\n" +
			"	 ) ON (items.item_id = shop_order_items.item_id)\n" +
			"WHERE items.item_id = ?";
	
	private static final String GET_AVAILABILITY_INFO_BY_ID_PQUERY =
			"SELECT users.user_id, users.username, users.password_hash, users.first_name, users.last_name, users.address_id, users.email, users.registration_date, users.registration_code, users.activated, users.password_reset_code,\n" +
			"	   shops.shop_id, shops.description, shops.vat_number, shops.phone_number, shops.upgrade_date,\n" +
			"       addresses.address_id, addresses.street, addresses.street_number, addresses.city, addresses.district, addresses.zip, addresses.country, addresses.latitude, addresses.longitude,\n" +
			"	   shop_items.shop_item_id, shop_items.shop_id, shop_items.item_id, shop_items.price, shop_items.availability,\n" +
			"       AVG(shop_reviews.rating) AS shop_rating,\n" +
			"       COUNT(shop_reviews.rating) AS shop_reviews_count,\n" +
			"       (shop_shipment_types.shop_id IS NOT NULL) AS has_given_shipment_method\n" +
			"FROM users INNER JOIN shops ON users.user_id = shops.shop_id\n" +
			"		   INNER JOIN addresses ON users.address_id = addresses.address_id\n" +
			"		   INNER JOIN shop_items ON (shop_items.shop_id = shops.shop_id AND shop_items.item_id = ?)\n" +
			"           LEFT JOIN (\n" +
			"			   shop_orders INNER JOIN shop_reviews ON shop_orders.shop_order_id = shop_reviews.shop_order_id\n" +
			"           ) ON shop_orders.shop_id = shops.shop_id\n" +
			"		   LEFT JOIN shop_shipment_types ON (shops.shop_id = shop_shipment_types.shop_id AND shop_shipment_types.shipment_type_id = ?)\n" +
			"GROUP BY shops.shop_id, shop_items.shop_item_id";
	
	private static final String GET_NEXT_ID_PQUERY =
			"SELECT (MAX(item_id) + 1) AS next_id\n" +
			"FROM items";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO items (item_id, title, description, insert_date, department_id)\n" +
			"VALUES (?, ?, ?, ?, ?)";

	public JdbcItemDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public List<ItemEntity> getAll() throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ItemEntity> itemEntities = getAll(connection);
			return itemEntities;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Items", e);
		}
	}
	
	public List<ItemEntity> getAll(Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ItemEntity> itemEntities = getAll(connection);
		return itemEntities;
	}
		
	public List<ItemEntity> getAll(Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ItemEntity> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_ALL)) {
			
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ItemEntity item = Entities.getItemFromResultSet(results, "");
					
					resources.add(item);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Items", e);
		}
		
		return resources;
	}
	
	@Override
	public ItemEntity getById(int itemId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			ItemEntity item = getById(itemId, connection);
			return item;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get Item", e);
		}
	}
	
	public ItemEntity getById(int itemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ItemEntity item = getById(itemId, connection);
		return item;
	}
	
	public ItemEntity getById(int itemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ItemEntity item = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, itemId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					item = Entities.getItemFromResultSet(results, "");

				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Item", e);
		}
		
		return item;
	}
	
	@Override
	public List<ItemAggregateInfo> getFromAdvancedSearch(String term, Department department,
														 BigDecimal priceLow, BigDecimal priceHigh,
														 GeoFence geoFence,
														 SortOrder sortOrder,
														 int offset, int count) throws DaoException {
		
		//Query takes multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				List<ItemAggregateInfo> results = getFromAdvancedSearch(term, department,
																		priceLow, priceHigh,
																		geoFence,
																		sortOrder,
																		offset, count, connection);
				return results;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot get ItemAggregateInfo", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
		
	}
	
	public List<ItemAggregateInfo> getFromAdvancedSearch(String term, Department department,
														 BigDecimal priceLow, BigDecimal priceHigh,
														 GeoFence geoFence,
														 SortOrder sortOrder,
														 int offset, int count,
														 Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ItemAggregateInfo> results = getFromAdvancedSearch(term, department,
																priceLow, priceHigh,
																geoFence,
																sortOrder,
																offset, count, connection);
		return results;
	}
	
	public List<ItemAggregateInfo> getFromAdvancedSearch(String term, Department department,
														 BigDecimal priceLow, BigDecimal priceHigh,
														 GeoFence geoFence,
														 SortOrder sortOrder,
														 int offset, int count,
														 Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
				
		if(sortOrder == null) {
			throw new IllegalArgumentException("SortOrder must not be null");
		}
		
		try {
			StringBuilder queryBuilder = new StringBuilder();
			//Creates the base query
			queryBuilder.append(
					"SELECT items.item_id, MIN(shop_items.price) as min_price " +
					"FROM items LEFT JOIN ( " +
					"        shop_items INNER JOIN shops ON (shop_items.shop_id = shops.shop_id) " + 
					"                   INNER JOIN users ON (shops.shop_id = users.user_id) " +
					"                   INNER JOIN addresses ON (users.address_id = addresses.address_id) " +
					"                   INNER JOIN shop_shipment_types ON (shops.shop_id = shop_shipment_types.shop_id) " +
					"    ) ON (items.item_id = shop_items.item_id) " +
					"WHERE TRUE " //Allows to start every additional where segment with an AND
			);

			List<Pair<Integer, String>> itemDescriptors = null;
			//Processes optional search term
			if(term != null) {
				itemDescriptors = LuceneIndexHelper.getFromQuery(term, LUCENE_QUERY_RESULTS);
				queryBuilder.append(
					"AND items.item_id IN ("
				);

				boolean first = true;
				for(Pair<Integer, String> currentDescriptor : itemDescriptors) {
					//Performs comma concatenation
					if(!first) {
						queryBuilder.append(", ");
					}
					first = false;
					queryBuilder.append(currentDescriptor.getKey());
				}
				queryBuilder.append(") ");
			}

			if(department != null) {
				queryBuilder.append("AND items.department_id = ").append(department.getID()).append(" ");
			}

			if(priceLow != null) {
				queryBuilder.append("AND shop_items.price >= ").append(priceLow).append(" ");
			}

			if(priceHigh != null) {
				queryBuilder.append("AND shop_items.price <= ").append(priceHigh).append(" ");
			}

			if(geoFence != null) {
				queryBuilder.append("AND (addresses.latitude BETWEEN ").append(geoFence.getBounds().getLatLow()).append(" AND ").append(geoFence.getBounds().getLatHigh()).append(") ");
				queryBuilder.append("AND (addresses.longitude BETWEEN ").append(geoFence.getBounds().getLngLow()).append(" AND ").append(geoFence.getBounds().getLngHigh()).append(") ");
			}

			//Group by is necessary to produce only one entry for every item
			queryBuilder.append("GROUP BY items.item_id ");

			switch(sortOrder) {
				case RELEVANCE:
					//Do nothing, will take Lucene ordering
					break;
				case PRICE_ASC:
					queryBuilder.append("ORDER BY min_price ASC ");
					break;
				case PRICE_DESC:
					queryBuilder.append("ORDER BY min_price DESC ");
					break;
			}

			String query = queryBuilder.toString();

			//Id data structures used to implement relevance or sql ordering
			Set<Integer> resultSet = new HashSet<>();
			List<Integer> resultList = new ArrayList<>();

			try(Statement statement = connection.createStatement()) {
				try(ResultSet results = statement.executeQuery(query)) {
					while(results.next()) {
						int currentId = results.getInt("items.item_id");
						resultSet.add(currentId);
						resultList.add(currentId);
					}
				}
			}

			List<Integer> orderedIdList = new ArrayList<>();

			//Use SQL query ordering
			if(term == null || sortOrder != SortOrder.RELEVANCE) {
				if(resultList.size() > offset + count) {
					orderedIdList = resultList.subList(offset, offset + count); //Take only the required portion of the list which is already ordered
				} else if(resultList.size() > offset) {
					orderedIdList = resultList.subList(offset, resultList.size());
				}
				
			//Use Lucene relevance ordering
			} else {
				
				//Used to keep track if we are or not in the range of items specified by count and offset
				int discardedCount = 0; //Number of ids in the result set that have been discarded
				int selectedCount = 0;  //Number of ids in the result set that have been inserted in the final result set
				//For every Lucene item if it still exist put it in the list in the original order
				for(Pair<Integer, String> currentDescriptor : itemDescriptors) {
					int luceneId = currentDescriptor.getKey();

					//We must select only the items in the specified range
					if(resultSet.contains(luceneId)) {
						if(discardedCount < offset) {
							++discardedCount;
						} else {
							orderedIdList.add(luceneId);
							++selectedCount;
						}
					}
					if(selectedCount == count) {
						break;
					}

				}
			}

			//For every item gets its info
			List<ItemAggregateInfo> aggregateInfo = new ArrayList<>();
			for(Integer currentId : orderedIdList) {
				ItemAggregateInfo currentItemAggregateInfo = getAggregateInfoById(currentId, connection);
				aggregateInfo.add(currentItemAggregateInfo);
			}

			return aggregateInfo;
			
		} catch(SQLException e) {
			throw new DaoException("Database error", e);
		} catch(IOException e) {
			throw new DaoException("Lucene error", e);
		}
	}

	@Override
	public ItemAggregateInfo getAggregateInfoById(int itemId) throws DaoException {
		//Query takes multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				ItemAggregateInfo result = getAggregateInfoById(itemId, connection);
				return result;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot get ItemAggregateInfo", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public ItemAggregateInfo getAggregateInfoById(int itemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ItemAggregateInfo result = getAggregateInfoById(itemId, connection);
		return result;
	}
		
	public ItemAggregateInfo getAggregateInfoById(int itemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
				
		try {
			ItemAggregateInfo itemInfo = null;
			JdbcResourceDao resourceDao = JdbcDaoFactory.getResourceDao();
			
			ItemEntity item = null;
			BigDecimal price = null;
			Rating averageRating = null;
			Integer reviewsCount = null;
			
			boolean itemFound = false;
			try(PreparedStatement query = connection.prepareStatement(GET_AGGREGATE_INFO_BY_ID_PQUERY)) {

				query.setInt(1, itemId);
				try(ResultSet results = query.executeQuery()) {

					if(results.next()) {
						itemFound = true;
						item = Entities.getItemFromResultSet(results, "items");
						price = results.getBigDecimal("minimum_price");
						averageRating = new Rating(results.getInt("item_rating"));
						reviewsCount = results.getInt("reviews_count");
						
					}
				}
			}

			//Handles the case where there is no image for the item
			ResourceEntity itemImage = null;
			List<ResourceEntity> images = resourceDao.getByItemId(itemId, connection);
			if(images.isEmpty() == false) {
				itemImage = images.get(0);
			}
			
			if(itemFound) {
				itemInfo = new ItemAggregateInfo(item, itemImage, price, averageRating, reviewsCount);
			}

			return itemInfo;
		} catch(SQLException | DaoException | DaoFactoryException e) {
			throw new DaoException("Could not retrieve ItemAggregateInfo", e);
		}
	}
	
	@Override
	public List<ItemShopAvailabilityInfo> getShopAvailabilityInfoByItemId(int itemId) throws DaoException {
		try(Connection connection = getConnection()) {
			
			List<ItemShopAvailabilityInfo> results = getShopAvailabilityInfoByItemId(itemId, connection);
			return results;
			
		} catch(SQLException e) {
			throw new DaoException("Cannot get availability info", e);
		}
	}
	
	public List<ItemShopAvailabilityInfo> getShopAvailabilityInfoByItemId(int itemId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		List<ItemShopAvailabilityInfo> results = getShopAvailabilityInfoByItemId(itemId, connection);
		return results;
	}
		
	public List<ItemShopAvailabilityInfo> getShopAvailabilityInfoByItemId(int itemId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		List<ItemShopAvailabilityInfo> resources = new ArrayList();
		
		try(PreparedStatement query = connection.prepareStatement(GET_AVAILABILITY_INFO_BY_ID_PQUERY)) {
			
			query.setInt(1, itemId);
			query.setInt(2, ShipmentType.SHOP_PICK_UP.getId()); //Query is parametrized to select the shipment type for which we want to know if the shop supports it
			try(ResultSet results = query.executeQuery()) {
				
				while(results.next()) {
					ShopEntity shop = Entities.getShopFromResultsSet(results, "shops");
					UserEntity user = Entities.getUserFromResultsSet(results, "users");
					AddressEntity address = Entities.getAddressFromResultSet(results, "addresses");
					ShopItemEntity shopItem = Entities.getShopItemFromResultsSet(results, "shop_items");
					Rating aggregateRating = new Rating(results.getInt("shop_rating"));
					int reviewsCount = results.getInt("shop_reviews_count");
					boolean canPickUp = results.getBoolean("has_given_shipment_method"); //Indicates whether hand pick up is allowed in the current shop
					
					ItemShopAvailabilityInfo availabilityInfo = new ItemShopAvailabilityInfo(user, shop, address, shopItem, aggregateRating, reviewsCount, canPickUp);
					
					resources.add(availabilityInfo);
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve availability info", e);
		}
		
		return resources;
	}
	
	@Override
	public int insert(ItemEntity item) throws DaoException {
		//Insert operations take multiple steps so a transaction is needed
		try(Transaction transaction = JdbcDaoFactory.beginTransaction()) {
			try {
				Connection connection = transaction.getConnection();

				int insertedId = insert(item, connection);
				return insertedId;
			} catch(DaoException e) {
				transaction.rollback();
				transaction.close();
				throw e;
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Item", e);
		} catch(DaoFactoryException e) {
			throw new DaoException("Cannot begin Transaction", e);
		}
	}
	
	public int insert(ItemEntity item, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		int insertedId = insert(item, connection);
		return insertedId;
	}
	
	public int insert(ItemEntity item, Connection connection) throws DaoException {
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
				query.setString(2, item.getTitle());
				query.setString(3, item.getDescription());
				query.setDate(4, new java.sql.Date(item.getInsertDate().getTime()));                              
				query.setInt(5, item.getDepartment().getID());                                

				int insertedRecords = query.executeUpdate();
				if(insertedRecords != 1) {
					throw new DaoException("Unexpected number of inserted records, only 1 should be inserted: " + insertedRecords);
				}
			}
			
			return nextId;
			
		}  catch(SQLException e) {
			throw new DaoException("Cannot insert Item", e);
		}
	}

}
