package com.cor.cep.event;


public class DistanceEvent {

    private int distance;


    public DistanceEvent(int distance) {
        this.distance = distance;
    }
    
    public int getDistance  () {
        return distance;
    }

    @Override
    public String toString() {
        return "Distance Event: distance= " + distance;
    }

}
