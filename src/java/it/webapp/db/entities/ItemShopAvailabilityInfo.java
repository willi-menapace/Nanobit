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
 * Represent availability information for an item
 */
public class ItemShopAvailabilityInfo {

	private UserEntity shopUser;
	private ShopEntity shop;
	private AddressEntity shopAddress;
	private ShopItemEntity shopItemEntity;
	private Rating aggregateShopRating;
	private int shopReviewsCount;
	private boolean pickupFromShop;

	public ItemShopAvailabilityInfo(UserEntity shopUser, ShopEntity shop, AddressEntity shopAddress, ShopItemEntity shopItemEntity, Rating aggregateShopRating, int shopReviewsCount, boolean pickupFromShop) {
		this.shopUser = shopUser;
		this.shop = shop;
		this.shopAddress = shopAddress;
		this.shopItemEntity = shopItemEntity;
		this.aggregateShopRating = aggregateShopRating;
		this.shopReviewsCount = shopReviewsCount;
		this.pickupFromShop = pickupFromShop;
	}

	public UserEntity getShopUser() {
		return shopUser;
	}

	public void setShopUser(UserEntity shopUser) {
		this.shopUser = shopUser;
	}

	public ShopEntity getShop() {
		return shop;
	}

	public void setShop(ShopEntity shop) {
		this.shop = shop;
	}

	public AddressEntity getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(AddressEntity shopAddress) {
		this.shopAddress = shopAddress;
	}

	public ShopItemEntity getShopItemEntity() {
		return shopItemEntity;
	}

	public void setShopItemEntity(ShopItemEntity shopItemEntity) {
		this.shopItemEntity = shopItemEntity;
	}

	public Rating getAggregateShopRating() {
		return aggregateShopRating;
	}

	public void setAggregateShopRating(Rating aggregateShopRating) {
		this.aggregateShopRating = aggregateShopRating;
	}

	public int getShopReviewsCount() {
		return shopReviewsCount;
	}

	public void setShopReviewsCount(int shopReviewsCount) {
		this.shopReviewsCount = shopReviewsCount;
	}

	public boolean isPickupFromShop() {
		return pickupFromShop;
	}

	public void setPickupFromShop(boolean pickupFromShop) {
		this.pickupFromShop = pickupFromShop;
	}
	
	
	
}
