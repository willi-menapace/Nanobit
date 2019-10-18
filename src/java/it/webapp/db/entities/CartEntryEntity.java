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

public class CartEntryEntity {

	private Integer cartEntryId;
	private Integer userId;
	private Integer shopItemId;
	private Integer quantity;

	public CartEntryEntity(Integer cartEntryId, Integer userId, Integer shopItemId, Integer quantity) {
		this.cartEntryId = cartEntryId;
		this.userId = userId;
		this.shopItemId = shopItemId;
		this.quantity = quantity;
	}

	public Integer getCartEntryId() {
		return cartEntryId;
	}

	public void setCartEntryId(Integer cartEntryId) {
		this.cartEntryId = cartEntryId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getShopItemId() {
		return shopItemId;
	}

	public void setShopItemId(Integer shopItemId) {
		this.shopItemId = shopItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}