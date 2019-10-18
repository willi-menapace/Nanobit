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

import java.math.BigDecimal;

/**
 * Helper class to manage payments
 */
public class PaymentGateway {
	
	private PaymentGateway() {
		//Enforces non instantiability
	}
	
	/**
	 * Performs a payment transaction
	 * 
	 * @param creditCart The credit card to use
	 * @param amount The amount to pay
	 * @return true in case of success
	 */
	public static boolean pay(CreditCard creditCart, BigDecimal amount) {
		//Simulates successful payment
		return true;
	}
}
