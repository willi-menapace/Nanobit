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
import it.webapp.db.entities.Department;
import it.webapp.db.entities.ItemAggregateInfo;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ItemShopAvailabilityInfo;
import it.webapp.spatial.GeoFence;
import java.math.BigDecimal;
import java.util.List;

public interface ItemDao extends Dao<ItemEntity> {
	
	public enum SortOrder {
		RELEVANCE(0, "rilevanza"),
		PRICE_ASC(1, "prezzo crescente"),
		PRICE_DESC(2, "prezzo decrescente");
		
        private final int sortOrderID;
        private final String sortOrderDescription;
        
		public static SortOrder getById(int sortOrderId) {
			SortOrder sortOrder = null;
			switch(sortOrderId) {
				case 0:
					sortOrder = RELEVANCE;
					break;
				case 1:
					sortOrder = PRICE_ASC;
					break;
				case 2:
					sortOrder = PRICE_DESC;
					break;
				default:
					throw new IllegalArgumentException("Specified department id is not valid");
			}
			return sortOrder;
		}
        
        SortOrder(int sortOrderID, String sortOrderDescription){
            this.sortOrderID = sortOrderID;
            this.sortOrderDescription = sortOrderDescription;
        }
        
        public int getSortOrderID(){
            return sortOrderID;
        }
        
        public String getSortOrderDescription(){
            return sortOrderDescription;
        }
	}
	
	/**
	 * Gets all items
	 * 
	 * @return List of all the items
	 * @throws DaoException In case it is not possible to obtain all the items
	 */
	public List<ItemEntity> getAll() throws DaoException;
	
	/**
	 * Gets the item with the specified id
	 * 
	 * @param itemId The id of the item to retrieve
	 * @return The item with the given id, null if it does not exits
	 * @throws DaoException In case it is not possible to retrieve the item
	 */
	public ItemEntity getById(int itemId) throws DaoException;
	
	/**
	 * Retrieves extended information about items that satisfy the given filters
	 * 
	 * @param term textual item description, optional
	 * @param department category of the item, optional
	 * @param priceLow lower price limit, optional
	 * @param priceHigh upper price limit, optional
	 * @param geoFence spatial search bounds, retrieves only items with shops with hand pick shipment method in the specified zone, optional
	 * @param sortOrder results sort order, not null
	 * @param offset number of the first entry to retrieve, starts from 1, not null
	 * @param count maximum number of entries to retrieve. not null
	 * @return Extended information about items that match the filters
	 * @throws DaoException In case it is not possible to retrieve the items
	 */
	public List<ItemAggregateInfo> getFromAdvancedSearch(String term, Department department,
														 BigDecimal priceLow, BigDecimal priceHigh,
														 GeoFence geoFence,
														 SortOrder sortOrder,
														 int offset, int count) throws DaoException;
	
	/**
	 * Gets aggregate information about the item with the specified id
	 * 
	 * @param itemId The id of the referred item
	 * @return Aggregate information about the given item, null if it does not exits
	 * @throws DaoException In case it is not possible to the item information
	 */
	public ItemAggregateInfo getAggregateInfoById(int itemId) throws DaoException;
	
	/**
	 * Retrieves various information about the availability of items in the shops
	 * 
	 * @param itemId The item of which obtain availability information
	 * @return List of availability information with one item for each shop
	 * @throws DaoException In case it is not possible to obtain the list
	 */
	public List<ItemShopAvailabilityInfo> getShopAvailabilityInfoByItemId(int itemId) throws DaoException;
	
	/**
	 * Inserts an ItemEntity
	 * 
	 * @param item The record to insert
	 * @return id of the inserted ItemEntity
	 * @throws DaoException In case it is not possible to insert the record
	 */
	public int insert(ItemEntity item) throws DaoException;
}
