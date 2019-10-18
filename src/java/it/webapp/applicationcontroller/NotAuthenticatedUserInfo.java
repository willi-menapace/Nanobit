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
 * A not authenticated user
 */
public class NotAuthenticatedUserInfo implements AuthenticationInfo {

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public boolean isShop() {
		return false;
	}

	@Override
	public boolean isAdmin() {
		return false;
	}

	@Override
	public Integer getUserId() {
		return null;
	}

    @Override
    public String getUsername() {
        return null;
    }

}
