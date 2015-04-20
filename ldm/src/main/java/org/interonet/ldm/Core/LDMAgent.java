package org.interonet.ldm.Core;

import java.io.IOException;

public class LDMAgent {
    private LDMCore ldmCore;

    public LDMAgent(LDMCore ldmCore) {
        this.ldmCore = ldmCore;
    }

    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer)throws Exception{
        ldmCore.createTunnelSW2SW(switchPortPeer,peerSwitchPortPeer);
    }

    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception{
        ldmCore.createTunnelSW2VM(switchPortPeeronTT,vmID);
    }

    public void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        ldmCore.addSwitchConf(switchID, controllerIP, controllerPort);
    }

    public void addSWitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        ldmCore.addSwitchConf(type, switchID, controllerIP, controllerPort);
    }

    public String powerOnVM(Integer vmID) {
        return  ldmCore.powerOnVM(vmID);
//        String OnResult = "failure";
//        OnResult = ldmCore.powerOnVM(vmID);
//        return OnResult;
    }

    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception{
        ldmCore.deleteTunnelSW2SW(switchPortPeeronTT,athrSwitchPortPeeronTT);
    }

    public void deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT)throws Exception{
        ldmCore.deleteTunnelSW2VM(switchPortPeeronTT,peerVMPortPeeronTT);
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
