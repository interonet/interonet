package org.interonet.ldm.Core;

import java.io.IOException;
import java.util.Map;

public class LDMAgent {
    private LDMCore ldmCore;

    public LDMAgent(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
    }

    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Exception {
        ldmCore.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
    }

    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        ldmCore.createTunnelSW2VM(switchPortPeeronTT, vmID);
    }

    public void addSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        ldmCore.addSwitchConf(type, switchID, controllerIP, controllerPort);
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
    public void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Exception {
        ldmCore.addSwitchConf(customSwitchConfGDM, switchID, controllerIP, controllerPort);
    }

    public String powerOnVM(Integer vmID) {
        return ldmCore.powerOnVM(vmID);
//        String OnResult = "failure";
//        OnResult = ldmCore.powerOnVM(vmID);
//        return OnResult;
    }

    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception {
        ldmCore.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
    }

    public void deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) throws Exception {
        ldmCore.deleteTunnelSW2VM(switchPortPeeronTT, peerVMPortPeeronTT);
    }

    public String deleteSWitchConf(Integer switchID) {
        return null;
    }


    public void resetSwitchConf(Integer switchID) throws IOException, InterruptedException {
        ldmCore.resetSwitchConf(switchID);
    }


    public String powerOffVM(Integer vmID) {
        return ldmCore.powerOffVM(vmID);
//        String OffResult = "failure";
//        OffResult = ldmCore.powerOffVM(vmID);
//        return OffResult;
    }

    public void powerOnSwitch(Integer switchID) throws Exception {
        ldmCore.powerOnSwitch(switchID);
    }

    public void powerOffSwitch(Integer switchID) throws Exception {
        ldmCore.powerOffSwitch(switchID);
    }


}
