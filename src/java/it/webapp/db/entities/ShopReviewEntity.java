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

public class ShopReviewEntity {
	private Integer shopReviewId;
	private String title;
	private String description;
	private Rating rating;
	private Date date;
	private Integer shopOrderId;

	public ShopReviewEntity(Integer shopReviewId, String title, String description, Rating rating, Date date, Integer shopOrderId) {
		this.shopReviewId = shopReviewId;
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.date = date;
		this.shopOrderId = shopOrderId;
	}

	public Integer getShopReviewId() {
		return shopReviewId;
	}

	public void setShopReviewId(Integer shopReviewId) {
		this.shopReviewId = shopReviewId;
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

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}
	
	
}