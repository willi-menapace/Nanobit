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

package it.webapp.payment;

/**
 * Credit card information
 */
public class CreditCard {

	private String owner;
	private String number;
	private int expirationYear;
	private int expirationMonth;
	private String securityCode;

	/**
	 * Creates a new credit card
	 * 
	 * @param owner The credit card owner, not null, not empty
	 * @param number The credit card number, not null, not empty
	 * @param expirationYear The expiration year
	 * @param expirationMonth The expiration month, from 1 to 12
	 * @param securityCode The card security code, not null, not empty
	 */
	public CreditCard(String owner, String number, int expirationYear, int expirationMonth, String securityCode) {
		if(owner == null || owner.isEmpty()) {
			throw new IllegalArgumentException("Owner must me a not empty string");
		}
		if(number == null || number.isEmpty()) {
			throw new IllegalArgumentException("Number must me a not empty string");
		}
		if(expirationMonth <= 0 || expirationMonth > 12) {
			throw new IllegalArgumentException("Expiration month must be a valid month");
		}
		if(securityCode == null || securityCode.isEmpty()) {
			throw new IllegalArgumentException("Security Code must me a not empty string");
		}
		
		this.owner = owner;
		this.number = number;
		this.expirationYear = expirationYear;
		this.expirationMonth = expirationMonth;
		this.securityCode = securityCode;
	}

	public String getOwner() {
		return owner;
	}

	public String getNumber() {
		return number;
	}

	public int getExpirationYear() {
		return expirationYear;
	}

	public int getExpirationMonth() {
		return expirationMonth;
	}

	public String getSecurityCode() {
		return securityCode;
	}
	
}
