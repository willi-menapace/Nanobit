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

public class IssueEntity {
	
	private Integer issueId;
	private String description;
	private Date openDate;
	private IssueResult issueResult;
	private String motivation;
	private Date closeDate;
	private Integer shopOrderItemId;

	/**
	 * Creates an open issue that refers to given ShopOrderItem
	 * @param description Description of the issue
	 * @param shopOrderItemId The contested item
	 */
	public IssueEntity(String description, Integer shopOrderItemId) {
		this.issueId = null;
		this.description = description;
		this.openDate = new Date(); //Issue is opened right now
		this.issueResult = null;
		this.motivation = null;
		this.closeDate = null;
		this.shopOrderItemId = shopOrderItemId;
	}
	
	public IssueEntity(Integer issueId, String description, Date openDate, IssueResult issueResult, String motivation, Date closeDate, Integer shopOrderItemId) {
		this.issueId = issueId;
		this.description = description;
		this.openDate = openDate;
		this.issueResult = issueResult;
		this.motivation = motivation;
		this.closeDate = closeDate;
		this.shopOrderItemId = shopOrderItemId;
	}

	/**
	 * Closes an issue
	 * 
	 * @param issueResult The result of the issue evaluation, not null
	 * @param motivation The motivation for the decision, not null, not empty
	 */
	public void closeIssue(IssueResult issueResult, String motivation) {
		
		if(issueResult == null) {
			throw new IllegalArgumentException("Issue result must not be null");
		}
		if(motivation == null || motivation.isEmpty()) {
			throw new IllegalArgumentException("Motivation must be a valid non empty string");
		}
		
		this.issueResult = issueResult;
		this.motivation = motivation;
		this.closeDate = new Date();
	}
	
	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public IssueResult getIssueResult() {
		return issueResult;
	}

	public void setIssueResult(IssueResult issueResult) {
		this.issueResult = issueResult;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Integer getShopOrderItemId() {
		return shopOrderItemId;
	}

	public void setShopOrderItemId(Integer shopOrderItemId) {
		this.shopOrderItemId = shopOrderItemId;
	}
	
}
