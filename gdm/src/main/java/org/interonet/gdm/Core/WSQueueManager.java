package org.interonet.gdm.Core;

import org.apache.commons.io.FileUtils;
import org.interonet.gdm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.gdm.Core.Utils.DayTime;
import org.interonet.gdm.OperationCenter.IOperationCenter;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class WSQueueManager implements Runnable {
    WaitingStartQueue waitingStartQueue;
    WaitingTermQueue waitingTermQueue;
    IOperationCenter operationCenter;
    IConfigurationCenter configurationCenter;
    GDMCore core;

    public WSQueueManager(GDMCore core, WaitingStartQueue wsQueue, WaitingTermQueue wtQueue, IOperationCenter operationCenter) {
        this.core = core;
        this.waitingStartQueue = wsQueue;
        this.waitingTermQueue = wtQueue;
        this.operationCenter = operationCenter;
        configurationCenter = core.getConfigurationCenter();
    }

    public List<WSOrder> checkOrders() {
        List<WSOrder> list = new ArrayList<>();
        DayTime currentTime = new DayTime(new SimpleDateFormat("HH:mm").format(new Date()));

        for (WSOrder wsOrder : waitingStartQueue.getQueue()) {
            if (new DayTime(wsOrder.beginTime).earlyThan(currentTime))
                list.add(wsOrder);
        }
        return list;
    }

    public void startSlice(List<WSOrder> wsOrderList) throws Throwable {
        for (WSOrder wsOrder : wsOrderList) {
            // Threads Pool to start up switch and VM.
            Collection<Thread> threadsStartVM = new HashSet<>();
            Collection<Thread> threadsAddConf = new HashSet<>();

            List<Integer> switchesIDs = wsOrder.switchIDs;
            List<Integer> vmIDs = wsOrder.vmIDs;
            Map<String, Integer> userSW2domSW = new HashMap<>();
            Map<String, Integer> userVM2domVM = new HashMap<>();
            for (int i = 0; i < switchesIDs.size(); i++)
                userSW2domSW.put("s" + Integer.toString(i), switchesIDs.get(i));

            for (int i = 0; i < vmIDs.size(); i++)
                userVM2domVM.put("h" + Integer.toString(i), vmIDs.get(i));

            List<SWSWTunnel> swswTunnels = getswswTunnel(wsOrder.topology, userSW2domSW, userVM2domVM);
            List<SWVMTunnel> swvmTunnels = getswvmTunnel(wsOrder.topology, userSW2domSW, userVM2domVM);

            for (SWSWTunnel swswT : swswTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.SwitchID, swswT.SwitchIDPortNum);
                int athrSwitchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.PeerSwitchID, swswT.PeerSwitchIDPortNum);
                operationCenter.createTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
            }

            for (SWVMTunnel swvmTunnel : swvmTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.SwitchID, swvmTunnel.SwitchPort);
                int vmID = swvmTunnel.VMID;
                operationCenter.createTunnelSW2VM(switchPortPeeronTT, vmID);
            }

            for (Map.Entry<String, Integer> entry : userSW2domSW.entrySet()) {
                Thread t = new Thread(()->{
                    try {
                        String userSW = entry.getKey();//s0
                        String userSWConf = wsOrder.switchConf.get(userSW);
                        Integer domSW = entry.getValue();//s5
                        if (userSWConf == null || userSW == null) {
                            Logger.getAnonymousLogger().severe("Error to map");
                            throw new Exception("Error to map");
                        }
                        switch (userSWConf) {
                            case "OF1.0":
                                operationCenter.addSWitchConf("OF1.0", domSW, wsOrder.controllerIP, wsOrder.controllerPort);
                                break;
                            case "OF1.3":
                                operationCenter.addSWitchConf("OF1.3", domSW, wsOrder.controllerIP, wsOrder.controllerPort);
                                break;
                            default:
                                //URL to download.
                                String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                                if (!userSWConf.matches(urlRegex)) {
                                    Logger.getAnonymousLogger().severe("Wrong URL");
                                    throw new Exception("Wrong URL");
                                }
                                String fileName = System.currentTimeMillis() + ".tar.xz";
                                String filePath = "/var/www/html/SwitchConfStore/" + fileName;
                                FileUtils.copyURLToFile(new URL(userSWConf), new File(filePath));
                                String urlFilePath = "http://202.117.15.79/SwitchConfStore/" + fileName;
                                operationCenter.addSWitchConf(urlFilePath, domSW, wsOrder.controllerIP, wsOrder.controllerPort);
                                break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
                t.start();
                threadsAddConf.add(t);
            }

            for (Thread thread : threadsAddConf) {
                thread.join();
            }

            for (Integer switchID : switchesIDs) {
                operationCenter.powerOnSwitch(switchID);
            }

            // Start up one VM.
            for (Integer vmID : vmIDs) {
                Thread t = new Thread(() -> {
                    try {
                        operationCenter.powerOnVM(vmID);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
                t.start();
                threadsStartVM.add(t);
            }

            WTOrder wtOrder = new WTOrder(
                    wsOrder.orderID,
                    wsOrder.username,
                    wsOrder.switchIDs,
                    wsOrder.vmIDs,
                    wsOrder.beginTime,
                    wsOrder.endTime,
                    wsOrder.topology,
                    wsOrder.switchConf,
                    wsOrder.controllerIP,
                    wsOrder.controllerPort,
                    swswTunnels,
                    swvmTunnels,
                    userSW2domSW,
                    userVM2domVM
            );

            waitingStartQueue.deleteOrderByID(wsOrder.orderID);
            waitingTermQueue.newOrder(wtOrder);

            //Wait for all the thread to complete.
            for(Thread t : threadsStartVM){
                t.join();
            }
        }
    }


    private List<SWSWTunnel> getswswTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSw, Map<String, Integer> userVM2domVM) {
        List<SWSWTunnel> swswTunnels = new ArrayList<>();

        for (Map.Entry<String, String> entry : topology.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            String userID = key.split(":")[0]; //"s0", "h0"
            String userPeerID = value.split(":")[0]; //"s0", "h0"

            if (userID.substring(0, 1).equals("h") || userPeerID.substring(0, 1).equals("h"))
                continue;

            int domIDint = userSW2domSw.get(userID);
            int domPeerIDint = userSW2domSw.get(userPeerID);

            int userSwitchPort = Integer.parseInt(key.split(":")[1]); //0
            int userPeerSwitchPort = Integer.parseInt(value.split(":")[1]); //1


            SWSWTunnel tunnel = new SWSWTunnel(domIDint, userSwitchPort, domPeerIDint, userPeerSwitchPort);
            swswTunnels.add(tunnel);
        }
        return swswTunnels;
    }

    private List<SWVMTunnel> getswvmTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSW, Map<String, Integer> userVM2domVM) throws Exception {
        List<SWVMTunnel> swvmTunnels = new ArrayList<>();

        for (Map.Entry<String, String> entry : topology.entrySet()) {
            // entry : h0:1---> s1:0
            // entry : s0:1---> h1:0

            String key = entry.getKey();
            String value = entry.getValue();

            String userID = key.split(":")[0]; //"s0", "h0"
            String userPeerID = value.split(":")[0]; //"s0", "h0"

            // Ignore Switch to Switch Link
            if (userID.substring(0, 1).equals("s") && userPeerID.substring(0, 1).equals("s"))
                continue;

            String userVmId;
            String vmPort;
            String userSwitchId;
            String switchPort;
            if (key.substring(0, 1).equals("h")) {
                // key = "h0:1"
                userVmId = key.split(":")[0];
                vmPort = key.split(":")[1];
                //value = "s1:0"
                userSwitchId = value.split(":")[0];
                switchPort = value.split(":")[1];
            } else {
                //key = "s0:1"
                userSwitchId = key.split(":")[0];
                switchPort = key.split(":")[1];
                //value = "s1:0"
                userVmId = value.split(":")[0];
                vmPort = value.split(":")[1];
            }
            // Find the true Id.
            try {
                int domVmId = userVM2domVM.get(userVmId);
                int domVmPort = Integer.parseInt(vmPort);
                int domSwitchId = userSW2domSW.get(userSwitchId);
                int domSwitchPort = Integer.parseInt(switchPort);
                SWVMTunnel tunnel = new SWVMTunnel(domSwitchId, domSwitchPort, domVmId, domVmPort);
                swvmTunnels.add(tunnel);

            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new Exception("can find the vmId");
            }
        }
        return swvmTunnels;
    }


    @Override
    public void run() {
        while (true) try {
            List<WSOrder> list = checkOrders();
            if (list.size() != 0) {
                startSlice(list);
            }
            Thread.sleep(3000);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
