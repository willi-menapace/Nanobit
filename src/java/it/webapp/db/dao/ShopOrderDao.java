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
import it.webapp.db.entities.ShopOrderAggregateInfo;
import it.webapp.db.entities.ShopOrderEntity;
import java.util.List;

public interface ShopOrderDao extends Dao<ShopOrderEntity> {
	
	/**
	 * Gets the ShopOrder with the specified id
	 * 
	 * @param shopOrderId The id of the ShopOrder
	 * @return The ShopOrder with the given id
	 * @throws DaoException In case it is not possible to retrieve the ShopOrder
	 */
	public ShopOrderEntity getById(int shopOrderId) throws DaoException;
	
	/**
	 * Gets the ShopOrders associated with the given shop in chronological order
	 * 
	 * @param shopId The id of the associated shop
	 * @param offset The offset of the first row to retrieve starting from 1
	 * @param count The maximum number of rows to retrieve
	 * @return List of ShopOrders
	 * @throws DaoException In case it is not possible to retrieve the orders
	 */
	public List<ShopOrderEntity> getByShopId(int shopId, int offset, int count) throws DaoException;
	
	/**
	 * Gets the ShopOrders associated with the given user in chronological order
	 * 
	 * @param userId The id of the associated user
	 * @param offset The offset of the first row to retrieve starting from 1
	 * @param count The maximum number of rows to retrieve
	 * @return List of ShopOrders
	 * @throws DaoException In case it is not possible to retrieve the orders
	 */
	public List<ShopOrderEntity> getByUserId(int userId, int offset, int count) throws DaoException;
	
	/**
	 * Gets the ShopOrderAggregateInfo associated with a shop order
	 * 
	 * @param shopOrderId The id of the ShopOrder
	 * @return The ShopOrderAggregateInfo associated with the given id
	 * @throws DaoException In case it is not possible to retrieve the ShopOrderAggregateInfo
	 */
	public ShopOrderAggregateInfo getAggregateInfoById(int shopOrderId) throws DaoException;
	
	/**
	 * Inserts a new ShopOrder
	 * 
	 * @param shopOrder The ShopOrder to insert
	 * @return The id of the newly inserted ShopOrder
	 * @throws DaoException In case it is not possible to insert the ShopOrder
	 */
	public int insert(ShopOrderEntity shopOrder) throws DaoException;
	
}
