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

package it.webapp.db.populating;

import com.google.maps.model.LatLng;
import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcAddressDao;
import it.webapp.db.dao.jdbc.JdbcAdministratorDao;
import it.webapp.db.dao.jdbc.JdbcCartEntryDao;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcIssueDao;
import it.webapp.db.dao.jdbc.JdbcItemDao;
import it.webapp.db.dao.jdbc.JdbcItemReviewDao;
import it.webapp.db.dao.jdbc.JdbcNotificationDao;
import it.webapp.db.dao.jdbc.JdbcResourceDao;
import it.webapp.db.dao.jdbc.JdbcShopDao;
import it.webapp.db.dao.jdbc.JdbcShopItemDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderItemDao;
import it.webapp.db.dao.jdbc.JdbcShopOrderStatusDao;
import it.webapp.db.dao.jdbc.JdbcShopReviewDao;
import it.webapp.db.dao.jdbc.JdbcShopShipmentTypeDao;
import it.webapp.db.dao.jdbc.JdbcUserDao;
import it.webapp.db.entities.AddressEntity;
import it.webapp.db.entities.AdminIssueOpenedNotificationEntity;
import it.webapp.db.entities.AdministratorEntity;
import it.webapp.db.entities.CartEntryEntity;
import it.webapp.db.entities.Department;
import it.webapp.db.entities.HashDigest;
import it.webapp.db.entities.IssueEntity;
import it.webapp.db.entities.IssueResult;
import it.webapp.db.entities.ItemEntity;
import it.webapp.db.entities.ItemReviewEntity;
import it.webapp.db.entities.NotificationEntity;
import it.webapp.db.entities.Rating;
import it.webapp.db.entities.ResourceEntity;
import it.webapp.db.entities.SecurityCode;
import it.webapp.db.entities.ShipmentType;
import it.webapp.db.entities.ShopEntity;
import it.webapp.db.entities.ShopIssueClosedNotificationEntity;
import it.webapp.db.entities.ShopIssueOpenedNotificationEntity;
import it.webapp.db.entities.ShopItemEntity;
import it.webapp.db.entities.ShopOrderAddedNotification;
import it.webapp.db.entities.ShopOrderEntity;
import it.webapp.db.entities.ShopOrderItemEntity;
import it.webapp.db.entities.ShopOrderStatusEntity;
import it.webapp.db.entities.ShopOrderStatusNotification;
import it.webapp.db.entities.ShopOrderStatusType;
import it.webapp.db.entities.ShopReviewEntity;
import it.webapp.db.entities.ShopReviewNotification;
import it.webapp.db.entities.ShopShipmentTypeEntity;
import it.webapp.db.entities.UserEntity;
import it.webapp.db.entities.UserIssueClosedNotificationEntity;
import it.webapp.spatial.GeocodingManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.fluttercode.datafactory.impl.DataFactory;
import org.joda.time.DateTime;


/**
 *  Helper class for Database management
 *  Must be called open method before being used
 */
public class DatabaseHelper {

    private static Connection shopDatabaseConnection;

    
    private static Connection amazonDatabaseConnection;
    
    private static final String getShopAddressesQuery = "SELECT * FROM address WHERE address_id < 201";
    
    private static final String getUserAddressesQuery = "SELECT * FROM address WHERE address_id >= 201";
    
    private static final String getAdministratorAddressesQuery = "SELECT * FROM address WHERE address_id >= 365 AND address_id <= 368";
    
    private static final String getItemQuery = "SELECT * FROM items";
    
    private static final String getShopQuery = "SELECT * FROM shops";
    
    private static final String getShopNameQuery = "SELECT shopname FROM shops";
    
    private static final String getShopByNameQuery = "SELECT description, vat_number, phone_number, upgrade_date FROM shops WHERE shopname=?";
    
    private static final String getResourceByShopNameQuery = "SELECT shoppictures.picture_id, shoppictures.filename FROM shoppictures, shops WHERE shoppictures.shop_id = shops.shop_id AND shops.shopname=?";
    
    private static final String getResourceByItemTitleQuery = "SELECT pictures.picture_id, pictures.filename\n" +
        "FROM pictures, items\n" +
        "WHERE pictures.item_id = items.item_id AND items.title=?";
    
    private static final String getNumberOfUsersQuery = "SELECT MAX(user_id)\n" +
        "FROM users";
    
    private static final String getNumberOfItemsQuery = "SELECT MAX(item_id)\n" +
        "FROM items";
    
    private static final String getMinIndexOfShopsQuery = "SELECT MIN(shop_id)\n" +
        "FROM shops";
    
    private static final String getNumberOfShopsQuery = "SELECT MAX(shop_id)\n" +
        "FROM shops";
    
    private static final String getReviewsByShopNameQuery = "SELECT shopreviews.review_id, shopreviews.rating, shopreviews.review_text\n" +
        "FROM shopreviews, shops\n" +
        "WHERE shopreviews.shop_id = shops.shop_id AND shops.shopname=?";
    
    private static final String removeShopReviewByIdQuery = "DELETE\n" +
        "FROM shopreviews\n" +
        "WHERE review_id=?";
    
    private static final String getReviewsByItemTitleQuery = "SELECT reviews.review_id, reviews.title, reviews.rating, reviews.review_text\n" +
        "FROM reviews, items\n" +
        "WHERE reviews.item_id = items.item_id AND items.title=?";
    
    private static final String removeItemReviewByIdQuery = "DELETE\n" +
        "FROM reviews\n" +
        "WHERE review_id=?";
    
    private static final String getPriceOfItemQuery = "SELECT price\n" +
        "FROM items\n" +
        "WHERE title=?";
    
    private static final String getShipmentTypesQuery = "SELECT *\n" +
        "FROM shop_shipment_types\n" +
        "WHERE shop_id=?";
    
    private DatabaseHelper() {
        //Not istantiable
    }
  
