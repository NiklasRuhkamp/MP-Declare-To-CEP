package com.cor.cep.event;


public class ButtonEvent {

    private int number;


    public ButtonEvent(int number) {
        this.number = number;
    }
    
    public int getNumber  () {
        return number;
    }
    

    @Override
    public String toString() {
        return "Button Event: number= " + number;
    }

}
