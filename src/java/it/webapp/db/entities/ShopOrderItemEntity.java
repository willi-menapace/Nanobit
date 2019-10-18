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

public class ShopOrderItemEntity {
	private Integer shopOrderItemId;
	private Integer shopOrderId;
	private Integer itemId;
	private BigDecimal price;
	private Integer quantity;

	public ShopOrderItemEntity(Integer shopOrderItemId, Integer shopOrderId, Integer itemId, BigDecimal price, Integer quantity) {
		this.shopOrderItemId = shopOrderItemId;
		this.shopOrderId = shopOrderId;
		this.itemId = itemId;
		this.price = price;
		this.quantity = quantity;
	}

	public Integer getShopOrderItemId() {
		return shopOrderItemId;
	}

	public void setShopOrderItemId(Integer shopOrderItemId) {
		this.shopOrderItemId = shopOrderItemId;
	}

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}