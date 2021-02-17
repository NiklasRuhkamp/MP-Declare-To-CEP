package com.cor.cep.event;


public class MachineFailureEvent {

    // ID of the instance
    private int id;

    // Name of the manufacturer
    private String manufacturer;

    // Boolean, if Machine is production critical
    private boolean productionCritial;

    
    public MachineFailureEvent(int id, String manufacturer, boolean productionCritial) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.productionCritial = productionCritial;
    }
    
    // Get the id.
    public int getId  () {
        return id;
    }

    
    // Get the manufacturer.
    public String getManufacturer  () {
        return manufacturer;
    }

    // * Get productionCritical.
    public boolean getProductionCritical  () {
        return productionCritial;
    }
       

    @Override
    public String toString() {
        return "Machine Failure: ID: " + id + ", Manufacturer: " + manufacturer + ", Prdouction critical: " + productionCritial;
    }

}
