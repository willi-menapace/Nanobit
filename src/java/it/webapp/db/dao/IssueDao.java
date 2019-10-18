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
import it.webapp.db.entities.IssueAggregateInfo;
import it.webapp.db.entities.IssueEntity;
import java.util.List;

public interface IssueDao extends Dao<IssueEntity> {
	
	/**
	 * Gets the issue with the specified id
	 * 
	 * @param issueId The id of the issue to get
	 * @return The issue with the given id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the issue
	 */
	public IssueEntity getById(int issueId) throws DaoException;
	
	/**
	 * Gets aggregate issue information for the issue with the specified id
	 * 
	 * @param issueId The id of the issue to get
	 * @return The aggregate information of the issue with the given id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the issue
	 */
	public IssueAggregateInfo getAggregateInfoById(int issueId) throws DaoException;
	
	/**
	 * Gets the issues associated with the specified user in chronological order with most recently open issues first
	 * 
	 * @param userId The id of the user
	 * @param offset Offset of the first issue to retrieve starting from 1
	 * @param count Maximum number of issues to retrieve
	 * @return The issues associated with the user
	 * @throws DaoException In case it is not possible to retrieve the issues
	 */
	public List<IssueEntity> getByUserId(int userId, int offset, int count) throws DaoException;
	
	/**
	 * Returns a limited list of all issues ordered by (closed/open, date) with the open issues listed first
	 * 
	 * @param offset The offset of the first row to get starting from 1
	 * @param count The number of successive rows to retrieve
	 * @return List of retrieved issues
	 * @throws DaoException In case it is not possible to retrieve the issues
	 */
	public List<IssueEntity> getCloseOrderedIssues(int offset, int count) throws DaoException;
	
	/**
	 * Gets the issues associated with the specified shop in chronological order with most recently open issues first
	 * 
	 * @param shopId The id of the shop
	 * @param offset Offset of the first issue to retrieve starting from 1
	 * @param count Maximum number of issues to retrieve
	 * @return The issues associated with the shop
	 * @throws DaoException In case it is not possible to retrieve the issues
	 */
	public List<IssueEntity> getByShopId(int shopId, int offset, int count) throws DaoException;
	
	/**
	 * Inserts a new open issue. Fields that are set only on a closed issue are ignored.
	 * 
	 * @param issue The issue to insert
	 * @throws DaoException In case it is not possible to insert the issue
	 */
	public int insert(IssueEntity issue) throws DaoException;
	
	/**
	 * Updates the issue with the id specified in the entity object
	 * 
	 * @param issue The issue to update
	 * @throws DaoException In case it is not possible to update the issue
	 */
	public void update(IssueEntity issue) throws DaoException;
	
}
