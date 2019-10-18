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

public class ShopEntity {
	
	private Integer shopId;
	private String description;
	private String vatNumber;
	private String phoneNumber;
	private Date upgradeDate;

	public ShopEntity(Integer shopId, String description, String vatNumber, String phoneNumber, Date upgradeDate) {
		this.shopId = shopId;
		this.description = description;
		this.vatNumber = vatNumber;
		this.phoneNumber = phoneNumber;
		this.upgradeDate = upgradeDate;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getUpgradeDate() {
		return upgradeDate;
	}

	public void setUpgradeDate(Date upgradeDate) {
		this.upgradeDate = upgradeDate;
	}
	
}
