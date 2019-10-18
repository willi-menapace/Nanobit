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

public class UserEntity {
	
	private Integer userId;
	private String username;
	private HashDigest passwordHash;
	private String firstName;
	private String lastName;
	private Integer addressId;
	private String email;
	private Date registrationDate;
	private SecurityCode registrationCode;
	private boolean activated;
	private SecurityCode passwordResetCode;

	public UserEntity(Integer userId, String username, HashDigest passwordHash, String firstName, String lastName, Integer addressId, String email, Date registrationDate, SecurityCode registrationCode, boolean activated, SecurityCode passwordResetCode) {
		this.userId = userId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressId = addressId;
		this.registrationDate = registrationDate;
		this.email = email;
		this.registrationCode = registrationCode;
		this.activated = activated;
		this.passwordResetCode = passwordResetCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public HashDigest getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(HashDigest passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public SecurityCode getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(SecurityCode registrationCode) {
		this.registrationCode = registrationCode;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public SecurityCode getPasswordResetCode() {
		return passwordResetCode;
	}

	public void setPasswordResetCode(SecurityCode passwordResetCode) {
		this.passwordResetCode = passwordResetCode;
	}
	
}
