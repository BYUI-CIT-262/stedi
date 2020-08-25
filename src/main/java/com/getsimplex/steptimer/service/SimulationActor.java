package com.getsimplex.steptimer.service;

import akka.actor.UntypedActor;
import com.getsimplex.steptimer.model.BrowserMessage;
import com.getsimplex.steptimer.model.StartSimulation;

import java.util.logging.Logger;

/**
 * Created by sean on 8/16/2016.
 */
public class SimulationActor extends UntypedActor {
    private static Logger logger = Logger.getLogger(SimulationActor.class.getName());

    public void onReceive(Object object){

        if (object instanceof StartSimulation){
            StartSimulation startSimulation = (StartSimulation) object;
            logger.info("SimulationActor received StartSimulationMessage to start simulation for: "+startSimulation.getNumberOfCustomers()+" test customers");
            try{
                logger.info("Creating test customers...");
                SimulationDataDriver.generateTestCustomers(startSimulation.getNumberOfCustomers());
                logger.info("Starting infinite loop for test data");
                SimulationDataDriver.createRapidStepTests();
            } catch (Exception e){
                logger.severe(e.getMessage());
            }
        }

    }
}
