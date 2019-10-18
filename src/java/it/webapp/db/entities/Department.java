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

public enum Department {
    CARS_AND_MOTORCYCLES(1, "Auto e moto"),
    COMPUTERS(2, "Tecnologia"),
    OFFICE(3, "Ufficio"),
    ELECTRONICS(4, "Elettronica"), 
    FASHION(5, "Moda"),
    AUDIO(7, "Musica"),
    DO_IT_YOUR_SELF(8, "Fai da te"),
    SPORTS(10, "Sport e tempo libero"),
    OUTDOOR(11, "Prodotti per esterno");
    //TODO implement
	
    private final int ID;
    private final String description;

    public static Department getById(int departmentId) {
        Department department = null;
        
        switch(departmentId) {
            case 1:
                department = CARS_AND_MOTORCYCLES;
                break;
            case 2:
                department = COMPUTERS;
                break;
            case 3:
                department = OFFICE;
                break;
            case 4:
                department = ELECTRONICS;
                break;
            case 5:
                department = FASHION;
                break;
            case 7:
                department = AUDIO;
                break;
            case 8:
                department = DO_IT_YOUR_SELF;
                break;
            case 10:
                department = SPORTS;
                break;
            case 11:
                department = OUTDOOR;
                break;     
            default:
                //Ensure we do not miss to update the swtich statement with new Departments
                throw new UnsupportedOperationException("No DepartmentType corresponding to id: " + departmentId);
        }
        
        return department;
              
    }

    Department(int ID, String description) {
        this.ID = ID;
        this.description = description;
    }
        
    public int getID() {     
        return ID;
    }
    
    public String getDescription(){
        return description;
    }
        
}
