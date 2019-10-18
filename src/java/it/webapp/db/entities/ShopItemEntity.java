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

public class ShopItemEntity {

	private Integer shopItemId;
	private Integer shopId;
	private Integer itemId;
	private BigDecimal price;
	private Integer availability;

	public ShopItemEntity(Integer shopItemId, Integer shopId, Integer itemId, BigDecimal price, Integer availability) {
		this.shopItemId = shopItemId;
		this.shopId = shopId;
		this.itemId = itemId;
		this.price = price;
		this.availability = availability;
	}

	public Integer getShopItemId() {
		return shopItemId;
	}

	public void setShopItemId(Integer shopItemId) {
		this.shopItemId = shopItemId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
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

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}
	
}