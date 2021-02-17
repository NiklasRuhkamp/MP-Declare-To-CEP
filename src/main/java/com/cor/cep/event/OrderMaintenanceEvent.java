package com.cor.cep.event;

/**
 * Immutable Order Maintenence Event class. The process control system creates these events. The
 * OrderMaintenenceHandler picks these up and processes them.
 */
public class OrderMaintenanceEvent {

    /** ID of the instance. */
    private int id;

    /** Name of the Company. */
    private String company;

    /** Boolean, if Company is aivailable. */
    private boolean available;

    /**
     * Single value constructor.
     * 
     * @param value.
     */
    /**
     * Temeratur constructor.
     * 
     * @param id
     * @param company
     * @param available
     */
    public OrderMaintenanceEvent(int id, String company, boolean available) {
        this.id = id;
        this.company = company;
        this.available = available;
    }

    /**
     * Get the id.
     * @return id as Integer
     */
    public int getId() {
        return id;
    }

    /**
     * Get the company.
     * @return company as String
     */
    public String getCompany  () {
        return company;
    }
       
    /**
     * Get available.
     * @return available or not
     */
    public boolean getAvailable() {
        return available;
    }



    @Override
    public String toString() {
        return "Maintenance ordered: ID " + id + ", Company: " + company + ", Available: " + available;
    }

}
