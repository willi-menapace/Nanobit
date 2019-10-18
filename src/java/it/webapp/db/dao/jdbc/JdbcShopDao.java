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

import it.webapp.db.dao.ShopDao;
import it.webapp.db.entities.Entities;
import it.webapp.db.entities.ShopEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcShopDao extends JdbcDao<ShopEntity> implements ShopDao {

	private static final String GET_BY_ID_PQUERY = 
			"SELECT shop_id, description, vat_number, phone_number, upgrade_date\n" +
			"FROM shops\n" +
			"WHERE shop_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO shops (shop_id, description, vat_number, phone_number, upgrade_date)\n" +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PQUERY =
			"UPDATE shops SET\n" +
			"description = ?,\n" +
			"vat_number = ?,\n" +
			"phone_number = ?,\n" +
			"upgrade_date = ?\n" +
			"WHERE shop_id = ?";

	public JdbcShopDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public ShopEntity getById(int shopId) throws DaoException {
		ShopEntity shop = null;
		
		try(Connection connection = getConnection()) {
			shop = getById(shopId, connection);
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve shop", e);
		}
		
		return shop;
	}
	
	public ShopEntity getById(int shopId, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		ShopEntity shop = getById(shopId, connection);
		return shop;
	}
	
	public ShopEntity getById(int shopId, Connection connection) throws DaoException {
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		ShopEntity shop = null;
		
		try(PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {
			
			query.setInt(1, shopId);
			try(ResultSet results = query.executeQuery()) {
				
				if(results.next()) {
					shop = Entities.getShopFromResultsSet(results, "");
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot retrieve Shop", e);
		}
		
		return shop;
	}

	@Override
	public void insert(ShopEntity shop) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(shop, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Shop", e);
		}
	}
	
	public void insert(ShopEntity shop, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(shop, connection);
	}
	
	public void insert(ShopEntity shop, Connection connection) throws DaoException {
		if(shop == null) {
			throw new IllegalArgumentException("Shop cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			query.setInt(1, shop.getShopId());  //Must be set manually, it is a subclass of User
			query.setString(2, shop.getDescription());
			query.setString(3, shop.getVatNumber());
			query.setString(4, shop.getPhoneNumber());
			query.setDate(5, new java.sql.Date(shop.getUpgradeDate().getTime()));
			
			int rowsChanged = query.executeUpdate();
			if(rowsChanged != 1) {
				throw new SQLException("More or less than 1 record have been updated: " + rowsChanged);
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Shop", e);
		}
	}
	
	@Override
	public void update(ShopEntity shop) throws DaoException {
		try(Connection connection = getConnection()) {
			update(shop, connection);
		} catch(SQLException e) {
			throw new DaoException("Cannot update Shop", e);
		}
	}
	
	public void update(ShopEntity shop, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		update(shop, connection);
	}

	private void update(ShopEntity shop, Connection connection) throws DaoException {
		if(shop == null) {
			throw new IllegalArgumentException("Address cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try(PreparedStatement query = connection.prepareStatement(UPDATE_PQUERY)) {
			query.setString(1, shop.getDescription());
			query.setString(2, shop.getVatNumber());
			query.setString(3, shop.getPhoneNumber());
			query.setDate(4, new java.sql.Date(shop.getUpgradeDate().getTime()));
			query.setInt(5, shop.getShopId());
			
			int rowsChanged = query.executeUpdate();
			if(rowsChanged != 1) {
				throw new DaoException("More or less than 1 record have been updated: " + rowsChanged);
			}
			
		} catch(SQLException e) {
			throw new DaoException("Cannot update Shop", e);
		}
	}

}
