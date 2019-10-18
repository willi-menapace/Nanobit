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
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.ShopEntity;

public interface ShopDao extends Dao<ShopEntity> {
	
	/**
	 * Gets the shop with the specified id
	 * 
	 * @param shopId The id of the shop to retrieve
	 * @return The shop with the specified id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the shop
	 */
	public ShopEntity getById(int shopId) throws DaoException;
	
	/**
	 * Inserts an ShopEntity
	 * 
	 * @param shop The record to insert
	 * @throws DaoException In case it is not possible to insert the record
	 */
	public void insert(ShopEntity shop) throws DaoException;
	
	/**
	 * Updates the shop with the specified id
	 * 
	 * @param shop The new shop entry, the id refers to the shop to update
	 * @throws DaoException In case it is not possible to execute the update
	 */
	public void update(ShopEntity shop) throws DaoException;
	
}
