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

package it.webapp.applicationcontroller;

/**
 * Information about an authenticated User
 * Immutable class
 */
public class AuthenticatedUserInfo implements AuthenticationInfo {

	private final int USER_ID;
    private final String USERNAME;
	private final boolean IS_SHOP;
	private final boolean IS_ADMIN;
	
	public AuthenticatedUserInfo(int userId, String username, boolean isShop, boolean isAdmin) {
		this.USER_ID = userId;
        this.USERNAME = username;
		this.IS_SHOP = isShop;
		this.IS_ADMIN = isAdmin;
	}
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public boolean isShop() {
		return IS_SHOP;
	}

	@Override
	public boolean isAdmin() {
		return IS_ADMIN;
	}

	@Override
	public Integer getUserId() {
		return USER_ID;
	}

    @Override
    public String getUsername() {
        return USERNAME;
    }

}
