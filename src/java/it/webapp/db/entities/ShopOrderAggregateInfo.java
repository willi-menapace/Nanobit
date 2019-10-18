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

import java.util.List;

/**
 * Aggregate information about a shop order
 */
public class ShopOrderAggregateInfo {

	private ShopOrderEntity shopOrder;
        private UserEntity user; //Seller
	private AddressEntity shipmentAddress;
	private ShopReviewEntity shopReview; //Can be null

	private List<ShopOrderStatusEntity> orderStatuses;
	private List<ShopOrderItemAggregateInfo> shopOrderItemsAggregate;

	public ShopOrderAggregateInfo(ShopOrderEntity shopOrder, UserEntity user, AddressEntity shipmentAddress, ShopReviewEntity shopReview, List<ShopOrderStatusEntity> orderStatuses, List<ShopOrderItemAggregateInfo> shopOrderItemsAggregate) {
		if(shopOrder == null || shipmentAddress == null || orderStatuses == null || shopOrderItemsAggregate == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		
		this.shopOrder = shopOrder;
                this.user = user;
		this.shipmentAddress = shipmentAddress;
		this.shopReview = shopReview;
		this.orderStatuses = orderStatuses;
		this.shopOrderItemsAggregate = shopOrderItemsAggregate;
	}

	public ShopOrderEntity getShopOrder() {
		return shopOrder;
	}

	public void setShopOrder(ShopOrderEntity shopOrder) {
		if(shopOrder == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
				
		this.shopOrder = shopOrder;
	}
        
        public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		if(user == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		this.user = user;
	}
        
	public AddressEntity getShipmentAddress() {
		return shipmentAddress;
	}

	public void setShipmentAddress(AddressEntity shipmentAddress) {
		if(shipmentAddress == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		this.shipmentAddress = shipmentAddress;
	}

	public ShopReviewEntity getShopReview() {
		return shopReview;
	}

	public void setShopReview(ShopReviewEntity shopReview) {
		this.shopReview = shopReview;
	}

	public List<ShopOrderStatusEntity> getOrderStatuses() {
		return orderStatuses;
	}

	public void setOrderStatuses(List<ShopOrderStatusEntity> orderStatuses) {
		if(orderStatuses == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		
		this.orderStatuses = orderStatuses;
	}

	public List<ShopOrderItemAggregateInfo> getShopOrderItemsAggregate() {
		return shopOrderItemsAggregate;
	}

	public void setShopOrderItemsAggregate(List<ShopOrderItemAggregateInfo> shopOrderItemsAggregate) {
		if(shopOrderItemsAggregate == null) {
			throw new IllegalArgumentException("Unexpected null parameter");
		}
		
		this.shopOrderItemsAggregate = shopOrderItemsAggregate;
	}
	
	
}
