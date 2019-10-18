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
 * Aggregate information about an issue
 */
public class IssueAggregateInfo {

	private IssueEntity issue;
	private ShopOrderItemEntity shopOrderItem;
	private ItemEntity item;

	public IssueAggregateInfo(IssueEntity issue, ShopOrderItemEntity shopOrderItem, ItemEntity item) {
		this.issue = issue;
		this.shopOrderItem = shopOrderItem;
		this.item = item;
	}

	public IssueEntity getIssue() {
		return issue;
	}

	public void setIssue(IssueEntity issue) {
		this.issue = issue;
	}

	public ShopOrderItemEntity getShopOrderItem() {
		return shopOrderItem;
	}

	public void setShopOrderItem(ShopOrderItemEntity shopOrderItem) {
		this.shopOrderItem = shopOrderItem;
	}

	public ItemEntity getItem() {
		return item;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}
	
}