    /**
     * Loads the driver and gets connection with amazondatabase and shopdatabase
     * 
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static void open() throws SQLException, ClassNotFoundException {	
        Class.forName("com.mysql.jdbc.Driver");
        //connection = DriverManager.getConnection("jdbc:mysql://185.25.204.238:3306/shop_database?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "6x742+244");
        shopDatabaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_database?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "");
        amazonDatabaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazon?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "root", "");
    }
    
    /**
     * Close connections
     * 
     * @throws SQLException 
     */
    public static void close() throws SQLException {
		shopDatabaseConnection.close();
                amazonDatabaseConnection.close();
    }
    
    /**
     * Parses a date string in a date with a given format
     * 
     * @param date date string to parse
     * @return a date with this format yyyy-MM-dd HH:mm:ss
     */
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch(ParseException e) {
            return null;
        }
    }
    
    /**
     * Generates a date between date ranges 
     * 
     * @param start lower date limit
     * @param end upper date limit
     * @return a date between start and end date
     */
    public static Date generateDate(Date start, Date end) {
        Date randomDate = new Date(ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime()));
        return randomDate;
    }
    
    /**
     * Generates an Italian vat number
     * 
     * @return a random vat number
     */
    public static String getVatNumber() {
        DecimalFormat df11 = new DecimalFormat("00000000000"); // 11 zeros
        Random rand = new Random();
        double vatNo = rand.nextInt(9) * Math.pow(10, 10) + rand.nextInt(9) * Math.pow(10, 9) + rand.nextInt(9) * Math.pow(10, 8) + rand.nextInt(9) * Math.pow(10, 7) + rand.nextInt(9) * Math.pow(10, 6) + rand.nextInt(9) * Math.pow(10, 5) +rand.nextInt(9) * Math.pow(10, 4) + rand.nextInt(9) * Math.pow(10, 3) + rand.nextInt(9) * Math.pow(10, 2) + rand.nextInt(9) * Math.pow(10, 1) + rand.nextInt(9) * Math.pow(10, 0);
        String vatNumber = df11.format(vatNo);
        
        return vatNumber;
    }
    
    /**
     * Gets all shops addresses from amazon database
     * 
     * @return a list of addresses
     * @throws SQLException 
     */
    public static LinkedList<AddressEntity> getAllShopsAddresses() throws SQLException { 
        LinkedList<AddressEntity> addressList = new LinkedList<>();
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getShopAddressesQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                AddressEntity address = new AddressEntity(results.getInt("address_id"), results.getString("street"), results.getString("streetnumber"), results.getString("city"), results.getString("district"), results.getString("zip"), results.getString("country"), results.getBigDecimal("latitude"), results.getBigDecimal("longitude"));
                addressList.add(address);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return addressList;    
    }
    
    /**
     * Gets all users addresses from amazon database
     * 
     * @return a list of addresses
     * @throws SQLException 
     */
    public static LinkedList<AddressEntity> getAllUsersAddresses() throws SQLException { 
        LinkedList<AddressEntity> addressList = new LinkedList<>();
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getUserAddressesQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                AddressEntity address = new AddressEntity(results.getInt("address_id"), results.getString("street"), results.getString("streetnumber"), results.getString("city"), results.getString("district"), results.getString("zip"), results.getString("country"), results.getBigDecimal("latitude"), results.getBigDecimal("longitude"));
                addressList.add(address);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return addressList;    
    }   
    
    /**
     * Gets all administrators addresses from amazon database
     * 
     * @return a list of addresses
     * @throws SQLException 
     */
    public static LinkedList<AddressEntity> getAllAdministratorsAddresses() throws SQLException { 
        LinkedList<AddressEntity> addressList = new LinkedList<>();
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getAdministratorAddressesQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                AddressEntity address = new AddressEntity(results.getInt("address_id"), results.getString("street"), results.getString("streetnumber"), results.getString("city"), results.getString("district"), results.getString("zip"), results.getString("country"), results.getBigDecimal("latitude"), results.getBigDecimal("longitude"));
                addressList.add(address);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return addressList;    
    } 
    
    /**
     * Gets all items from amazon database
     * 
     * @return a list of items
     * @throws SQLException 
     */
    public static LinkedList<ItemEntity> getAllItems() throws SQLException {   
        LinkedList<ItemEntity> itemList = new LinkedList<>();
        Date startDate;
        Date endDate;
        Date insertDate;
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getItemQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                startDate = parseDate("2017-01-01 00:00:00");
                endDate = new Date();
                insertDate = generateDate(startDate, endDate);
                ItemEntity item = new ItemEntity(results.getInt("item_id"), results.getString("title"), results.getString("description"), insertDate, Department.getById(results.getInt("category")));
                itemList.add(item);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return itemList;    
    }
    
    /**
     * Gets all shops from amazon database
     * 
     * @return a list of shops
     * @throws SQLException 
     */
    public static LinkedList<ShopEntity> getAllShops() throws SQLException {   
        LinkedList<ShopEntity> shopList = new LinkedList<>();
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getShopQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                ShopEntity shop = new ShopEntity(results.getInt("shop_id"), results.getString("description"), results.getString("vat_number"), results.getString("phone_number"), new Date(results.getTimestamp("upgrade_date").getTime()));
                shopList.add(shop);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return shopList;    
    }
    
    /**
     * Gets all shops names from amazon database
     * 
     * @return a list of shops names
     * @throws SQLException 
     */
    public static LinkedList<String> getAllShopNames() throws SQLException { 
        LinkedList<String> shopNameList = new LinkedList<>();
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getShopNameQuery);
            ResultSet results;
            results = getElementStatement.executeQuery();
            while(results.next()) {
                String shopName = results.getString("shopname");
                shopNameList.add(shopName);
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return shopNameList;    
    }
    
    /**
     * Gets a shop from amazon database by it's name
     * 
     * @param shopName name of shop to obtain
     * @param shopId id of shop to insert in shop database connection
     * @return a shop identified by it's name
     * @throws SQLException 
     */
    public static ShopEntity getShopByName(String shopName, int shopId) throws SQLException {
        ShopEntity shop = null;
        
        try {
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getShopByNameQuery);
            getElementStatement.setString(1, shopName);
            ResultSet results;
            results = getElementStatement.executeQuery();
            if(results.next()) {
                shop = new ShopEntity(shopId, results.getString("description"), getVatNumber(), results.getString("phone_number"), results.getTimestamp("upgrade_date"));
            }
        } catch(SQLException e) {
                System.out.println(e);
                e.printStackTrace();
        }
        
        return shop;
    }
    
    /**
     * Inserts an address in shop database
     * 
     * @param address address to insert
     * @param addressDao dao that allows to insert the address in shop database
     * @return the id with which the address has been inserted in shop database
     * @throws SQLException 
     */
    public static int insertAddress(AddressEntity address, JdbcAddressDao addressDao) throws SQLException {
        int addressId = -1;  
        
        try {              
           addressId = addressDao.insert(address, shopDatabaseConnection);              
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }     
        
        return addressId;
    }
    
    /**
     * Inserts a shop in shop database
     * 
     * @param shop shop to insert
     * @param shopDao dao that allows to insert the shop in shop database
     * @throws SQLException 
     */
    public static void insertShop(ShopEntity shop, JdbcShopDao shopDao) throws SQLException {       
        try {                   
           shopDao.insert(shop, shopDatabaseConnection);
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }     
    }
    
    /**
     * Inserts an administrator in shop database
     * 
     * @param administrator administator to insert
     * @param administratorDao dao that allows to insert administrator in shop database
     * @throws SQLException 
     */
    public static void insertAdministrator(AdministratorEntity administrator, JdbcAdministratorDao administratorDao) throws SQLException {
        try {                   
           administratorDao.insert(administrator, shopDatabaseConnection);
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        } 
    }
    
    /**
     * Inserts a shop image in shop database
     * 
     * @param resource resource to insert
     * @param shopId id of the shop to which insert the image
     * @param resourceDao dao that allows to insert resource in shop database
     * @throws SQLException 
     */
    public static void insertShopImage(ResourceEntity resource, int shopId, JdbcResourceDao resourceDao) throws SQLException {
        try {                   
           resourceDao.insertShopImage(resource, shopId, shopDatabaseConnection);
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        } 
    }
    
    /**
     * Inserts an item image in shop database
     * 
     * @param resource resource to insert
     * @param itemId id of the shop to which insert the image
     * @param resourceDao dao that allows to insert resource in shop database
     * @throws SQLException 
     */
    public static void insertItemImage(ResourceEntity resource, int itemId, JdbcResourceDao resourceDao) throws SQLException {
        try {                   
           resourceDao.insertItemImage(resource, itemId, shopDatabaseConnection);
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        } 
    }
    
    /**
     * Inserts a shop review in shop database
     * 
     * @param shopReview shop reviews to insert
     * @param shopReviewDao dao that allows to insert shop review in shop database
     * @throws SQLException 
     */
    public static void insertShopReview(ShopReviewEntity shopReview, JdbcShopReviewDao shopReviewDao) throws SQLException {
        try {                   
           shopReviewDao.insert(shopReview, shopDatabaseConnection);
           
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        } 
    }
    
    /**
     * Gets a title for a review based on the rating
     * 
     * @param rating rating on which the title is based on
     * @return a title for a shop review
     */
    public static String getTitle(int rating) {
        Random rand = new Random();
        
        if(rating <= 2) {
            ArrayList<String> worstTitles = new ArrayList<>();
            worstTitles.add("Mai più");
            worstTitles.add("Sconsigliato");
            worstTitles.add("Pessima esperienza");
            worstTitles.add("Inaffidabile");
            worstTitles.add("Non acquistate da lui");
            
            return worstTitles.get(rand.nextInt(worstTitles.size()));
            
        } else if(rating <= 5) {
            ArrayList<String> badTitles = new ArrayList<>();
            badTitles.add("Ultima volta");
            badTitles.add("Se possibile evitate");
            badTitles.add("Non ci siamo");
            
            return badTitles.get(rand.nextInt(badTitles.size()));
            
        } else if(rating <= 7) {
            ArrayList<String> okTitles = new ArrayList<>();
            okTitles.add("Venditore OK");
            okTitles.add("Consigliato");
            okTitles.add("Esperienza buona");
            
            return okTitles.get(rand.nextInt(okTitles.size()));
            
        } else {
            ArrayList<String> excellentTitles = new ArrayList<>();
            excellentTitles.add("Ottimo");
            excellentTitles.add("Affidabilissimo");
            excellentTitles.add("Eccellente");
            excellentTitles.add("Consigliatissimo");
            excellentTitles.add("Venditore 10+");
            
            return excellentTitles.get(rand.nextInt(excellentTitles.size()));
        }    
    }
    
    /**
     * Inserts a shop order item in shop database
     * 
     * @param shopOrderItem shop order item to insert
     * @param shopOrderItemDao dao that allows to insert a shop order item
     * @return the id with which the shop order item has been inserted in shop database
     * @throws SQLException 
     */
    public static int insertShopOrderItem(ShopOrderItemEntity shopOrderItem, JdbcShopOrderItemDao shopOrderItemDao) throws SQLException {
        int shopOrderItemId = -1;
        try {   
           shopOrderItemId = shopOrderItemDao.insert(shopOrderItem, shopDatabaseConnection);
           return shopOrderItemId;
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        } 
        return shopOrderItemId;
    }
    
    /**
     * Gets all shipment types
     * 
     * @return a list of shipment types
     * @throws SQLException 
     */
    public static ArrayList<ShipmentType> getAllShipmentTypes() throws SQLException {
        ArrayList<ShipmentType> shipmentTypes = new ArrayList<>();
        ShipmentType shipmentType;
        for(int i = 1; i <= ShipmentType.getNumberOfShipments(); i++) {
            shipmentType = ShipmentType.getById(i);
            shipmentTypes.add(shipmentType);
        }
        return shipmentTypes;
    }
    
    /**
     * Inserts randomly shipment types for a given shop 
     * 
     * @param shopId id of the shop to which insert shipment types
     * @param shopShipmentTypeDao dao that allows to insert shipment type of a shop
     * @throws SQLException 
     */
    public static void insertShopShipmentType(Integer shopId, JdbcShopShipmentTypeDao shopShipmentTypeDao ) throws SQLException {
        ArrayList<ShipmentType> shipmentTypes = getAllShipmentTypes();
        int shopShipmentTypeInserted = 0;
        ShipmentType shipmentType;
        ShopShipmentTypeEntity shopShipmentType;
        Random rand = new Random();
        
        try {  
            
            for(int i = 0; i < ShipmentType.getNumberOfShipments(); i++) {
                if(rand.nextBoolean()) {
                    BigDecimal shipmentPrice;
                    shipmentType = shipmentTypes.get(i);
                    //If shipment type is shop pick up then the shipment price is zero
                    if(shipmentType == ShipmentType.SHOP_PICK_UP) {
                        shipmentPrice = BigDecimal.ZERO;
                    } else {
                        shipmentPrice = BigDecimal.valueOf(rand.nextInt(11));
                    }
                    shopShipmentType = new ShopShipmentTypeEntity(shopId, shipmentType, shipmentPrice);
                    shopShipmentTypeDao.insert(shopShipmentType, shopDatabaseConnection);
                    shopShipmentTypeInserted++;
                }                
            }
            
            //If no shipment types have been inserted then inserts one randomly
            if(shopShipmentTypeInserted == 0) {
                shipmentType = shipmentTypes.get(rand.nextInt(ShipmentType.getNumberOfShipments()));
                shopShipmentType = new ShopShipmentTypeEntity(shopId, shipmentType, BigDecimal.valueOf(rand.nextInt(10)));
                shopShipmentTypeDao.insert(shopShipmentType, shopDatabaseConnection);
                shopShipmentTypeInserted++;
            }
            
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }         
    }
    
    /**
     * Inserts a shop item in shop database
     * 
     * @param shopItem shop item to insert
     * @param shopItemDao dao that allows to insert a shop item
     * @return the id with which the shop item has been inserted in shop database
     * @throws SQLException 
     */
    public static int insertShopItem(ShopItemEntity shopItem, JdbcShopItemDao shopItemDao) throws SQLException {
        int shopItemId = -1;
        try {
            shopItemId = shopItemDao.insert(shopItem, shopDatabaseConnection);
            return shopItemId;
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }      
        return shopItemId;
    }
    
    /**
     * Inserts a cart entry in shop database
     * 
     * @param cartEntry cart entry to insert
     * @param cartEntryDao dao that allows to insert a cart entry in shop database
     * @throws SQLException 
     */
    public static void insertShopCartEntry(CartEntryEntity cartEntry, JdbcCartEntryDao cartEntryDao) throws SQLException {
        try { 
            cartEntryDao.insert(cartEntry, shopDatabaseConnection);
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    /**
     * Inserts a notification in shop database
     * 
     * @param notification notification to insert
     * @throws SQLException 
     */
    public static void insertNotification(NotificationEntity notification) throws SQLException {
        JdbcNotificationDao notificationDao;
        try {
            notificationDao = JdbcDaoFactory.getNotificationDao();
            notificationDao.insert(notification, shopDatabaseConnection);
        } catch(DaoFactoryException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    /**
     * Inserts shop order status in shop database
     * 
     * @param shopOrder shop order of which to insert the order status
     * @param shopOrderStatusDao dao that allows to insert a shop order status in shop database
     * @param shopOrderId id with which the given shop has been inserted in shop database
     * @throws SQLException 
     */
    public static void insertShopOrderStatus(ShopOrderEntity shopOrder, JdbcShopOrderStatusDao shopOrderStatusDao, Integer shopOrderId) throws SQLException {
        ShopOrderStatusEntity shopOrderStatus;
        int shippingLimit = 5;
        int deliveryLimit = 12;
        Date todayDate = new Date();  
        DateTime todayDateTime = new DateTime(todayDate);
        Date orderDate = shopOrder.getDate();
        Date shipmentDate, deliveryDate;
        DateTime shippingLimitDateTime = todayDateTime.minusDays(shippingLimit);
        DateTime deliveryLimitDateTime = todayDateTime.minusDays(deliveryLimit);
        Date shippingLimitDate = shippingLimitDateTime.toDate();
        Date deliveryLimitDate = deliveryLimitDateTime.toDate();
        //LocalDate shippingLimitLocalDate = LocalDate.parse().minusDays(shippingLimit);
        //LocalDate deliveryLimitLocalDate = LocalDate.parse(todayDate.toString()).minusDays(deliveryLimit);
        //Date shippingLimitDate = shippingLimitLocalDate.toDateTimeAtStartOfDay().toDate();
        //Date deliveryLimitDate = deliveryLimitLocalDate.toDateTimeAtStartOfDay().toDate();
        ShopOrderStatusNotification shopOrderStatusNotification;
        
        try {
            if(orderDate.before(deliveryLimitDate)) { 
                //Adding shipment of order
                shipmentDate = generateDate(orderDate, deliveryLimitDate);
                shopOrderStatus = new ShopOrderStatusEntity(shopOrderId, ShopOrderStatusType.SENT, shipmentDate);
                shopOrderStatusDao.insert(shopOrderStatus, shopDatabaseConnection);
                shopOrderStatusNotification = new ShopOrderStatusNotification(null, shipmentDate, false, shopOrder.getUserId(), shopOrderId);
                insertNotification(shopOrderStatusNotification);
                //Adding delivery of order
                deliveryDate = generateDate(shipmentDate, todayDate); 
                shopOrderStatus = new ShopOrderStatusEntity(shopOrderId, ShopOrderStatusType.DELIVERED, deliveryDate);
                shopOrderStatusDao.insert(shopOrderStatus, shopDatabaseConnection);
                shopOrderStatusNotification = new ShopOrderStatusNotification(null, deliveryDate, false, shopOrder.getUserId(), shopOrderId);
                insertNotification(shopOrderStatusNotification);
            } else if(orderDate.before(shippingLimitDate)) {
                //Adding shipment of order
                shipmentDate = generateDate(orderDate, todayDate);
                shopOrderStatus = new ShopOrderStatusEntity(shopOrderId, ShopOrderStatusType.SENT, shipmentDate);
                shopOrderStatusDao.insert(shopOrderStatus, shopDatabaseConnection);
                shopOrderStatusNotification = new ShopOrderStatusNotification(0, shipmentDate, false, shopOrder.getUserId(), shopOrderId);
                insertNotification(shopOrderStatusNotification);
            } else {
                //DONOTHING
            }
            
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    /**
     * Gets descriptions of issues
     * 
     * @return a list of issues descriptions
     */
    public static ArrayList<String> getIssueDescriptions() {
        ArrayList<String> issueDescriptions = new ArrayList<>();
        File issueDescriptionsFile = new File("C:\\Users\\luca\\Documents\\NetBeansProjects\\WebProgrammingProject\\WebApp\\IssueDescriptions.txt");
                
        try {           
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(issueDescriptionsFile)));
            String line;
            while((line = br.readLine()) != null) {               
                
                //Ignore comment line
                if(line.trim().startsWith("//") || line.isEmpty()) {
                        continue;
                }
                
                issueDescriptions.add(line);
                
            }
            
            br.close();
        } catch(IOException e) {
            System.out.println("An error occurred while processing input file line: " + e.toString());
            e.printStackTrace();
        } 
        
        return issueDescriptions;
    }
    
    /**
     * Gets motivations with which solve an issue based on the issue result
     * 
     * @param issueResultIdToFind id of issue result with which the issue has been solved
     * @return a list of motivations for the given issue result
     */
    public static ArrayList<String> getIssueMotivations(int issueResultIdToFind) {
        ArrayList<String> issueMotivations = new ArrayList<>();
        File issueMotivationsFile = new File("C:\\Users\\luca\\Documents\\NetBeansProjects\\WebProgrammingProject\\WebApp\\IssueMotivations.txt");
        
        try {           
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(issueMotivationsFile)));
            String line;
            while((line = br.readLine()) != null) {
                
                //Ignore comment line
                if(line.trim().startsWith("//") || line.isEmpty()) {
                        continue;
                }
                
                String[] tokens = line.split(",");
                String motivation = tokens[0].trim();
                int issueResultId = Integer.parseInt(tokens[1].trim());
                if(issueResultId == issueResultIdToFind) {
                    issueMotivations.add(motivation);
                }   
            }
            
            br.close();
        } catch(IOException e) {
            System.out.println("An error occurred while processing input file line: " + e.toString());
            e.printStackTrace();
        } 
        
        return issueMotivations;
    }
    
    /**
     * Inserts an issue in shop database
     * 
     * @param shopOrder shop order that includes the shop order item that has caused the issue
     * @param issueDao dao that allows to insert an issue in shop database
     * @param shopOrderItemId id of the shop order item that has caused the issue
     * @throws SQLException 
     */
    public static void insertIssue(ShopOrderEntity shopOrder, JdbcIssueDao issueDao, Integer shopOrderItemId) throws SQLException {
        IssueEntity issue = null;
        ArrayList<String> issueDescriptions = new ArrayList<>();
        ArrayList<String> issueMotivations = new ArrayList<>();
        ShopIssueOpenedNotificationEntity shopIssueOpenedNotification;
        ShopIssueClosedNotificationEntity shopIssueClosedNotification;
        UserIssueClosedNotificationEntity userIssueClosedNotification;
        AdminIssueOpenedNotificationEntity adminIssueOpenedNotification;
        int issueId;
        
        Random rand = new Random();
        
        try {
            //I'll put a issue with a probability of 5% for each ShopOrderItem
             
            int closureLimit = 10;
            Date todayDate = new Date();
            DateTime todayDateTime = new DateTime(todayDate);
            DateTime closureLimitDateTime = todayDateTime.minusDays(closureLimit);
            //LocalDate closureLimitLocalDate = LocalDate.parse(todayDate.toString()).minusDays(closureLimit);        
            Date closureLimitDate = closureLimitDateTime.toDate();
            issueDescriptions = getIssueDescriptions();
            String description = issueDescriptions.get(rand.nextInt(issueDescriptions.size()));
            Date openDate = generateDate(shopOrder.getDate(), new Date());  
            
            issue = new IssueEntity(null, description, openDate, null, null, null, shopOrderItemId);
            //Open issue
            issueId = issueDao.insert(issue, shopDatabaseConnection);
            shopIssueOpenedNotification = new ShopIssueOpenedNotificationEntity(null, openDate, false, shopOrder.getShopId(), issueId);
            insertNotification(shopIssueOpenedNotification);
            adminIssueOpenedNotification = new AdminIssueOpenedNotificationEntity(null, openDate, false, issueId);
            insertNotification(adminIssueOpenedNotification);
            
            if(openDate.before(closureLimitDate)) {
                //Close issue
                IssueResult issueResult = IssueResult.getById(rand.nextInt(IssueResult.getNumberOfIssueResults()) + 1);
                issueMotivations = getIssueMotivations(issueResult.getId());
                String motivation = issueMotivations.get(rand.nextInt(issueMotivations.size()));
                Date closeDate = generateDate(openDate, todayDate);
                issue = new IssueEntity(issueId, description, openDate, issueResult, motivation, closeDate, shopOrderItemId);    
                issueDao.update(issue, shopDatabaseConnection);
                shopIssueClosedNotification = new ShopIssueClosedNotificationEntity(null, closeDate, false, shopOrder.getShopId(), issueId);
                insertNotification(shopIssueClosedNotification);
                userIssueClosedNotification = new UserIssueClosedNotificationEntity(null, closeDate, false, shopOrder.getUserId(), issueId);       
                insertNotification(userIssueClosedNotification);
            } 

        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }     
    }     
    
    /**
     * Inserts users, shops, administrators and corresponding addresses in shop database
     * 
     * @throws SQLException 
     */
    public static void insertUsers() throws SQLException {
        UserEntity user;
        JdbcUserDao userDao;
        int userId;
        LinkedList<AddressEntity> usersAddresses, shopsAddresses, administratorAddresses;
        AddressEntity address;
        JdbcAddressDao addressDao;  
        int addressId;     
        LinkedList<String> shopNames;
        ShopEntity shop;
        JdbcShopDao shopDao;
        String firstName, lastName, userName, email;
        HashDigest passwordHash;
        Date registrationDate, startDate, endDate;
        SecurityCode registrationCode, passwordResetCode;
        boolean activated;
        ResourceEntity resource;
        JdbcResourceDao resourceDao;
        AdministratorEntity administrator;
        JdbcAdministratorDao administratorDao;
        JdbcShopShipmentTypeDao shopShipmentTypeDao;
        ArrayList<String> administratorNames = new ArrayList<>();
        ArrayList<String> administratorLastNames = new ArrayList<>();
        Random rand;
        DataFactory df = new DataFactory();
        
        usersAddresses = getAllUsersAddresses();
        shopsAddresses = getAllShopsAddresses();
        administratorAddresses = getAllAdministratorsAddresses();
        shopNames = getAllShopNames();
        administratorNames.add("Willi");
        administratorNames.add("Daniele");
        administratorNames.add("Luca");
        administratorNames.add("Alessio");
        administratorLastNames.add("Menapace");
        administratorLastNames.add("Giuliani");
        administratorLastNames.add("Zanella");
        administratorLastNames.add("Paternoster");
        rand = new Random();        
        
        try {
            userDao = JdbcDaoFactory.getUserDao();
            addressDao = JdbcDaoFactory.getAddressDao();
            shopDao = JdbcDaoFactory.getShopDao();
            administratorDao = JdbcDaoFactory.getAdministratorDao();
            resourceDao = JdbcDaoFactory.getResourceDao();
            shopShipmentTypeDao = JdbcDaoFactory.getShopShipmentTypeDao();
            
            
            //Adding administrators
            for(int i = 0; i < 4; i++) {
                if(!administratorAddresses.isEmpty()) {
                    firstName = administratorNames.get(i);
                    lastName = administratorLastNames.get(i);
                    address = administratorAddresses.removeFirst();
                    //Adding address
                    addressId = insertAddress(address, addressDao);
                    DecimalFormat df4 = new DecimalFormat("0000");
                    userName = firstName.concat(String.valueOf(lastName.charAt(rand.nextInt(lastName.length())))).concat(df4.format(rand.nextInt(9999)));
                    passwordHash = new HashDigest(userName);
                    email = firstName.toLowerCase().concat(lastName.toLowerCase().concat(df4.format(rand.nextInt(9999))).concat("@gmail.com"));
                    registrationDate = parseDate("2017-01-01 00:00:00");
                    registrationCode = new SecurityCode();
                    activated = true;
                    passwordResetCode = new SecurityCode();
                    user = new UserEntity(0, userName, passwordHash, firstName, lastName, addressId, email, registrationDate, registrationCode, activated, passwordResetCode);
                    userId = userDao.insert(user, shopDatabaseConnection);
                    administrator = new AdministratorEntity(userId, registrationDate);
                    insertAdministrator(administrator, administratorDao);
                }
                
            }
            
            //Adding shops
            for(String shopName : shopNames) {
                if(!shopsAddresses.isEmpty()) {
                    address = shopsAddresses.removeFirst();
                    //Adding address
                    addressId = insertAddress(address, addressDao);
                    firstName = df.getFirstName();
                    lastName = df.getLastName();
                    userName = shopName;
                    passwordHash = new HashDigest(userName);
                    email = userName.replace(" ", "_").toLowerCase().concat("@gmail.com");
                    startDate = parseDate("2017-01-01 00:00:00");
                    endDate = new Date(); 
                    registrationDate = generateDate(startDate, endDate);
                    registrationCode = new SecurityCode();
                    activated = true;
                    passwordResetCode = new SecurityCode();
                    user = new UserEntity(0, userName, passwordHash, firstName, lastName, addressId, email, registrationDate, registrationCode, activated, passwordResetCode);
                    userId = userDao.insert(user, shopDatabaseConnection);
                    shop = getShopByName(userName, userId);
                    insertShop(shop, shopDao);                   
                    insertShopShipmentType(userId, shopShipmentTypeDao);

                    PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getResourceByShopNameQuery);
                    getElementStatement.setString(1, shopName);
                    ResultSet results;
                    results = getElementStatement.executeQuery();
                    while(results.next()) {
                        resource = new ResourceEntity(results.getInt("picture_id"), results.getString("filename"), shopName);
                        insertShopImage(resource, userId, resourceDao);
                    } 
                }
                
            }
            
            //Adding users
            while(!usersAddresses.isEmpty()) {
                firstName = df.getFirstName();
                lastName = df.getLastName();
                address = usersAddresses.removeFirst();
                //Adding address
                addressId = insertAddress(address, addressDao);
                DecimalFormat df4 = new DecimalFormat("0000");
                userName = firstName.concat(String.valueOf(lastName.charAt(rand.nextInt(lastName.length())))).concat(df4.format(rand.nextInt(9999)));
                passwordHash = new HashDigest(userName);
                email = firstName.toLowerCase().concat(lastName.toLowerCase().concat(df4.format(rand.nextInt(9999))).concat("@gmail.com"));
                startDate = parseDate("2017-01-01 00:00:00");
                endDate = new Date(); 
                registrationDate = generateDate(startDate, endDate);
                registrationCode = new SecurityCode();
                activated = true;
                passwordResetCode = new SecurityCode();
                user = new UserEntity(0, userName, passwordHash, firstName, lastName, addressId, email, registrationDate, registrationCode, activated, passwordResetCode);
                userDao.insert(user, shopDatabaseConnection);        
            }
     
        } catch(DaoFactoryException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
    
    /**
     * Inserts items and corresponding elements in shop database
     * 
     * @throws SQLException 
     */
    public static void insertItems() throws SQLException {
        LinkedList<ItemEntity> items;
        JdbcItemDao itemDao;
        int itemId, shopItemId;
        BigDecimal itemPrice = null;
        int minIndexOfShops = 0;
        int numberOfShops = 0;
        int numberOfUsers = 0;
        ResourceEntity resource;
        JdbcResourceDao resourceDao;
        JdbcShopItemDao shopItemDao;
        JdbcShopDao shopDao;
        JdbcCartEntryDao cartEntryDao;
        ShopEntity shop;
        ShopItemEntity shopItem;
        CartEntryEntity cartEntry;
        Random rand = new Random();
        
        items = getAllItems();
        
        try {
            itemDao = JdbcDaoFactory.getItemDao();
            resourceDao = JdbcDaoFactory.getResourceDao();
            shopItemDao = JdbcDaoFactory.getShopItemDao();
            shopDao = JdbcDaoFactory.getShopDao();
            cartEntryDao = JdbcDaoFactory.getCartEntryEntityDao();
            
            PreparedStatement getElementStatement = amazonDatabaseConnection.prepareStatement(getResourceByItemTitleQuery);
            PreparedStatement getMinIndexOfShopsStatement = shopDatabaseConnection.prepareStatement(getMinIndexOfShopsQuery);
            PreparedStatement getNumberOfShopsStatement = shopDatabaseConnection.prepareStatement(getNumberOfShopsQuery);
            PreparedStatement getPriceOfItemStatement = amazonDatabaseConnection.prepareStatement(getPriceOfItemQuery);
            PreparedStatement getNumberOfUsersStatement = shopDatabaseConnection.prepareStatement(getNumberOfUsersQuery);
            ResultSet results;
            
            for(ItemEntity item : items) {
                
                //Inserting item
                itemId = itemDao.insert(item, shopDatabaseConnection);               
                getElementStatement.setString(1, item.getTitle());
                results = getElementStatement.executeQuery();
                while(results.next()) {
                    resource = new ResourceEntity(results.getInt("picture_id"), results.getString("filename").concat(".jpg"), item.getTitle());
                    insertItemImage(resource, itemId, resourceDao);
                }
                
                results = getMinIndexOfShopsStatement.executeQuery();
                if(results.next()) {
                    minIndexOfShops = results.getInt(1);
                }   
                
                results = getNumberOfShopsStatement.executeQuery();
                if(results.next()) {
                    numberOfShops = results.getInt(1);
                }
                
                getPriceOfItemStatement.setString(1, item.getTitle());
                results = getPriceOfItemStatement.executeQuery();
                if(results.next()) {
                    itemPrice = results.getBigDecimal(1);
                }
                
                results = getNumberOfUsersStatement.executeQuery();
                if(results.next()) {
                    numberOfUsers = results.getInt(1);
                }
                
                //Inserting shop item and cart entry
                for(int i = 1; i <= (rand.nextInt(4) + 4); i++) {
                    shop = shopDao.getById((rand.nextInt(numberOfShops - minIndexOfShops + 1) + minIndexOfShops), shopDatabaseConnection);
                    //Add to item price a value that range from 0% to 20% of item price itself
                    BigDecimal increase = itemPrice.multiply(BigDecimal.valueOf(rand.nextInt(21))).divide(BigDecimal.valueOf(100));
                    BigDecimal itemPriceIncreased = itemPrice.add(increase);
                    shopItem = new ShopItemEntity(null, shop.getShopId(), itemId, itemPriceIncreased, rand.nextInt(100));
                    //Inserting shop item
                    shopItemId = insertShopItem(shopItem, shopItemDao);
                    for(int j = 1; j <= (rand.nextInt(3) + 1); j++) {
                        if(rand.nextBoolean()) {
                            cartEntry = new CartEntryEntity(null, rand.nextInt(numberOfUsers)+1, shopItemId, rand.nextInt(4)+1);
                            //Inserting cart entry
                            insertShopCartEntry(cartEntry, cartEntryDao);
                        }      
                    } 
                }               
            }
                                
        } catch(DaoFactoryException e) {
                System.out.println(e);
                e.printStackTrace();
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
     
    }
    
    /**
     * Insert shop orders and corresponding elements in shop database
     * 
     * @throws SQLException 
     */
    public static void insertShopOrders() throws SQLException {
        JdbcUserDao userDao;
        JdbcShopDao shopDao;
        JdbcItemDao itemDao; 
        JdbcShopOrderDao shopOrderDao;
        JdbcShopReviewDao shopReviewDao;
        JdbcShopOrderItemDao shopOrderItemDao;
        JdbcItemReviewDao itemReviewDao;
        JdbcShopOrderStatusDao shopOrderStatusDao;
        JdbcIssueDao issueDao;
        ShopOrderEntity shopOrder;
        ShopOrderItemEntity shopOrderItem;
        ShopOrderAddedNotification shopOrderAddedNotification;
        ShopReviewNotification shopReviewNotification;
        ItemEntity item;
        int shopOrderId;
        int shopOrderItemId;
        int itemId;
        int shipmentTypeId = -1;
        int shopReviewId;
        String itemTitle;
        BigDecimal itemPrice;
        BigDecimal shipmentTypePrice = BigDecimal.valueOf(-1);
        UserEntity user;
        ShopEntity shop;
        Date startDate, endDate, orderDate, shopReviewDate, itemReviewDate;
        PreparedStatement getNumberOfUsersStatement = shopDatabaseConnection.prepareStatement(getNumberOfUsersQuery);  
        PreparedStatement getNumberOfItemsStatement = shopDatabaseConnection.prepareStatement(getNumberOfItemsQuery);
        PreparedStatement getMinIndexOfShopsStatement = shopDatabaseConnection.prepareStatement(getMinIndexOfShopsQuery);
        PreparedStatement getNumberOfShopsStatement = shopDatabaseConnection.prepareStatement(getNumberOfShopsQuery);
        PreparedStatement getShopReviewsStatement = amazonDatabaseConnection.prepareStatement(getReviewsByShopNameQuery);
        PreparedStatement removeShopReviewStatement = amazonDatabaseConnection.prepareStatement(removeShopReviewByIdQuery);
        PreparedStatement getItemReviewsStatement = amazonDatabaseConnection.prepareStatement(getReviewsByItemTitleQuery);
        PreparedStatement removeItemReviewStatement = amazonDatabaseConnection.prepareStatement(removeItemReviewByIdQuery);
        PreparedStatement getPriceOfItemStatement = amazonDatabaseConnection.prepareStatement(getPriceOfItemQuery);
        PreparedStatement getShipmentTypesByShopId = shopDatabaseConnection.prepareStatement(getShipmentTypesQuery);
        int numberOfUsers = 0;
        int numberOfItems = 0;
        int minIndexOfShops = 0;
        int numberOfShops = 0;
        int includedItems;
        ShopReviewEntity shopReview;
        ItemReviewEntity itemReview;
        ResultSet results;
        Random rand = new Random();
        
        try {
            userDao = JdbcDaoFactory.getUserDao();
            shopDao = JdbcDaoFactory.getShopDao();
            itemDao = JdbcDaoFactory.getItemDao();
            shopOrderDao = JdbcDaoFactory.getShopOrderDao();
            shopOrderItemDao = JdbcDaoFactory.getShopOrderItemDao();
            shopReviewDao = JdbcDaoFactory.getShopReviewDao();
            itemReviewDao = JdbcDaoFactory.getItemReviewDao();
            shopOrderStatusDao = JdbcDaoFactory.getShopOrderStatusDao();
            issueDao = JdbcDaoFactory.getIssueDao();
            
            results = getNumberOfUsersStatement.executeQuery();     
            if(results.next()) {
                numberOfUsers = results.getInt(1);
            }
            
            results = getNumberOfItemsStatement.executeQuery();
            if(results.next()) {
                numberOfItems = results.getInt(1);
            }
            
            results = getMinIndexOfShopsStatement.executeQuery();
            if(results.next()) {
                minIndexOfShops = results.getInt(1);
            }
            
            results = getNumberOfShopsStatement.executeQuery();
            if(results.next()) {
                numberOfShops = results.getInt(1);
            }
            
            for(int i = 1; i <= 4000; i++) {
                
                user = userDao.getById((rand.nextInt(numberOfUsers)+1), shopDatabaseConnection);
                shop = shopDao.getById((rand.nextInt(numberOfShops - minIndexOfShops + 1) + minIndexOfShops), shopDatabaseConnection);
                startDate = user.getRegistrationDate();
                endDate = new Date();
                orderDate = generateDate(startDate, endDate);
                getShipmentTypesByShopId.setInt(1, shop.getShopId());
                results = getShipmentTypesByShopId.executeQuery();
                
                if(results.next()) {                   
                    shipmentTypeId = results.getInt("shipment_type_id");
                    shipmentTypePrice = results.getBigDecimal("price");
                }
                
                //Ensure that the query works well
                assert(shipmentTypeId != -1);
                assert(shipmentTypePrice != BigDecimal.valueOf(-1));
                shopOrder = new ShopOrderEntity(i, orderDate, user.getUserId(), shop.getShopId(), user.getAddressId(), ShipmentType.getById(shipmentTypeId), shipmentTypePrice);               
                shopOrderId = shopOrderDao.insert(shopOrder, shopDatabaseConnection);  
                shopOrderAddedNotification = new ShopOrderAddedNotification(null, orderDate, false, shopOrder.getShopId(), shopOrderId);
                insertNotification(shopOrderAddedNotification);
                
                
                //Inserting shop order status
                insertShopOrderStatus(shopOrder, shopOrderStatusDao, shopOrderId);
                
                //Inserting shop review
                String shopName = userDao.getById(shop.getShopId(), shopDatabaseConnection).getUsername();
                getShopReviewsStatement.setString(1, shopName);
                results = getShopReviewsStatement.executeQuery();
                if(results.next()) {
                    shopReviewDate = generateDate(orderDate, endDate);
                    shopReview = new ShopReviewEntity(results.getInt("review_id"), getTitle(results.getInt("rating")), results.getString("review_text"), new Rating(results.getInt("rating")), shopReviewDate, shopOrderId);
                    shopReviewId = shopReviewDao.insert(shopReview, shopDatabaseConnection);
                    removeShopReviewStatement.setInt(1, shopReview.getShopReviewId());
                    removeShopReviewStatement.executeUpdate();
                    shopReviewNotification = new ShopReviewNotification(null, shopReviewDate, false, shopOrder.getShopId(), shopReviewId);
                    insertNotification(shopReviewNotification);
                }
                
                //Inserting shop order item
                includedItems = rand.nextInt(3) + 4;               
                for(int j = 1; j <= includedItems; j++) {
                    item = itemDao.getById((rand.nextInt(numberOfItems)+1), shopDatabaseConnection);
                    itemId = item.getItemId();
                    itemTitle = item.getTitle();
                    getPriceOfItemStatement.setString(1, itemTitle);
                    results = getPriceOfItemStatement.executeQuery();
                    if(results.next()) {
                        itemPrice = results.getBigDecimal(1);
                        BigDecimal increase = itemPrice.multiply(BigDecimal.valueOf(rand.nextInt(21))).divide(BigDecimal.valueOf(100));
                        BigDecimal itemPriceIncreased = itemPrice.add(increase);
                        shopOrderItem = new ShopOrderItemEntity(0, shopOrderId, itemId, itemPriceIncreased, rand.nextInt(3)+1);
                        shopOrderItemId = insertShopOrderItem(shopOrderItem, shopOrderItemDao);

                        //Inserting item review                      
                        getItemReviewsStatement.setString(1, itemTitle);
                        results = getItemReviewsStatement.executeQuery();
                        if(results.next()) {
                            itemReviewDate = generateDate(orderDate, endDate);
                            itemReview = new ItemReviewEntity(results.getInt("review_id"), results.getString("title"), results.getString("review_text"), new Rating(results.getInt("rating")), itemReviewDate, shopOrderItemId);
                            itemReviewDao.insert(itemReview, shopDatabaseConnection);
                            removeItemReviewStatement.setInt(1, itemReview.getItemReviewId());
                            removeItemReviewStatement.executeUpdate();
                        }
                        
                        //Inserting issue 
                        if(rand.nextInt(99) < 5) {
                            insertIssue(shopOrder, issueDao, shopOrderItemId);
                        }
                    }     
                }
                            
            }
        } catch(DaoFactoryException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch(DaoException e) {
            System.out.println(e);
            e.printStackTrace();
        }
          
    }

}