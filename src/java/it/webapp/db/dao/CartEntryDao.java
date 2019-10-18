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
import it.webapp.db.entities.CartEntryAggregateInfo;
import it.webapp.db.entities.CartEntryEntity;
import it.webapp.db.entities.ItemEntity;
import java.util.List;

public interface CartEntryDao extends Dao<CartEntryEntity> {
	
	/**
	 * Gets the CartEntryEntity with the specified id
	 * 
	 * @param cartEntryId The id of the CartEntryEntity to retrieve
	 * @return The specified CartEntryEntity, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the CartEntryEntity
	 */
	public CartEntryEntity getById(int cartEntryId) throws DaoException;
	
	/**
	 * Gets the CartEntryEntity with the specified userId and shopItemId
	 * 
	 * @param userId The associated user
	 * @param shopItemId The associated shop item
	 * @return The CartEntryEntity with the specified user and shopItem, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the CartEntryEntity
	 */
	public CartEntryEntity getByUserShopItemId(int userId, int shopItemId) throws DaoException;
	
	/**
	 * Gets the CartEntryEntities associated with the given user
	 * 
	 * @param userId The id of the given user
	 * @return List of the CartEntryEntitities associated with a given user
	 * @throws DaoException In case it is not possible to retrieve the CartEntryEntities
	 */
	public List<CartEntryEntity> getByUserId(int userId) throws DaoException;
	
	/**
	 * Gets the CartEntryAggregateInfo associated with the given user
	 * 
	 * @param userId The id of the given user
	 * @return List of the CartEntryAggregateInfo associated with a given user
	 * @throws DaoException In case it is not possible to retrieve the CartEntryAggregateInfo
	 */
	public List<CartEntryAggregateInfo> getAggregateByUserId(int userId) throws DaoException;
	
	/**
	 * Inserts a new CartEntryEntity
	 * 
	 * @param cartEntry The CartEntryEntity to insert
	 * @throws DaoException In case it is not possible to insert the record
	 */
	public void insert(CartEntryEntity cartEntry) throws DaoException;
	
	/**
	 * Updates the CartEntryEntity with the specified id
	 * 
	 * @param cartEntry The CartEntryEntity to update
	 * @throws DaoException In case it is not possible to perform the update
	 */
	public void update(CartEntryEntity cartEntry) throws DaoException;
	
	/**
	 * Retrieves a list of items that are no longer available
	 * 
	 * @param userId Id of the cart owner
	 * @return List of no longer available items
	 * @throws DaoException In case it is not possible to retrieve the items
	 */
	public List<ItemEntity> getNoLongerAvailable(int userId) throws DaoException;
	
	/**
	 * Deletes the cartEntryEntities that are no longer available
	 * 
	 * @param userId The id of the cart owner
	 * @return Number of deleted records
	 * @throws DaoException In case it is not possible to delete the items
	 */
	public int deleteNoLongerAvailable(int userId) throws DaoException;
	
	/**
	 * Deletes the CartEntryEntity with the given id
	 * 
	 * @param cartEntryId The id of the CartEntryEntity to delete
	 * @throws DaoException In case it is not possible to delete the record
	 */
	public void delete(int cartEntryId) throws DaoException;
	
}
