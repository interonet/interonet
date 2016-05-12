package org.interonet.gdm.WebService.VO;

import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.Switch;
import org.interonet.gdm.Core.VirtualMachine;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserSlice {
    private String id;
    private Slice.SliceStatus status = Slice.SliceStatus.NEW;
    private Integer switchesNum;
    private Integer vmsNum;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private Map<String, String> topology;
    private Map<String, String> switchConf;
    private String controllerIP;
    private int controllerPort;
    private Map<String, Map> customSwitchConf;
    private Map<Integer, Switch> switchList;
    private Map<Integer, VirtualMachine> vmList;
    private Map<String, Integer> userSW2domSW;
    private Map<String, Integer> userVM2domVM;
    private Slice.SliceException exception = Slice.SliceException.NONE;

    public UserSlice(Slice slice) {
        this.id = slice.getId();
        this.status = slice.getStatus();
        this.switchesNum = slice.getSwitchesNum();
        this.vmsNum = slice.getVmsNum();
        this.beginTime = slice.getBeginTime();
        this.endTime = slice.getEndTime();
        this.topology = slice.getTopology();
        this.switchConf = slice.getSwitchConf();
        this.controllerIP = slice.getControllerIP();
        this.controllerPort = slice.getControllerPort();
        this.customSwitchConf = slice.getCustomSwitchConf();
        this.switchList = new HashMap<>();
        for (Switch sw : slice.getSwitchList()) {
            this.switchList.put(sw.getId(), sw);
        }
        this.vmList = new HashMap<>();
        for (VirtualMachine vm : slice.getVmList()) {
            this.vmList.put(vm.getId(), vm);
        }
        this.userSW2domSW = slice.getUserSW2domSW();
        this.userVM2domVM = slice.getUserVM2domVM();
        this.exception = slice.getException();
    }

    public UserSlice(String id) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Slice.SliceStatus getStatus() {
        return status;
    }

    public void setStatus(Slice.SliceStatus status) {
        this.status = status;
    }

    public Integer getSwitchesNum() {
        return switchesNum;
    }

    public void setSwitchesNum(Integer switchesNum) {
        this.switchesNum = switchesNum;
    }

    public Integer getVmsNum() {
        return vmsNum;
    }

    public void setVmsNum(Integer vmsNum) {
        this.vmsNum = vmsNum;
    }

    public ZonedDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(ZonedDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Map<String, String> getTopology() {
        return topology;
    }

    public void setTopology(Map<String, String> topology) {
        this.topology = topology;
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

    public Map<String, Map> getCustomSwitchConf() {
        return customSwitchConf;
    }

    public void setCustomSwitchConf(Map<String, Map> customSwitchConf) {
        this.customSwitchConf = customSwitchConf;
    }

    public Map<Integer, Switch> getSwitchList() {
        return switchList;
    }

    public void setSwitchList(Map<Integer, Switch> switchList) {
        this.switchList = switchList;
    }

    public Map<Integer, VirtualMachine> getVmList() {
        return vmList;
    }

    public void setVmList(Map<Integer, VirtualMachine> vmList) {
        this.vmList = vmList;
    }

    public Map<String, Integer> getUserSW2domSW() {
        return userSW2domSW;
    }

    public void setUserSW2domSW(Map<String, Integer> userSW2domSW) {
        this.userSW2domSW = userSW2domSW;
    }

    public Map<String, Integer> getUserVM2domVM() {
        return userVM2domVM;
    }

    public void setUserVM2domVM(Map<String, Integer> userVM2domVM) {
        this.userVM2domVM = userVM2domVM;
    }

    public Slice.SliceException getException() {
        return exception;
    }

    public void setException(Slice.SliceException exception) {
        this.exception = exception;
    }
}
