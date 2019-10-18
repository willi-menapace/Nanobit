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
import it.webapp.db.entities.ShopOrderItemAggregateInfo;
import it.webapp.db.entities.ShopOrderItemEntity;
import java.util.List;

public interface ShopOrderItemDao extends Dao<ShopOrderItemEntity> {

	/**
	 * Gets the ShopOrderItem with the given id
	 *
	 * @param shopOrderItemId The id
	 * @return The ShopOrderItem with the given id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the
	 * ShopOrderItem
	 */
	public ShopOrderItemEntity getById(int shopOrderItemId) throws DaoException;

	/**
	 * Gets the ShopOrderItems associated with a certain ShopOrder
	 *
	 * @param shopOrderId The associated ShopOrder
	 * @return The items associated with a given ShopOrder
	 * @throws DaoException In case it is not possible to retrieve the
	 * ShopOrderItems
	 */
	public List<ShopOrderItemEntity> getByShopOrderId(int shopOrderId) throws DaoException;

	/**
	 * Gets the ShopOrderItemAggregateInfo associated with a certain ShopOrder
	 *
	 * @param shopOrderId The associated ShopOrder
	 * @return The items aggregate info associated with a given ShopOrder
	 * @throws DaoException In case it is not possible to retrieve the
	 * ShopOrderItems
	 */
	public List<ShopOrderItemAggregateInfo> getAggregateInfoByShopOrderId(int shopOrderId) throws DaoException;

	/**
	 * Inserts a new ShopOrderItem
	 *
	 * @param shopOrderItem The ShopOrderItem to insert
	 * @throws DaoException In case it is not possible to insert the
	 * ShopOrderItem
	 */
	public int insert(ShopOrderItemEntity shopOrderItem) throws DaoException;

}
