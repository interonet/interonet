package org.interonet.ldm.Core;

import java.io.IOException;

public class LDMAgent {
    private LDMCore ldmCore;

    public LDMAgent(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
    }

    public String createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) {
        return null;
    }

    public String createTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) {
        return null;
    }

    public void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws IOException {
        ldmCore.addSwitchConf(switchID, controllerIP, controllerPort);
    }

    public String powerOnSwitch(Integer switchID) {
        return null;
    }

    public String powerOnVM(Integer vmID) {
        String OnResult = "failure";
        OnResult = ldmCore.powerOnVM(vmID);
        return OnResult;
    }

    public String deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) {
        return null;
    }

    public String deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) {
        return null;
    }

    public String deleteSWitchConf(Integer switchID) {
        return null;
    }

    public String powerOffSwitch(Integer switchID) {
        return null;
    }

    public String powerOffVM(Integer vmID) {
        String OffResult = "failure";
        OffResult = ldmCore.powerOffVM(vmID);
        return OffResult;
    }
}
