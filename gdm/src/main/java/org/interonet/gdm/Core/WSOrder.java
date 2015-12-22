package org.interonet.gdm.Core;

import java.util.List;
import java.util.Map;

public class WSOrder extends Order {

    String orderID;
    String username;
    List<Integer> switchIDs;
    List<Integer> vmIDs;
    String beginTime;
    String endTime;
    Map<String, String> topology;
    Map<String, String> switchConf;
    String controllerIP;
    int controllerPort;
    Map<String, Map> customSwitchConf;

    public WSOrder(String orderID,
                   String username,
                   List<Integer> switchIDs,
                   List<Integer> vmIDs,
                   String beginTime,
                   String endTime,
                   Map<String, String> topology,
                   Map<String, String> switchConf,
                   String controllerIP,
                   int controllerPort,
                   Map<String, Map> customSwitchConf) {
        this.orderID = orderID;
        this.username = username;
        this.switchIDs = switchIDs;
        this.vmIDs = vmIDs;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.topology = topology;
        this.switchConf = switchConf;
        this.controllerIP = controllerIP;
        this.controllerPort = controllerPort;
        this.customSwitchConf = customSwitchConf;
    }
}