package org.interonet.gdm.Core;

import java.util.List;
import java.util.Map;

public class WTOrder extends Order {
    String sliceID;
    String username;
    List<Integer> switchIDs;
    List<Integer> vmIDs;
    String beginTime;
    String endTime;
    Map<String, String> topology;
    Map<String, String> switchConf;
    String controllerIP;
    int controllerPort;
    List<SWSWTunnel> swswTunnel;
    List<SWVMTunnel> swvmTunnel;
    Map<String, Integer> userSW2domSW;
    Map<String, Integer> userVM2domVM;


    public WTOrder(String sliceID,
                   String username,
                   List<Integer> switchIDs,
                   List<Integer> vmIDs,
                   String beginTime,
                   String endTime,
                   Map<String, String> topology,
                   Map<String, String> switchConf,
                   String controllerIP,
                   int controllerPort,
                   List<SWSWTunnel> swswTunnel,
                   List<SWVMTunnel> swvmTunnel,
                   Map<String, Integer> userSW2domSW,
                   Map<String, Integer> userVM2domVM) {
        this.sliceID = sliceID;
        this.username = username;
        this.switchIDs = switchIDs;
        this.vmIDs = vmIDs;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.topology = topology;
        this.switchConf = switchConf;
        this.controllerIP = controllerIP;
        this.controllerPort = controllerPort;
        this.swswTunnel = swswTunnel;
        this.swvmTunnel = swvmTunnel;
        this.userSW2domSW = userSW2domSW;
        this.userVM2domVM = userVM2domVM;
    }

}
