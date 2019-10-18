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

import java.math.BigDecimal;

/**
 * Aggregate information about an item
 */
public class ItemAggregateInfo {

	private ItemEntity item;
	private ResourceEntity image;
	private BigDecimal minimumPrice;
	private Rating averageRating;
	private int reviewsCount;

	public ItemAggregateInfo(ItemEntity item, ResourceEntity image, BigDecimal minimumPrice, Rating averageRating, int reviewsCount) {
		this.item = item;
		this.image = image;
		this.minimumPrice = minimumPrice;
		this.averageRating = averageRating;
		this.reviewsCount = reviewsCount;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}

	public ResourceEntity getImage() {
		return image;
	}

	public void setImage(ResourceEntity image) {
		this.image = image;
	}

	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public Rating getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Rating averageRating) {
		this.averageRating = averageRating;
	}

	public int getReviewsCount() {
		return reviewsCount;
	}

	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}
	
}
