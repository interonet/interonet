package org.interonet.ldm.WebService;

import org.interonet.ldm.Core.LDMAgent;

import java.io.IOException;
import java.util.logging.Logger;

public class RPCService {
    private LDMAgent ldmAgent;
    private Logger ldmRPCServiceLogger;

    public RPCService(LDMAgent ldmAgent) {
        this.ldmAgent = ldmAgent;
        ldmRPCServiceLogger = Logger.getLogger("LDMRPCServiceLogger");
    }

    public String createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
    }

    public String createTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.createTunnelSW2VM(switchPortPeeronTT, vmID);
    }

    public String addSWitchConf(Integer switchID, String controllerIP, int controllerPort) {
        ldmRPCServiceLogger.info(switchID + " " + controllerIP + " " + controllerPort);
        try {
            ldmAgent.addSWitchConf(switchID, controllerIP, controllerPort);
            return "Success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String powerOnSwitch(Integer switchID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOnSwitch(switchID);
    }

    public String powerOnVM(Integer vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOnVM(vmID);
    }

    public String deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
    }

    public String deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
    }

    public String deleteSWitchConf(Integer switchID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.deleteSWitchConf(switchID);
    }

    public String powerOffSwitch(Integer switchID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOffSwitch(switchID);
    }

    public String powerOffVM(Integer vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOffVM(vmID);
    }
}
