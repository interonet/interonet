package org.interonet.gdm.OperationCenter;

import java.util.Map;

public interface IOperationCenter {
    void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable;

    void createTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) throws Throwable;

    void addSwitchConf(String oFVersion, Integer switchID, String controllerIP, int controllerPort) throws Throwable;

    void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Throwable;

    void powerOnSwitch(Integer switchID) throws Throwable;

    void powerOnVM(Integer vmID) throws Throwable;

    void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable;

    void deleteTunnelSW2VM(int switchPortPeeronTT, int peerVMPortPeeronTT) throws Throwable;

    void deleteSWitchConf(Integer switchID) throws Throwable;

    void powerOffSwitch(Integer switchID) throws Throwable;

    void powerOffVM(Integer vmID) throws Throwable;


}
