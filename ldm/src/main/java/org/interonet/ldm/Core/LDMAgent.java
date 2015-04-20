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

    public void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        ldmCore.addSwitchConf(switchID, controllerIP, controllerPort);
    }

    public void addSWitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        ldmCore.addSwitchConf(type, switchID, controllerIP, controllerPort);
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


    public void resetSwitchConf(Integer switchID) throws IOException, InterruptedException {
        ldmCore.resetSwitchConf(switchID);
    }


    public String powerOffVM(Integer vmID) {
        String OffResult = "failure";
        OffResult = ldmCore.powerOffVM(vmID);
        return OffResult;
    }
    
    public String powerOnSwitch(Integer switchID)
    {
    	String OffResult = "failure";
        OffResult = ldmCore.powerOnSwitch(switchID);
        return OffResult;
    }
    
    public String powerOffSwitch(Integer switchID)
    {
    	String OffResult = "failure";
        OffResult = ldmCore.powerOffSwitch(switchID);
        return OffResult;
    }


}
