package entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserDevice {

    @Id
    private int iotuser_id;
    
    @Id
    private int device_id;
    
    public UserDevice() {
    	
    }

	public int getIotuser_id() {
		return iotuser_id;
	}

	public void setIotuser_id(int iotuser_id) {
		this.iotuser_id = iotuser_id;
	}

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
    
    

}