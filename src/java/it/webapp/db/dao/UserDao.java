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
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.UserEntity;

public interface UserDao extends Dao<UserEntity> {
	
	/**
	 * Retrieves the user with the given id
	 * 
	 * @param userId The id of the user to retrieve
	 * @return The user with the given id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the user
	 */
	public UserEntity getById(int userId) throws DaoException;
	
		/**
	 * Retrieves the user with the given username
	 * 
	 * @param username The username of the user to retrieve
	 * @return The user with the given id, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the user
	 */
	public UserEntity getByUsername(String username) throws DaoException;
	
	/**
	 * Retrieves the user with the given username and password
	 * 
	 * @param username The username to search
	 * @param password The password
	 * @return UserEntity with the given username and password, null if it does not exist
	 * @throws DaoException In case it is not possible to retrieve the UserEntity
	 */
	public UserEntity getByUsernameAndPassword(String username, HashDigest password) throws DaoException;
	
	/**
	 * Inserts a new user and updates its id
	 * 
	 * @param user The user to insert
	 * @return the id of the inserted record
	 * @throws DaoException In case it is not possible to insert the user
	 */
	public int insert(UserEntity user) throws DaoException;
	
	/**
	 * Updates the user entity with the id specified in user
	 * 
	 * @param user The entity to update
	 * @throws DaoException In case it is not possible to update the user
	 */
	public void update(UserEntity user) throws DaoException;
	
}
