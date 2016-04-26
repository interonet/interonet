package org.interonet.gdm.LDMConnector;

import java.util.List;
import java.util.Map;

public interface LDMService {
    void createTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) throws Throwable;

    @Deprecated
    void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable;

    @Deprecated
    void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable;

    void createTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) throws Throwable;

    void addSwitchConf(String type, Integer switchId, String controllerIP, int controllerPort) throws Throwable;

    /*
         *  customSwitchConf should be like this.
         *  {
         *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
         *     "boot-bin": "http://202.117.15.79/ons_bak/system.bit",
         *     "uImage": "http://202.117.15.79/ons_bak/uImage",
         *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
         *  }
         * */
    void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchId, String controllerIP, int controllerPort) throws Throwable;

    void powerOnSwitch(Integer switchID) throws Throwable;

    @Deprecated
    String powerOnVM(Integer vmID) throws Throwable;

    String powerOnVM(List<Integer> vmIDList) throws Throwable;

    @Deprecated
    void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable;

    void deleteTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) throws Throwable;

    @Deprecated
    void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable;

    void deleteTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) throws Throwable;

    void deleteSWitchConf(Integer switchID) throws Throwable;

    @Deprecated
    void powerOffSwitch(Integer switchID) throws Throwable;

    @Deprecated
    String powerOffVM(Integer vmID) throws Throwable;

    String powerOffVM(List<Integer> vmIdList) throws Throwable;
}
