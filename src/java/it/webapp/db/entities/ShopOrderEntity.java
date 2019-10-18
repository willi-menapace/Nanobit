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
import java.util.Date;

public class ShopOrderEntity {
	
	private Integer shopOrderId;
	private Date date;
	private Integer userId;
	private Integer shopId;
	private Integer addressId;
	private ShipmentType shipmentType;
	private BigDecimal shipmentPrice;

	public ShopOrderEntity(Integer shopOrderId, Date date, Integer userId, Integer shopId, Integer addressId, ShipmentType shipmentType, BigDecimal shipmentPrice) {
		this.shopOrderId = shopOrderId;
		this.date = date;
		this.userId = userId;
		this.shopId = shopId;
		this.addressId = addressId;
		this.shipmentType = shipmentType;
		this.shipmentPrice = shipmentPrice;
	}

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public ShipmentType getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(ShipmentType shipmentType) {
		this.shipmentType = shipmentType;
	}

	public BigDecimal getShipmentPrice() {
		return shipmentPrice;
	}

	public void setShipmentPrice(BigDecimal shipmentPrice) {
		this.shipmentPrice = shipmentPrice;
	}
	
}
