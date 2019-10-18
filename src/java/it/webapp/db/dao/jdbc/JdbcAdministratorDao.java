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

import it.webapp.db.dao.AdministratorDao;
import it.webapp.db.entities.AdministratorEntity;
import it.webapp.db.entities.Entities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcAdministratorDao extends JdbcDao<AdministratorEntity> implements AdministratorDao {

	private static final String GET_BY_ID_PQUERY =
			"SELECT administrator_id, upgrade_date\n" +
			"FROM administrators\n" +
			"WHERE administrator_id = ?";
	
	private static final String INSERT_PQUERY =
			"INSERT INTO administrators (administrator_id, upgrade_date)\n" +
			"VALUES (?, ?)";

	public JdbcAdministratorDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public AdministratorEntity getById(int administratorId) throws DaoException {
		try (Connection connection = getConnection()) {

			AdministratorEntity administrator = getById(administratorId, connection);
			return administrator;

		} catch (SQLException e) {
			throw new DaoException("Cannot get Administrator", e);
		}
	}
	
	public AdministratorEntity getById(int administratorId, Transaction transaction) throws DaoException {
		if (transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();

		AdministratorEntity administrator = getById(administratorId, connection);
		return administrator;
	}
	
	public AdministratorEntity getById(int administratorId, Connection connection) throws DaoException {
		if (connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}

		AdministratorEntity administrator = null;

		try (PreparedStatement query = connection.prepareStatement(GET_BY_ID_PQUERY)) {

			query.setInt(1, administratorId);
			try (ResultSet results = query.executeQuery()) {

				if (results.next()) {
					administrator = Entities.getAdministratorFromResultsSet(results, "");

				}
			}
		} catch (SQLException e) {
			throw new DaoException("Cannot retrieve Administrator", e);
		}

		return administrator;
	}

	@Override
	public void insert(AdministratorEntity administrator) throws DaoException {
		try(Connection connection = getConnection()) {
			
			insert(administrator, connection);
			
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Administrator", e);
		}
	}
	
	public void insert(AdministratorEntity administrator, Transaction transaction) throws DaoException {
		if(transaction == null) {
			throw new IllegalArgumentException("Transaction cannot be null");
		}
		Connection connection = transaction.getConnection();
		
		insert(administrator, connection);
	}
	
	public void insert(AdministratorEntity administrator, Connection connection) throws DaoException {
		if(administrator == null) {
			throw new IllegalArgumentException("Administrator cannot be null");
		}
		if(connection == null) {
			throw new IllegalArgumentException("Connection cannot be null");
		}
		
		try(PreparedStatement query = connection.prepareStatement(INSERT_PQUERY)) {
			query.setInt(1, administrator.getAdministratorId());  //Must be set manually, it is a subclass of User
			query.setDate(2, new java.sql.Date(administrator.getUpgradeDate().getTime()));
			
			int rowsChanged = query.executeUpdate();
			if(rowsChanged != 1) {
				throw new SQLException("More or less than 1 record have been updated: " + rowsChanged);
			}
		} catch(SQLException e) {
			throw new DaoException("Cannot insert Administrator", e);
		}
	}

}
