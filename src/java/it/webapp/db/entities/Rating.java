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

public class Rating {
	private static final int MIN = 0;
	private static final int MAX = 10;
	
	private int rating;
	
	public Rating(int rating) {
		setRating(rating);
	}
	
	public void setRating(int rating) {
		if(rating < Rating.MIN || rating > Rating.MAX) {
			throw new IllegalArgumentException("Rating must be a value between " + Rating.MIN + " and " + Rating.MAX);
		}
		
		this.rating = rating;
	}
	
	public int getRating() {
		return this.rating;
	}
}
