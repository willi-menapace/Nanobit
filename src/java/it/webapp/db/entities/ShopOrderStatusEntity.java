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

public class ShopOrderStatusEntity {

	private Integer shopOrderId;
	private ShopOrderStatusType shopOrderStatus;
	private Date changeDate;

	public ShopOrderStatusEntity(Integer shopOrderId, ShopOrderStatusType shopOrderStatus, Date changeDate) {
		this.shopOrderId = shopOrderId;
		this.shopOrderStatus = shopOrderStatus;
		this.changeDate = changeDate;
	}

	public Integer getShopOrderId() {
		return shopOrderId;
	}

	public void setShopOrderId(Integer shopOrderId) {
		this.shopOrderId = shopOrderId;
	}

	public ShopOrderStatusType getShopOrderStatus() {
		return shopOrderStatus;
	}

	public void setShopOrderStatus(ShopOrderStatusType shopOrderStatus) {
		this.shopOrderStatus = shopOrderStatus;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
}