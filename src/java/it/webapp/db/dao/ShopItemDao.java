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
import it.webapp.db.entities.ShopItemEntity;
import java.util.List;

public interface ShopItemDao extends Dao<ShopItemEntity> {
	
	/**
	 * Retrieves the ShopItem with the given id
	 * 
	 * @param shopItemId The id to retrieve
	 * @return The shop item with the given id, null if not found
	 * @throws DaoException In case it is not possible to retrieve the ShopItem
	 */
	public ShopItemEntity getById(int shopItemId) throws DaoException;
	
	/**
	 * Retrieves the ShopItems associated with a given shop
	 * 
	 * @param shopId The id of the associated shop
	 * @return List of ShopItem associated with the given shop
	 * @throws DaoException In case it is not possible to retrieve the items
	 */
	public List<ShopItemEntity> getByShopId(int shopId) throws DaoException;
	
	/**
	 * Inserts a new ShopItem
	 * 
	 * @param shopItem The shop Item to insert
	 * @throws DaoException In case it is not possible to insert the ShopItem
	 */
	public int insert(ShopItemEntity shopItem) throws DaoException;
	
	/**
	 * Updates the ShopItem identified by the given id
	 * 
	 * @param shopItem The updated ShopItem
	 * @throws DaoException In case it is not possible to update the ShopItem
	 */
	public void update(ShopItemEntity shopItem) throws DaoException;
	
	/**
	 * Deletes the ShopItem with a given id
	 * 
	 * @param shopItemId The id of the ShopItem to delete
	 * @throws DaoException In case it is not possible to delete the ShopItem
	 */
	public void delete(int shopItemId) throws DaoException;
	
}
