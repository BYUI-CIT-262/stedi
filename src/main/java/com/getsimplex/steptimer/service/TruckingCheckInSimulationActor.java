package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.ContinueTruckingSimulation;
import com.getsimplex.steptimer.model.TruckingSimulationDataDriver;

import java.util.logging.Logger;

public class TruckingCheckInSimulationActor {
    private static Logger logger = Logger.getLogger(BalanceSimulationActor.class.getName());

    public void onReceive(Object object) {
        if (object instanceof ContinueTruckingSimulation){
            try{
                TruckingSimulationDataDriver.createCheckInStatusUpdates();
            } catch (Exception e){
                logger.severe("Unable to create CheckIn Status Updates due to: "+e.getMessage());
            }
        }
    }
}
