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
import it.webapp.db.entities.NotificationEntity;
import java.util.List;

public interface NotificationDao extends Dao<NotificationEntity> {
	
	/**
	 * Retrieves the NotificationEntity with the specified id
	 * 
	 * @param notificationId The id of the NotificationEntity to retrieve
	 * @return The specified NotificationEntity, null if it does not exist
	 * @throws DaoException In case the NotificationEntity does not exist
	 */
	public NotificationEntity getById(int notificationId) throws DaoException;
	
	/**
	 * Retrieves the NotificationEntities associated with a given user in chronological order
	 * 
	 * @param userId The associated user
	 * @param offset The offset of the first record to retrieve starting from 1
	 * @param count The maximum number of records to retrieve
	 * @param includeAdmin Includes in the results notifications for all admins
	 * @return List of specified NotificationEntities in chronological order
	 * @throws DaoException In case it is not possible to retrieve the NotificationEntities
	 */
	public List<NotificationEntity> getByUserId(int userId, int offset, int count, boolean includeAdmin) throws DaoException;
	
	/**
	 * Inserts a new NotificationEntity
	 * 
	 * @param notification The notification to insert
	 * @throws DaoException In case it is not possible to insert the NotificationEntity
	 */
	public void insert(NotificationEntity notification) throws DaoException;
	
	/**
	 * Deletes a NotificationEntity
	 * 
	 * @param notification The NotificationEntity to update
	 * @throws DaoException In case it is not possible to delete the NotificationEntity
	 */
	public void update(NotificationEntity notification) throws DaoException;
	
}
