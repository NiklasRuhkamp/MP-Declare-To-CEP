package com.cor.cep.util;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.cor.cep.StartEngine;
import com.cor.cep.event.OrderMaintenanceEvent;


// Class to create a random OrderMaintenanceEvent and pass it to the runtime
public class RandomOrderMaintenanceEventGenerator {

    // Logger
    private static Logger LOG = LoggerFactory.getLogger(RandomOrderMaintenanceEventGenerator.class);


    // This method gives a random manufacturer out of a list  
    public String givenList_ReturnARandomCompany() {
        List<String> givenList = Arrays.asList("ABB", "Siemens", "Emerson", "Schneider", "KUKA");
        Random rand = new Random();
        String randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    // This method gives a random boolean for available 
    public boolean returnARandomBoolean() {
        return Math.random() <0.5;
    }

    // Create events and pass them to the runtime
    public void startSendingOrderMaintenanceReadings(final long noOfOrderMaintenanceEvents) {


        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

        xrayExecutor.submit(new Runnable() {
            public void run() {

                LOG.debug(getStartingMessage());
                
                int count = 0;
                while (count < noOfOrderMaintenanceEvents) {
                    OrderMaintenanceEvent ve = new OrderMaintenanceEvent(new Random().nextInt(100), givenList_ReturnARandomCompany(), returnARandomBoolean());
                    LOG.debug(ve.toString());
                    StartEngine.epService.getEPRuntime().sendEvent(ve);      
                    count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        LOG.error("Thread Interrupted", e);
                    }
                }

            }
        });
    }

    
    
    private String getStartingMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - MAINTENANCE_ORDERS ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        return sb.toString();
    }
}
