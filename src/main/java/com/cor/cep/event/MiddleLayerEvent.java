package com.cor.cep.event;

public class MiddleLayerEvent {

    // ID of the instance
    private int id;

    // Name of the manufacturer
    private String constraint;

    // Boolean, if Machine is production critical
    private String actOrTar;

    // String for value of correlation payload
    private String correlationActivation;
    
    public MiddleLayerEvent(int id, String constraint, String actOrTar, String correlationActivation) {
        this.id = id;
        this.constraint = constraint;
        this.actOrTar = actOrTar;
        this.correlationActivation = correlationActivation;
    }

    // Get the id
    public int getId  () {
        return id;
    }

    // Get productionCritical    
    public String getConstraint  () {
        return constraint;
    }
       
    // Get correlationActivation    
    public String getcorrelationActivation() {
        return correlationActivation;
    }

    // Get actOrTar    
    public String getactOrTar() {
        return actOrTar;
    }

    @Override
    public String toString() {
        return "Machine Failure: ID: " + id + ", Constraint: " + constraint + ", Activation/Target: " + actOrTar + ", CorellationActivation: " + correlationActivation;
    }

}
