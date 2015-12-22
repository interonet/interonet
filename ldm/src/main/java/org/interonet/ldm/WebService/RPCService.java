package org.interonet.ldm.WebService;

import org.interonet.ldm.Core.LDMAgent;

import java.util.Map;
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

    public String addSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) {
        ldmRPCServiceLogger.info(type + " " + switchID + " " + controllerIP + " " + controllerPort);
        try {
            ldmAgent.addSwitchConf(type, switchID, controllerIP, controllerPort);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    /*
    *   customSwitchConf should be like this.
    *
    *  {
    *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
    *     "boot-bin": "http://202.117.15.79/ons_bak/system.bit",
    *     "uImage": "http://202.117.15.79/ons_bak/uImage",
    *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
    *  }
    *
    * */
    public String addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchId, String controllerIP, int controllerPort) {
        ldmRPCServiceLogger.info("customSwitchConfGDM = [" + customSwitchConfGDM + "], switchId = [" + switchId + "], controllerIP = [" + controllerIP + "], controllerPort = [" + controllerPort + "]");
        try {
            ldmAgent.addSwitchConf(customSwitchConfGDM, switchId, controllerIP, controllerPort);
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
            ldmRPCServiceLogger.severe(e.getMessage());
            return "Failed";
        }
    }

    public String powerOnVM(Integer vmID) {
        try {
            ldmRPCServiceLogger.info("");
            return ldmAgent.powerOnVM(vmID);
        } catch (Exception e) {
            ldmRPCServiceLogger.severe(e.getMessage());
            return "Failed";
        }
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
