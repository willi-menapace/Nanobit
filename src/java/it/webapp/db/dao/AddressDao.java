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

public interface AddressDao extends Dao<AddressEntity> {
	
	/**
	 * Retrieves the address with the given id
	 * 
	 * @param addressId The user from which retrieve the address
	 * @return The address with the given id, null if it does not exist
	 * @throws DaoException In case it is impossible to retrieve the address
	 */
	public AddressEntity getById(int addressId) throws DaoException;
	
	/**
	 * Retrieves an address associated with a given user
	 * 
	 * @param userId The user from which retrieve the address
	 * @return The address associated with the user, null if it does not exist
	 * @throws DaoException In case it is impossible to retrieve the associated address
	 */
	public AddressEntity getByUserId(int userId) throws DaoException;
	
	/**
	 * Inserts an AddressEntity
	 * 
	 * @param address The record to insert
	 * @returns Id of the inserted address
	 * @returns Id of the inserted address
	 * @throws DaoException In case it is not possible to insert the record
	 */
	public int insert(AddressEntity address) throws DaoException;
	
	/**
	 * Updates an address entry
	 * 
	 * @param address The new address entry. The id refers to the record to update
	 * @throws DaoException In case it is impossible to perform the update
	 */
	public void update(AddressEntity address) throws DaoException;
	
}
