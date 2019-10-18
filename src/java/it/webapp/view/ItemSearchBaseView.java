/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author:
 * 
 ******************************************************************************/

package it.webapp.view;

import it.webapp.db.entities.Department;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sends as response a base page on which to view search results
 */
public class ItemSearchBaseView implements View {
    
    public static final String TERM_ATTRIBUTE = "item_search_base_view_term";
	public static final String DEPARTMENT_ATTRIBUTE = "item_search_base_view_department";
    private static final String PAGE_URL = "/jsp/ItemSearchBase.jsp";
    
    public static final void setRequestParameters(String term, Department department, HttpServletRequest request){
        if(request == null) {
			throw new IllegalArgumentException("Request must be non null");
		}
    
        request.setAttribute(TERM_ATTRIBUTE, term);
        request.setAttribute(DEPARTMENT_ATTRIBUTE, department);
    }
    
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
            request.getRequestDispatcher(PAGE_URL).forward(request, response);
        } catch (IOException | ServletException exception) {
            throw new IllegalArgumentException("Invalid redirect path " +  request.getContextPath() + PAGE_URL, exception);
        }
	}

}
