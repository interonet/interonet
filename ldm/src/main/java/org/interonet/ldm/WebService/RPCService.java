package org.interonet.ldm.WebService;

import org.interonet.ldm.Core.LDMAgent;

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
        try {
            ldmAgent.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String createTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        ldmRPCServiceLogger.info("");
        try {
            ldmAgent.createTunnelSW2VM(switchPortPeeronTT, vmID);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String addSWitchConf(Integer switchID, String controllerIP, int controllerPort) {
        ldmRPCServiceLogger.info(switchID + " " + controllerIP + " " + controllerPort);
        try {
            ldmAgent.addSWitchConf(switchID, controllerIP, controllerPort);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String addSWitchConf(String type, Integer switchID, String controllerIP, int controllerPort) {
        ldmRPCServiceLogger.info(type + " " + switchID + " " + controllerIP + " " + controllerPort);
        try {
            ldmAgent.addSWitchConf(type, switchID, controllerIP, controllerPort);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String powerOnSwitch(Integer switchID) {
        ldmRPCServiceLogger.info("");
        try {
            ldmAgent.powerOnSwitch(switchID);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String powerOnVM(Integer vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOnVM(vmID);
    }

    public String deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) {
        ldmRPCServiceLogger.info("");
        try {
            ldmAgent.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        ldmRPCServiceLogger.info("");
        try {
            ldmAgent.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String deleteSWitchConf(Integer switchID) {
        ldmRPCServiceLogger.info("");
        try{
            ldmAgent.resetSwitchConf(switchID);
            return "Success";
        } catch (Exception e){
            e.printStackTrace();
            return "Failed";
        }
    }

    public String powerOffSwitch(Integer switchID) {
        ldmRPCServiceLogger.info("");
        try {
            ldmAgent.powerOffSwitch(switchID);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String powerOffVM(Integer vmID) {
        ldmRPCServiceLogger.info("");
        return ldmAgent.powerOffVM(vmID);
    }
}
