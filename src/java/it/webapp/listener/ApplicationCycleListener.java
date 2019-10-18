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

package it.webapp.listener;

import it.webapp.applicationcontroller.PersistenceHelper;
import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.indexing.LuceneIndexHelper;
import it.webapp.logging.Logger;
import it.webapp.mail.MailManager;
import it.webapp.spatial.GeocodingManager;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Default application cycle listener.
 * Perfroms startup initialization and resource cleanup at shutdown
 */
public class ApplicationCycleListener implements ServletContextListener {
	
	private static final String GOOGLE_API_KEY_PARAM = "google_api_key";
	
	private static final String MAIL_USERNAME_PARAM = "mail_username";
	private static final String MAIL_PASSWORD_PARAM = "mail_password";
	
	private static final String UPLOADS_DIR_PARAM = "uploads_dir";
	
	private static final String LUCENE_INDEX_DIRECTORY_PARAM = "lucene_index_directory";
	
	private static final String DRIVER_CLASS_PARAM = "driverClassName";
    private static final String DB_URL_PARAM = "dbUrlParam";
    private static final String DB_USERNAME_PARAM = "dbUsernameParam";
    private static final String DB_PASSWORD_PARAM = "dbPasswordParam";
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        
		//Initializes the logger
		Logger.init(context);
		
		GeocodingManager.init(context.getInitParameter(GOOGLE_API_KEY_PARAM));
		
		//Initializes the mail manager with mail account credentials
		MailManager.init(context.getInitParameter(MAIL_USERNAME_PARAM), context.getInitParameter(MAIL_PASSWORD_PARAM));
		
		//Initializes the persitence helper with the default download directory
		PersistenceHelper.init(context.getInitParameter(UPLOADS_DIR_PARAM));
		
		//Gets the db initialization parameters and initializes the DaoFactory
        String driverClassName = context.getInitParameter(DRIVER_CLASS_PARAM);
        String dbUrl = context.getInitParameter(DB_URL_PARAM);
        String username = context.getInitParameter(DB_USERNAME_PARAM);
        String password = context.getInitParameter(DB_PASSWORD_PARAM);
        
        try {
            JdbcDaoFactory.init(driverClassName, dbUrl, username, password);
        } catch(DaoFactoryException e) {
            Logger.log("Error: DB connection not properly initialized, shutting down ", e);
            System.exit(1);
        }
		
		//Initializes the Lucene index
		String luceneIndexDirectory = context.getInitParameter(LUCENE_INDEX_DIRECTORY_PARAM);
		try {
			LuceneIndexHelper.init(luceneIndexDirectory);
		} catch(IOException | DaoFactoryException | DaoException e) {
			Logger.log("Error: Lucene index not properly initialized, shutting down", e);
			System.exit(1);
		}

        /*
        try {
            System.out.println("Initializing JdbcDaoFactory");
            JdbcDaoFactory.init("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/shop_database", "root", "");
        } catch(DaoFactoryException e) {
            Logger.log("Error: DB connection not properly initialized, shutting down ", e);
            System.exit(1);
        }
              
        //Populates the db
        try {
            System.out.println("Opening DB");
            DatabaseHelper.open();
            System.out.println("Populating users, shops, addresses");
            DatabaseHelper.insertUsers();
            System.out.println("Populating items");
            DatabaseHelper.insertItems();
            System.out.println("Populating shop orders");
            DatabaseHelper.insertShopOrders();
            System.out.println("Terminated partially population");
        } catch(SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }*/
    }
    

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
		//Closes the DaoFactory
        try {
            JdbcDaoFactory.close();
        } catch(DaoFactoryException e) {
			ServletContext context = sce.getServletContext();
            Logger.log("Error: DB connection not properly shutdown, terminating forcefully", e);
            System.exit(1);
        }
    }
}
