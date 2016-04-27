package org.interonet.ldm.service;

import java.util.List;
import java.util.Map;

public interface LDMService {
    String createSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> switchToSwitchTunnelList);

    String createSwitchToVMTunnel(List<SwitchToVMTunnel> switchToVMTunnelList);

    String deleteSwitchToSwitchTunnel(List<SwitchToSwitchTunnel> switchToSwitchTunnels);

    String deleteSwitchToVMTunnel(List<SwitchToVMTunnel> switchToVMTunnelList);

    String powerOnVM(List<Integer> vmIdList);

    String powerOffVM(List<Integer> vmIdList);

    @Deprecated
    void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable;

    @Deprecated
    void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable;

    @Deprecated
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
    @Deprecated
    String addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchId, String controllerIP, int controllerPort);

    @Deprecated
    String powerOnSwitch(Integer switchID) throws Throwable;

    @Deprecated
    String powerOnVM(Integer vmID) throws Throwable;

    @Deprecated
    void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable;

    @Deprecated
    void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable;

    @Deprecated
    void deleteSWitchConf(Integer switchID) throws Throwable;

    @Deprecated
    void powerOffSwitch(Integer switchID) throws Throwable;

    @Deprecated
    String powerOffVM(Integer vmID) throws Throwable;
}
