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
 * The authentication status of the user
 */
public interface AuthenticationInfo {

	public static final String SESSION_ATTRIBUTE = "authentication_info";
	
	/**
	 * @return True if the user is authenticated
	 */
	public boolean isAuthenticated();
	
	/**
	 * @return True if the User is a Shop
	 */
	public boolean isShop();
	
	/**
	 * @return True if the User is an Admin
	 */
	public boolean isAdmin();
	
	/**
	 * Gets the id of the represented user
	 * 
	 * @return The id of the User, null if the User is not authenticated
	 */
	public Integer getUserId();
    
    /**
     * Gets the username of the represented user
     * 
     * @return The username of the User, null if the User is not authenticated
     */
    public String getUsername();
	
}
