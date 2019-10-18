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

public enum ShipmentType {
	SHOP_PICK_UP(1, "Consegna a mano"),
	STANDARD_DELIVERY(2, "Consegna in una settimana"),
	EXPRESS_DELIVERY(3, "Consegna entro 3 giorni");     

	private final int id;
    private final String shipmentDescription;

	private final static int NUMBER_OF_SHIPMENTS = 3;
        
	public static ShipmentType getById(int shipmentTypeId) {
        ShipmentType shipmentType = null;
           
		switch(shipmentTypeId) {
            case 1:
                shipmentType = SHOP_PICK_UP;
                break;
            case 2:
                shipmentType = STANDARD_DELIVERY;
                break;
            case 3:
                shipmentType = EXPRESS_DELIVERY;
                break;   
            default:
                //Ensure we do not miss to update the swtich statement with new ShipmentTypes
                throw new UnsupportedOperationException("No NotificationType corresponding to id: " + shipmentTypeId);
        }
		
        return shipmentType;
        
	}
        
    public static int getNumberOfShipments() {
        return NUMBER_OF_SHIPMENTS;
    }
	
	ShipmentType(int shipmentTypeId, String shipmentDescription) {
		this.id = shipmentTypeId;
        this.shipmentDescription = shipmentDescription;
	}
        
	public int getId() {
		return id;
	}
    
    public String getShipmentDescription(){
        return shipmentDescription;
    }
        
}
