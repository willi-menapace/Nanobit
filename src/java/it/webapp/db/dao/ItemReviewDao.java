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
import it.webapp.db.entities.ItemReviewEntity;
import java.util.List;

public interface ItemReviewDao extends Dao<ItemReviewEntity> {
	
	/**
	 * Gets the ItemReview with the given id
	 * 
	 * @param itemReviewId The id of the item review to retrieve
	 * @return The ItemReview with the given id, null if not found
	 * @throws DaoException In case it is not possible to retrieve the review
	 */
	public ItemReviewEntity getById(int itemReviewId) throws DaoException;
	
	/**
	 * Gets the ItemReview associated with the given shop order item
	 * 
	 * @param shopOrderItemId The id of the associated shop order item
	 * @return The ItemReview with the associated id, null if not found
	 * @throws DaoException In case it is not possible to retrieve the review
	 */
	public ItemReviewEntity getByShopOrderItemId(int shopOrderItemId) throws DaoException;
	
	/**
	 * Gets the ItemReview associated with an item in chronological order
	 * 
	 * @param itemId The id of the referenced item
	 * @param offset The row from wich to start, counting from 1
	 * @param count The number of rows to retrieve
	 * @return List of retrieved reviews in chronological order
	 * @throws DaoException In case it is not possible to retrieve the reviews
	 */
	public List<ItemReviewEntity> getByItemId(int itemId, int offset, int count) throws DaoException;
	
	/**
	 * Inserts a new ItemReview
	 * 
	 * @param itemReview The item review to insert
	 * @throws DaoException In case it is not possible to insert the review
	 */
	public void insert(ItemReviewEntity itemReview) throws DaoException;

}
