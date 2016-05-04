package org.interonet.gdm.LDMConnector.LDMTaskCall;

import org.interonet.gdm.LDMConnector.LDMCalls;
import org.interonet.gdm.LDMConnector.LDMTask.LDMStopTask;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class LDMTaskStopCall extends LDMTaskCall implements Callable<LDMTaskReturn> {
    private Logger logger = LoggerFactory.getLogger(LDMTaskStopCall.class);
    private LDMStopTask ldmStopTask;

    public LDMTaskStopCall(LDMStopTask ldmStopTask, LDMCalls ldmRPCService) {
        super(ldmRPCService);
        this.ldmStopTask = ldmStopTask;
    }

    @Override
    public LDMTaskReturn call() {
        LDMTaskReturn ldmTaskReturn = new LDMTaskReturn(ldmStopTask.getSliceId());
        try {
            ldmCalls.deleteTunnelSW2SW(ldmStopTask.getSwitchToSwitchTunnels());
            ldmCalls.deleteTunnelSW2VM(ldmStopTask.getSwitchToVMTunnels());
            ldmCalls.powerOffVM(ldmStopTask.getHostIdList());
            ldmTaskReturn.setSuccess(true);
            return ldmTaskReturn;
        } catch (Throwable throwable) {
            ldmTaskReturn.setSuccess(false);
            ldmTaskReturn.setThrowable(throwable);
            logger.error("ldmService RPC Call Exception", throwable);
            return ldmTaskReturn;
        }


        /*
        *
        * Comment these lines for the fuck onetswitch30's eth0 driver in u-boot-meshsr.
        * By Samuel, Dec, 14
        *
        * */

        //for (Integer switchID : switchesIDs) {
        //    operationCenter.deleteSWitchConf(switchID);
        //}

        /*
        *
        * Comment these lines for the fuck box to fix the power system.
        * by Samuel, Dec, 14
        *
        * */
        //for (Integer switchID : switchesIDs) {
        //    operationCenter.powerOffSwitch(switchID);
        //}
    }
}
