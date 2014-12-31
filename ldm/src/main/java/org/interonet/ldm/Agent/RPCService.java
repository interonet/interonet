package org.interonet.ldm.Agent;

import org.interonet.ldm.LDMAgent;

public class RPCService {
    private LDMAgent ldmAgent;

    public RPCService(LDMAgent ldmAgent) {
        this.ldmAgent = ldmAgent;
    }

    public String createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) {
        return ldmAgent.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
    }

    public String createTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) {
        return ldmAgent.createTunnelSW2VM(switchPortPeeronTT, peerVMPortPeeronTT);
    }

    public String addSWitchConf(Integer switchID, String controllerIP, int controllerPort) {
        return ldmAgent.addSWitchConf(switchID, controllerIP, controllerPort);
    }

    public String powerOnSwitch(Integer switchID) {
        return ldmAgent.powerOnSwitch(switchID);
    }

    public String powerOnVM(Integer vmID) {
        return ldmAgent.powerOnVM(vmID);
    }

    public String deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) {
        return ldmAgent.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
    }

    public String deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) {
        return ldmAgent.deleteTunnelSW2VM(switchPortPeeronTT, peerVMPortPeeronTT);
    }

    public String deleteSWitchConf(Integer switchID) {
        return ldmAgent.deleteSWitchConf(switchID);
    }

    public String powerOffSwitch(Integer switchID) {
        return ldmAgent.powerOffSwitch(switchID);
    }

    public String powerOffVM(Integer vmID) {
        return ldmAgent.powerOffVM(vmID);
    }
}
