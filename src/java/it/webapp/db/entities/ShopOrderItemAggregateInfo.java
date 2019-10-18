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
 * Aggregate info about a shop order item
 */
public class ShopOrderItemAggregateInfo {

	private ShopOrderItemEntity shopOrderItem;
	private ItemEntity item;
	private ResourceEntity image;		 //Can be null
	private ItemReviewEntity itemReview; //Can be null

	public ShopOrderItemAggregateInfo(ShopOrderItemEntity shopOrderItem, ItemEntity item, ResourceEntity image, ItemReviewEntity itemReview) {
		if(shopOrderItem == null || item == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		
		this.shopOrderItem = shopOrderItem;
		this.item = item;
		this.image = image;
		this.itemReview = itemReview;
	}

	public ShopOrderItemEntity getShopOrderItem() {
		return shopOrderItem;
	}

	public void setShopOrderItem(ShopOrderItemEntity shopOrderItem) {
		if(shopOrderItem == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		this.shopOrderItem = shopOrderItem;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		if(item == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		this.item = item;
	}

	public ResourceEntity getImage() {
		return image;
	}

	public void setImage(ResourceEntity image) {
		this.image = image;
	}

	public ItemReviewEntity getItemReview() {
		return itemReview;
	}

	public void setItemReview(ItemReviewEntity itemReview) {
		this.itemReview = itemReview;
	}
	
	
	
}
