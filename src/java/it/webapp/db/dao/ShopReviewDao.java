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
import it.webapp.db.entities.ShopReviewAggregateInfo;
import it.webapp.db.entities.ShopReviewEntity;
import java.util.List;

public interface ShopReviewDao extends Dao<ShopReviewEntity> {
	
	/**
	 * Retrieves the ShopReview with the given id
	 * 
	 * @param shopReviewId The id of the ShopReview to retrieve
	 * @return The requested ShopReview, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the ShopReview
	 */
	public ShopReviewEntity getById(int shopReviewId) throws DaoException;
	
	/**
	 * Retrieves the ShopReview with the given shop order id
	 * 
	 * @param shopOrderId The id of the associates shop order
	 * @return The requested ShopReview, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the ShopReview
	 */
	public ShopReviewEntity getByShopOrderId(int shopOrderId) throws DaoException;
	
	/**
	 * Gets the average rating for all the reviews associated with a given shop
	 * 
	 * @param shopId Id of the associated shop
	 * @return Information about the shop ratings, null if given shop does not exist
	 * @throws DaoException In case it is not possible to retrieve the rating
	 */
	public ShopReviewAggregateInfo getAverageRating(int shopId) throws DaoException;
	
	/**
	 * Retrieves the ShopReviews associated with a given shop in chronological order
	 * 
	 * @param shopId The id of the associated shop
	 * @param offset The offset of the first row to retrieve starting from 1
	 * @param count The maximum number of rows to retrieve
	 * @return List of retrieved ShopReviews
	 * @throws DaoException In case it is not possible to retrieve the reivews
	 */
	public List<ShopReviewEntity> getByShopId(int shopId, int offset, int count) throws DaoException;
	
	/**
	 * Inserts a new ShopReview
	 * 
	 * @param shopReview The review to insert
	 * @throws DaoException In case it is not possible to insert the review
	 */
	public int insert(ShopReviewEntity shopReview) throws DaoException;

}
