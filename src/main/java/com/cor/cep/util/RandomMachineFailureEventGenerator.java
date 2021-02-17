package com.cor.cep.util;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.cor.cep.StartEngine;
import com.cor.cep.event.MachineFailureEvent;


// Class to create a random MachineFailureEvents and pass it to the runtime
public class RandomMachineFailureEventGenerator {

    private static Logger LOG = LoggerFactory.getLogger(RandomMachineFailureEventGenerator.class);

    // This method gives a random manufacturer out of a list  
    public static String givenList_ReturnARandomManufaturer() {
        List<String> givenList = Arrays.asList("ABB", "Siemens", "Emerson", "Schneider", "KUKA");
        Random rand = new Random();
        String randomElement = givenList.get(rand.nextInt(givenList.size()));
        return randomElement;
    }

    // This method gives a random boolean for productionCritical 
    public static boolean returnARandomBoolean() {
        return Math.random() < 0.5;
    }

    // Create events and pass them to the runtime
    public void startSendingMachineFailureReadings(final long noOfMachineFailureEvents) {

        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

        xrayExecutor.submit(new Runnable() {
            public void run() {

                LOG.debug(getStartingMessage());

                int count = 0;
                while (count < noOfMachineFailureEvents) {
                    MachineFailureEvent ve = new MachineFailureEvent(new Random().nextInt(100),
                            givenList_ReturnARandomManufaturer(), returnARandomBoolean());
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

    
    
    public static String getStartingMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - MASCHINE_FAILURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        return sb.toString();
    }
}
