package org.interonet.gdm.OperationCenter;

/**
 * Created by samuel on 1/2/15.
 */
public interface IOperationCenter {
    void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable;

    void createTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) throws Throwable;

    void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws Throwable;

    void addSWitchConf(String s, Integer domSW, String controllerIP, int controllerPort) throws Throwable;

    void powerOnSwitch(Integer switchID) throws Throwable;

    void powerOnVM(Integer vmID) throws Throwable;

    void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable;

    void deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) throws Throwable;

    void deleteSWitchConf(Integer switchID) throws Throwable;

    void powerOffSwitch(Integer switchID) throws Throwable;

    void powerOffVM(Integer vmID) throws Throwable;


}
