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

import it.webapp.db.entities.ResourceEntity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import javax.imageio.IIOException;
import javax.servlet.http.Part;

/**
 * Helper class	for resource persistance management
 */
public class PersistenceHelper {
	
	public static File uploadsDir;
	
	private PersistenceHelper() {
		//Enforces non instantiability
	}
	
	/**
	 * Sets the default upload directory
	 * 
	 * @param uploadsDirPath Path to the default upload directory
	 */
	public static void init(String uploadsDirPath) {
		uploadsDir = new File(uploadsDirPath);
	}
	
	/**
	 * Saves a resource on the default directory.
	 * PersistenceHelper must be initialized
	 * 
	 * @param requestPart The part to persist
	 * @return ResourceEntity indicating the generated resource
	 * @throws IOException In case it is not possible to persist the resource
	 */
	public static ResourceEntity save(Part requestPart) throws IOException {
		if(uploadsDir == null) {
			throw new IllegalStateException("Persistance helper must be initialized before being used");
		}
		if(requestPart == null) {
			throw new IllegalArgumentException("Part must not be null");
		}
		
		//Obtains the original resource name and generates a unique one
		String originalFilename = Paths.get(requestPart.getSubmittedFileName()).getFileName().toString(); //Fixes IE
		String[] tokens = originalFilename.split("[.]");
		String extension = tokens[tokens.length - 1];
		String uuidName = UUID.randomUUID().toString() + extension;
		
		//Persists the file on disk
		InputStream fileInput = requestPart.getInputStream();
		File outFile = new File(uploadsDir, uuidName);
		Files.copy(fileInput, outFile.toPath());

		ResourceEntity resource = new ResourceEntity(null, uuidName, originalFilename);
		return resource;
	}
	
	/**
	 * Deletes a resource in the default resource directory
	 * 
	 * @param resource The resource to delete
	 * @throws IOException In case it is not possible to delete the resource
	 */
	public static void delete(ResourceEntity resource) throws IOException {
		if(resource == null) {
			throw new IllegalArgumentException("Resource must be not null");
		}
		
		File targetFile = new File(uploadsDir, resource.getFilename());
		targetFile.delete();
	}
	
	/**
	 * Deletes a resource in the default resource directory
	 * 
	 * @param resources The resources to delete
	 * @throws IOException In case it is not possible to delete the resources completely
	 */
	public static void delete(List<ResourceEntity> resources) throws IOException {
		if(resources == null) {
			throw new IllegalArgumentException("Resource must be not null");
		}
		
		boolean exceptionHappened = false;
		for(ResourceEntity currentResource : resources) {
			try {
				PersistenceHelper.delete(currentResource);
			} catch(IOException e2) {
				exceptionHappened = true;
			}
		}
		if(exceptionHappened) {
			throw new IIOException("Cannot delete all requested files");
		}
	}
	
	
	
						

	
}
