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

package it.webapp.db.entities;

import java.util.Date;

public class ItemReviewEntity {

	private Integer itemReviewId;
	private String title;
	private String description;
	private Rating rating;
	private Date date;
	private Integer shopOrderItemId;

	public ItemReviewEntity(Integer itemReviewId, String title, String description, Rating rating, Date date, Integer shopOrderItemId) {
		this.itemReviewId = itemReviewId;
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.date = date;
		this.shopOrderItemId = shopOrderItemId;
	}

	public Integer getItemReviewId() {
		return itemReviewId;
	}

	public void setItemReviewId(Integer itemReviewId) {
		this.itemReviewId = itemReviewId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getShopOrderItemId() {
		return shopOrderItemId;
	}

	public void setShopOrderItemId(Integer shopOrderItemId) {
		this.shopOrderItemId = shopOrderItemId;
	}
	
	
}
