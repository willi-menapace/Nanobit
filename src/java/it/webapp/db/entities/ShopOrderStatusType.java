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

public enum ShopOrderStatusType {
    SENT(1, "Spedito"),
    DELIVERED(2, "Consegnato");

    private final int orderStatusTypeId;
	private final String description;

    public static ShopOrderStatusType getById(int shipmentTypeId) {
            ShopOrderStatusType shopOrderStatusType = null;
        
            switch(shipmentTypeId) {
                case 1:
                    shopOrderStatusType = SENT;
                    break;
                case 2:
                    shopOrderStatusType = DELIVERED;
                    break;
                default:
                    //Ensure we do not miss to update the swtich statement with new ShopOrderStatusTypes
                    throw new UnsupportedOperationException("No NotificationType corresponding to id: " + shipmentTypeId);
            }

            return shopOrderStatusType;
    }

    ShopOrderStatusType(int orderStatusTypeId, String description) {
            this.orderStatusTypeId = orderStatusTypeId;
			this.description = description;
    }

    public int getId() {
            return orderStatusTypeId;
    }
	
	public String getDescription() {
            return description;
    }
}
