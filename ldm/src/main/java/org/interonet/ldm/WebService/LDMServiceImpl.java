package org.interonet.ldm.WebService;

import org.interonet.ldm.Core.LDMCore;
import org.interonet.ldm.service.LDMService;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LDMServiceImpl implements LDMService {
    private LDMCore ldmCore;
    private Logger logger;

    public LDMServiceImpl(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
        logger = Logger.getLogger("LDMRPCServiceLogger");
    }

    @Override
    public String createSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> switchToSwitchTunnelList) {
        logger.info("switchToSwitchTunnelList = [" + switchToSwitchTunnelList + "]");
        try {
            ldmCore.createSwitchToSwitchTunnel(switchToSwitchTunnelList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String createSwitchToVMTunnel(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.info("swvmTunnelList = [" + switchToVMTunnelList + "]");
        try {
            ldmCore.createSwitchToVMTunnel(switchToVMTunnelList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String powerOnVM(List<Integer> vmIdList) {
        logger.info("vmIdList = [" + vmIdList + "]");
        try {
            ldmCore.powerOnVM(vmIdList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String deleteSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        logger.info("switchToSwitchTunnels = [" + switchToSwitchTunnels + "]");
        try {
            ldmCore.deleteSwitchToSwitchTunnel(switchToSwitchTunnels);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String deleteSwitchToVMTunnel(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.info("switchToVMTunnelList = [" + switchToVMTunnelList + "]");
        try {
            ldmCore.deleteSwitchToVMTunnel(switchToVMTunnelList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    public String powerOffVM(List<Integer> vmIdList) {
        logger.info("vmIdList = [" + vmIdList + "]");
        try {
            ldmCore.powerOffVM(vmIdList);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Deprecated
    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) {
        logger.info("");
        try {
            ldmCore.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        logger.info("");
        try {
            ldmCore.createTunnelSW2VM(switchPortPeeronTT, vmID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void addSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) {
        logger.info(type + " " + switchID + " " + controllerIP + " " + controllerPort);
        try {
            ldmCore.addSwitchConf(type, switchID, controllerIP, controllerPort);
        } catch (Exception e) {
            e.printStackTrace();
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
    @Deprecated
    public String addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchId, String controllerIP, int controllerPort) {
        logger.info("customSwitchConfGDM = [" + customSwitchConfGDM + "], switchId = [" + switchId + "], controllerIP = [" + controllerIP + "], controllerPort = [" + controllerPort + "]");
        try {
            ldmCore.addSwitchConf(customSwitchConfGDM, switchId, controllerIP, controllerPort);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    @Deprecated
    public String powerOnSwitch(Integer switchID) {
        logger.info("");
        try {
            ldmCore.powerOnSwitch(switchID);
            return "Success";
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return "Failed";
        }
    }

    @Deprecated
    public String powerOnVM(Integer vmID) {
        try {
            logger.info("");
            return ldmCore.powerOnVM(vmID);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return "Failed";
        }
    }

    @Deprecated
    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) {
        logger.info("");
        try {
            ldmCore.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) {
        logger.info("");
        try {
            ldmCore.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void deleteSWitchConf(Integer switchID) {
        logger.info("");
        try {
            ldmCore.resetSwitchConf(switchID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void powerOffSwitch(Integer switchID) {
        logger.info("");
        try {
            ldmCore.powerOffSwitch(switchID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String powerOffVM(Integer vmID) {
        logger.info("");
        return ldmCore.powerOffVM(vmID);
    }
}
