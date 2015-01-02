package org.interonet.ldm.Core;

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

    public String addSWitchConf(Integer switchID, String controllerIP, int controllerPort) {
        return null;
    }

    public String powerOnSwitch(Integer switchID) {
        return null;
    }

    public String powerOnVM(Integer vmID) {
        ldmCore.powerOnVM(vmID);
        return null;
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
        ldmCore.powerOnVM(vmID);
        return null;
    }
}
