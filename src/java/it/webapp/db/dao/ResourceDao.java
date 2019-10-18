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
import it.webapp.db.entities.ResourceEntity;
import java.util.List;

public interface ResourceDao extends Dao<ResourceEntity> {
	
	public List<ResourceEntity> getByShopId(int shopId) throws DaoException;
	
	public List<ResourceEntity> getByItemId(int itemId) throws DaoException;
	
	public void insertShopImage(ResourceEntity resource, int shopId) throws DaoException;
	
	public void insertItemImage(ResourceEntity resource, int itemId) throws DaoException;
	
	public void delete(int resourceId) throws DaoException;
	
	public void update(ResourceEntity resource) throws DaoException;
	
}
