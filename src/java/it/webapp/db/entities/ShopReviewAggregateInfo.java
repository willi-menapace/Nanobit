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

/**
 * Aggregate information about the reviews of a shop
 */
public class ShopReviewAggregateInfo {

	private Rating averageRating;
	private int ratingsCount;

	public ShopReviewAggregateInfo(Rating averageRating, int ratingsCount) {
		this.averageRating = averageRating;
		this.ratingsCount = ratingsCount;
	}

	public Rating getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Rating averageRating) {
		this.averageRating = averageRating;
	}

	public int getRatingsCount() {
		return ratingsCount;
	}

	public void setRatingsCount(int ratingsCount) {
		this.ratingsCount = ratingsCount;
	}
	
}
