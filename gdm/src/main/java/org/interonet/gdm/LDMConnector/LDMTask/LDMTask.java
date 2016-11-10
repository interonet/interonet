package org.interonet.gdm.LDMConnector.LDMTask;

import org.interonet.gdm.Core.Slice;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;

import java.util.List;
import java.util.Map;

public class LDMTask {
    private String sliceId;
    private Map<String, String> switchConf;
    private String controllerIP;
    private int controllerPort;
    private List<Integer> switchIdList;
    private List<Integer> hostIdList;
    private List<SwitchToSwitchTunnel> switchToSwitchTunnels;
    private List<SwitchToVMTunnel> switchToVMTunnels;
    private Map<String, Map> customSwitchConf;
    private Map<String,List<Map<String,String>>> topologyMininet;
    private List<Map<String,String>> mininetMapPort;
    private Map<String,List<String>> deviceID;
    private Map<String,String> controllerConf;
    private Map<String, Integer> userVM2domVM;

    public LDMTask(Slice slice) {
        sliceId = slice.getId();
        switchConf = slice.getSwitchConf();
        controllerIP = slice.getControllerIP();
        controllerPort = slice.getControllerPort();
        switchIdList = slice.getSwitchIdList();
        hostIdList = slice.getVmIdList();
        switchToSwitchTunnels = slice.getSwitchToSwitchTunnelList();
        switchToVMTunnels = slice.getSwitchToVMTunnelList();
        topologyMininet = slice.getTopologyMininet();
        mininetMapPort = slice.getMininetMapPort();
        deviceID = slice.getDeviceID();
        controllerConf = slice.getControllerConf();
        userVM2domVM = slice.getUserVM2domVM();
    }

    public LDMTask() {

    }

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }

    public Map<String, String> getSwitchConf() {
        return switchConf;
    }

    public void setSwitchConf(Map<String, String> switchConf) {
        this.switchConf = switchConf;
    }

    public String getControllerIP() {
        return controllerIP;
    }

    public void setControllerIP(String controllerIP) {
        this.controllerIP = controllerIP;
    }

    public int getControllerPort() {
        return controllerPort;
    }

    public void setControllerPort(int controllerPort) {
        this.controllerPort = controllerPort;
    }

    public List<Integer> getSwitchIdList() {
        return switchIdList;
    }

    public void setSwitchIdList(List<Integer> switchIdList) {
        this.switchIdList = switchIdList;
    }

    public List<Integer> getHostIdList() {
        return hostIdList;
    }

    public void setHostIdList(List<Integer> hostIdList) {
        this.hostIdList = hostIdList;
    }

    public List<SwitchToSwitchTunnel> getSwitchToSwitchTunnels() {
        return switchToSwitchTunnels;
    }

    public void setSwitchToSwitchTunnels(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        this.switchToSwitchTunnels = switchToSwitchTunnels;
    }

    public List<SwitchToVMTunnel> getSwitchToVMTunnels() {
        return switchToVMTunnels;
    }

    public void setSwitchToVMTunnels(List<SwitchToVMTunnel> switchToVMTunnels) {
        this.switchToVMTunnels = switchToVMTunnels;
    }

    public Map<String, Map> getCustomSwitchConf() {
        return customSwitchConf;
    }

    public void setCustomSwitchConf(Map<String, Map> customSwitchConf) {
        this.customSwitchConf = customSwitchConf;
    }

    public Map<String, List<Map<String, String>>> getTopologyMininet() {
        return topologyMininet;
    }

    public void setTopologyMininet(Map<String, List<Map<String, String>>> topologyMininet) {
        this.topologyMininet = topologyMininet;
    }

    public List<Map<String, String>> getMininetMapPort() {
        return mininetMapPort;
    }

    public void setMininetMapPort(List<Map<String, String>> mininetMapPort) {
        this.mininetMapPort = mininetMapPort;
    }

    public Map<String, List<String>> getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Map<String, List<String>> deviceID) {
        this.deviceID = deviceID;
    }

    public Map<String, String> getControllerConf() {
        return controllerConf;
    }

    public void setControllerConf(Map<String, String> controllerConf) {
        this.controllerConf = controllerConf;
    }

    public Map<String, Integer> getUserVM2domVM() {
        return userVM2domVM;
    }

    public void setUserVM2domVM(Map<String, Integer> userVM2domVM) {
        this.userVM2domVM = userVM2domVM;
    }
}
