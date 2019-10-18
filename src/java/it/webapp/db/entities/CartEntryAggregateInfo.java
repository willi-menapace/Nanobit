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
 * Represents aggregate information about an item in the cart of an user
 */
public class CartEntryAggregateInfo {
	
	private ItemEntity item;
	private ResourceEntity image;
	private ShopEntity shop;
	private ShopItemEntity shopItem;
	private CartEntryEntity cartEntry;

	public CartEntryAggregateInfo(ItemEntity item, ResourceEntity image, ShopEntity shop, ShopItemEntity shopItem, CartEntryEntity cartEntry) {
		this.item = item;
		this.image = image;
		this.shop = shop;
		this.shopItem = shopItem;
		this.cartEntry = cartEntry;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}

	public ResourceEntity getImage() {
		return image;
	}

	public void setImage(ResourceEntity image) {
		this.image = image;
	}

	public ShopEntity getShop() {
		return shop;
	}

	public void setShop(ShopEntity shop) {
		this.shop = shop;
	}

	public ShopItemEntity getShopItem() {
		return shopItem;
	}

	public void setShopItem(ShopItemEntity shopItem) {
		this.shopItem = shopItem;
	}

	public CartEntryEntity getCartEntry() {
		return cartEntry;
	}

	public void setCartEntry(CartEntryEntity cartEntry) {
		this.cartEntry = cartEntry;
	}
	
}
