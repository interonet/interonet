package org.interonet.gdm.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Slice {
    private String id;
    private SliceStatus status = SliceStatus.NEW;
    private String username;
    private Integer switchesNum;
    private Integer vmsNum;
    private ZonedDateTime beginTime;
    private ZonedDateTime endTime;
    private Map<String, String> topology;
    private Map<String, String> switchConf;
    private String controllerIP;
    private int controllerPort;
    private Map<String, Map> customSwitchConf;
    private List<Integer> switchIdList;
    private List<Switch> switchList;
    private List<Integer> vmIdList;
    private List<VirtualMachine> vmList;
    private List<SwitchToSwitchTunnel> switchToSwitchTunnelList;
    private List<SwitchToVMTunnel> switchToVMTunnelList;
    private Map<String, Integer> userSW2domSW;
    private Map<String, Integer> userVM2domVM;
    private SliceException exception = SliceException.NONE;
    private Map<String,List<String>> deviceID;
    private List<Map<String,String>> mininetMapPort;
    private Map<String,List<Map<String,String>>> topologyMininet;
    private Map<String,String> controllerConf;



    public Slice() {
    }

    public Slice(Slice other) {
        username = other.getUsername();
        switchesNum = other.getSwitchesNum();
        vmsNum = other.getVmsNum();
        beginTime = other.getBeginTime();
        endTime = other.getEndTime();
        topology = other.getTopology();
        switchConf = other.getSwitchConf();
        controllerIP = other.getControllerIP();
        controllerPort = other.getControllerPort();
        customSwitchConf = other.getCustomSwitchConf();
    }

    public List<Switch> getSwitchList() {
        return switchList;
    }

    public void setSwitchList(List<Switch> switchList) {
        this.switchList = switchList;
    }

    public List<VirtualMachine> getVmList() {
        return vmList;
    }

    public void setVmList(List<VirtualMachine> vmList) {
        this.vmList = vmList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SliceStatus getStatus() {
        return status;
    }

    public void setStatus(SliceStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Integer> getSwitchIdList() {
        return switchIdList;
    }

    public void setSwitchIdList(List<Integer> switchIdList) {
        this.switchIdList = switchIdList;
    }

    public List<Integer> getVmIdList() {
        return vmIdList;
    }

    public void setVmIdList(List<Integer> vmIdList) {
        this.vmIdList = vmIdList;
    }

    public List<SwitchToSwitchTunnel> getSwitchToSwitchTunnelList() {
        return switchToSwitchTunnelList;
    }

    public void setSwitchToSwitchTunnelList(List<SwitchToSwitchTunnel> switchToSwitchTunnelList) {
        this.switchToSwitchTunnelList = switchToSwitchTunnelList;
    }

    public List<SwitchToVMTunnel> getSwitchToVMTunnelList() {
        return switchToVMTunnelList;
    }

    public void setSwitchToVMTunnelList(List<SwitchToVMTunnel> switchToVMTunnelList) {
        this.switchToVMTunnelList = switchToVMTunnelList;
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

    public SliceException getException() {
        return exception;
    }

    public void setException(SliceException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "Slice{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", switchesNum=" + switchesNum +
                ", vmsNum=" + vmsNum +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", topology=" + topology +
                ", mininetMapPort =" + mininetMapPort +
                ", switchConf=" + switchConf +
                ", controllerIP='" + controllerIP + '\'' +
                ", controllerPort=" + controllerPort +
                ", customSwitchConf=" + customSwitchConf +
                ", switchIdList=" + switchIdList +
                ", vmIdList=" + vmIdList +
                ", switchToSwitchTunnelList=" + switchToSwitchTunnelList +
                ", switchToVMTunnelList=" + switchToVMTunnelList +
                ", userSW2domSW=" + userSW2domSW +
                ", userVM2domVM=" + userVM2domVM +
                ", exception=" + exception +
                '}';
    }

    public Map<String, List<String>> getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Map<String, List<String>> deviceID) {
        this.deviceID = deviceID;
    }

    public List<Map<String, String>> getMininetMapPort() {
        return mininetMapPort;
    }

    public void setMininetMapPort(List<Map<String, String>> mininetMapPort) {
        this.mininetMapPort = mininetMapPort;
    }

    public Map<String, List<Map<String, String>>> getTopologyMininet() {
        return topologyMininet;
    }

    public void setTopologyMininet(Map<String, List<Map<String, String>>> topologyMininet) {
        this.topologyMininet = topologyMininet;
    }

    public Map<String, String> getControllerConf() {
        return controllerConf;
    }

    public void setControllerConf(Map<String, String> controllerConf) {
        this.controllerConf = controllerConf;
    }

    public enum SliceStatus {
        NEW,
        TIME_WAITING,
        RUNNABLE,
        RUNNING_WAITING,
        RUNNING,
        TERMINATED_WAITING,
        TERMINATABLE,
        TERMINATED
    }

    public enum SliceException {
        NONE,
        LDM_TASK_START_CALL_TIMEOUT, //vmThreadPool.awaitTermination(10, TimeUnit.MINUTES)
        LDM_TASK_STOP_CALL_TIMEOUT,//vmThreadPool.awaitTermination(10, TimeUnit.MINUTES)
        WRONG_TOPOLOGY_FORMAT, //parse topology error when start. // TODO: 4/22/16 it can be done when submit.
        USER_OPERATION // user force to stop a slice.
    }

    public static class SliceParser {
        static private Logger logger = LoggerFactory.getLogger(Slice.SliceParser.class);

        static public Slice parse(String orderStr) throws Exception {
            Integer switchNum;
            Integer vmsNum;
            ZonedDateTime begin;
            ZonedDateTime end;
            Map<String, String> topology;
            Map<String, String> switchConf;
            Map<String, String> controllerConf;
            Map<String, Map> customSwitchConf;
            Map<String,List<Map<String,String>>> topologyMininet = new HashMap<>();
            List<String> switchID = new ArrayList<>();
            List<String> vmID = new ArrayList<>();
            Integer countMin = 0;

            Slice slice = new Slice();

            Map<String, Map<String, Object>> parser = new ObjectMapper().readValue(orderStr, Map.class);
            Map<String, String> num = (Map) parser.get("num");
            if (num == null) throw new Exception("Parse Slice Error: can find 'num' element.");
            String switchNumStr = num.get("switchesNum");
            if (switchNumStr == null)
                throw new Exception("Parse Slice Error: can find 'switchesNum' in 'num' element.");
            String vmsNumStr = num.get("vmsNum");
            if (vmsNumStr == null) throw new Exception("Parse Slice Error: can find 'vmsNum' in 'num' element.");
            switchNum = Integer.parseInt(switchNumStr);
            vmsNum = Integer.parseInt(vmsNumStr);
            slice.setSwitchesNum(switchNum);
            slice.setVmsNum(vmsNum);

            Map<String, String> time = (Map) parser.get("time");
            if (time == null) throw new Exception("Parse Slice Error: can find 'time' element.");
            String beginStr = time.get("begin");
            String endStr = time.get("end");
            if (beginStr == null) throw new Exception("Parse Slice Error: can find 'begin' in 'time' element.");
            if (endStr == null) throw new Exception("Parse Slice Error: can find 'end' in 'time' element.");
            begin = ZonedDateTime.parse(beginStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            end = ZonedDateTime.parse(endStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            slice.setBeginTime(begin);
            slice.setEndTime(end);

            Map<String,List<String>> deviceID = (Map) parser.get("deviceID");
            switchID = deviceID.get("switches");
            vmID = deviceID.get("vms");
            /*
            * TODO validate the topology and switch configuration.
            * Bug Tracking Link: https://github.com/samueldeng/interonet/issues/11
            * */
            topology = (Map) parser.get("topology");
            switchConf = (Map) parser.get("switchConf");
            for(Map.Entry<String,Map<String,Object>> entry : parser.entrySet()) {
                if(entry.getKey().startsWith("topologyMininet")){
                    List<Map<String,String>> topologyMinTemp = (List) entry.getValue();
                    topologyMininet.put(entry.getKey(),topologyMinTemp);
                }
            }
            if (topology == null || switchConf == null) throw new Exception("topology or swConf are null");
            if (switchConf.size() != switchNum) throw new Exception("switchConf.size()!=switchNum");
            for (Map.Entry<String, String> entry : switchConf.entrySet()) {
                if (entry.getValue().equals("custom")) {
                    customSwitchConf = (Map) parser.get("customSwitchConf");
                    if (customSwitchConf == null)
                        throw new Exception("Parse Error: 'customSwitchConf' is null but there exists custom in switchConf");
                    logger.debug(entry.getKey() + ":" + entry.getValue());
                    slice.setCustomSwitchConf(customSwitchConf);
                } else if (entry.getValue().equals("OF1.0")) {
                    logger.debug(entry.getKey() + ": OF1.0");
                } else if (entry.getValue().equals("OF1.3")) {
                    logger.debug(entry.getKey() + ": OF1.3");
                }
            }
            List<Map<String,String>> mininetMapPort = (List) parser.get("mininetMapPort");
            if(mininetMapPort.size()<topologyMininet.size())throw new Exception("Parse Error: Every Mininet should have one port connected to the outside network.");
            if(topologyMininet.size() !=0){
                for(Map.Entry<String,List<Map<String,String>>> entry1 : topologyMininet.entrySet()) {
                    for (Map.Entry<String, String> entry : topology.entrySet()) {
                        if (entry.getKey().substring(0, entry.getKey().indexOf(":")).equals(entry1.getKey().substring(15)) || entry.getValue().substring(0, entry.getKey().indexOf(":")).equals(entry1.getKey().substring(11))) {
                            countMin++;
                        }
                    }
                    if(countMin>1){
                        throw new Exception("Parse Error: Every Mininet should have one port connected to the outside network.");
                    }
                    countMin=0;
                }
            }

            int countSwitchID = 0;
            int countVmID = 0;
            for(int i =0;i<switchID.size();i++){
                for(Map.Entry<String,String> entry : topology.entrySet()){
                    if(entry.getKey().substring(0,entry.getKey().indexOf(":")).equals(switchID.get(i))||entry.getValue().substring(0,entry.getKey().indexOf(":")).equals(switchID.get(i))){
                        countSwitchID++;
                        break;
                    }
                }
            }
            for(int i =0;i<vmID.size();i++){
                for(Map.Entry<String,String> entry : topology.entrySet()){
                    if(entry.getKey().substring(0,entry.getKey().indexOf(":")).equals(vmID.get(i))||entry.getValue().substring(0,entry.getKey().indexOf(":")).equals(vmID.get(i))){
                        countVmID++;
                        break;
                    }
                }
            }
            for(Map.Entry<String,String> entry : topology.entrySet()){
                if(entry.getKey().substring(0,entry.getKey().indexOf(":")).equals(entry.getValue().substring(0,entry.getKey().indexOf(":"))))
                    throw new Exception("Parse Error: Any device can not connect to itself.");
                else if(entry.getKey().startsWith("h")&&entry.getValue().startsWith("h"))
                    throw new Exception("Parse Error: Vitual machine can not connect to itself.");
            }
            if(countSwitchID < switchID.size() || countVmID < vmID.size())
                throw new Exception("Parse Error: There are isolated nodes here.");



            slice.setTopology(topology);
            slice.setSwitchConf(switchConf);
            slice.setDeviceID(deviceID);
            slice.setMininetMapPort(mininetMapPort);
            slice.setTopologyMininet(topologyMininet);

            controllerConf = (Map) parser.get("controllerConf");
            if (controllerConf == null) throw new Exception("Parse Error: 'controllerConf' is null");
            slice.setControllerConf(controllerConf);
            String ctrlIP = controllerConf.get("ip");
            if (ctrlIP == null) throw new Exception("Parse Error: 'ip' in 'controllerConf' is null");
            String match = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
            if (!ctrlIP.matches(match)) {
                throw new Exception("orderStr format wrong: controller ip");
            }
            slice.setControllerIP(ctrlIP);

            try {
                String ctrlPort = controllerConf.get("port");
                if (ctrlPort == null) throw new Exception("Parse Error: 'port' in 'controllerConf' is null");

                if (Integer.parseInt(ctrlPort) > 65535) {
                    throw new Exception("orderStr format wrong: controller port");
                }
                slice.setControllerPort(Integer.parseInt(ctrlPort));
            } catch (Exception e) {
                logger.error("slice parser error.", e);
                throw e;
            }

            return slice;
        }
    }
}
