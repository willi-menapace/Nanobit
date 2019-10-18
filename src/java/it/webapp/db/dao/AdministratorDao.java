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

package it.webapp.db.dao;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.entities.AdministratorEntity;

public interface AdministratorDao extends Dao<AdministratorEntity> {
	
	/**
	 * Retrieves the AdministratorEntity with the given id
	 * @param administratorId The id of the record to retrieve
	 * @return The specified AministratorEntity, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the record
	 */
	public AdministratorEntity getById(int administratorId) throws DaoException;

	/**
	 * Inserts a new AdministratorEntity
	 * 
	 * @param administrator The record to insert
	 * @throws DaoException In case it is not possible to insert the record
	 */
	public void insert(AdministratorEntity administrator) throws DaoException;

}
