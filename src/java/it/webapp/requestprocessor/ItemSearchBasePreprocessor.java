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

package it.webapp.requestprocessor;

import it.webapp.db.entities.Department;
import javax.servlet.http.HttpServletRequest;

/**
 * Preprocesses a request for a base page on which to view search results
 */
public class ItemSearchBasePreprocessor implements RequestPreprocessor {

    public static final String TERM_PARAM = "item_search_base_preprocessor_term";
	public static final String DEPARTMENT_PARAM = "item_search_base_preprocessor_department";
    
    public static final String TERM_ATTRIBUTE = "item_search_base_preprocessor_term";
	public static final String DEPARTMENT_ATTRIBUTE = "item_search_base_preprocessor_department";
    
    @Override
    public boolean parseAndValidate(HttpServletRequest request) {
        
        String term = request.getParameter(TERM_PARAM);
        String departmentString = request.getParameter(DEPARTMENT_PARAM);
        
        Department department = null;
        if(departmentString != null && !departmentString.isEmpty()){
            try{
                department = Department.getById(Integer.parseInt(departmentString));
            }catch(NumberFormatException exception){
                return false;
            }
        }
        
        // If the term is empty we set it to null
        if(term != null && term.isEmpty()){
            term = null;
        }
        
        request.setAttribute(TERM_ATTRIBUTE, term);
        request.setAttribute(DEPARTMENT_ATTRIBUTE, department);
        
        return true;
    }

    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        //No authentication required
		return true;
    }

    @Override
    public boolean checkAuthorization(HttpServletRequest request) {
        //No authorization required
		return true;
    }
    
}
