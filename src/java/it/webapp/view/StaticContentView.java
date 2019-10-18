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

import it.webapp.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticContentView implements View {

	public static final String RESOURCE_ATTRIBUTE = "static_content_view_resource";
	
	/**
	 * Convenience method for setting View parameters
	 * @param resource The resource to serve
	 * @param request The request on which to set the parameters
	 */
	public static final void setRequestParameters(String resource, HttpServletRequest request) {
		if(resource == null || resource.isEmpty()) {
			throw new IllegalArgumentException("Resource must be a non empty stirng");
		}

		request.setAttribute(RESOURCE_ATTRIBUTE, resource);
	}
	
	@Override
	public void view(HttpServletRequest request, HttpServletResponse response) {
		
		//Gets directory and file name
		String resource = (String) request.getAttribute(RESOURCE_ATTRIBUTE);
		String uploadFilesDirectory = request.getServletContext().getInitParameter("uploads_dir");
		
        File file = new File(uploadFilesDirectory, resource);
        response.setHeader("Content-Type", request.getServletContext().getMimeType(resource));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        try {
			Files.copy(file.toPath(), response.getOutputStream());
		} catch(IOException e) {
			Logger.log("Could not process static resource request for resource: " + resource, e);
		}
	}

}
