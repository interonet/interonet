package org.interonet.gdm.Core;

import org.interonet.gdm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.gdm.Core.Utils.DayTime;
import org.interonet.gdm.OperationCenter.IOperationCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class WSQueueManager implements Runnable {
    WaitingStartQueue waitingStartQueue;
    WaitingTermQueue waitingTermQueue;
    IOperationCenter operationCenter;
    IConfigurationCenter configurationCenter;
    GDMCore core;

    Logger logger;

    public WSQueueManager(GDMCore core, WaitingStartQueue wsQueue, WaitingTermQueue wtQueue, IOperationCenter operationCenter) {
        this.core = core;
        this.waitingStartQueue = wsQueue;
        this.waitingTermQueue = wtQueue;
        this.operationCenter = operationCenter;
        configurationCenter = core.getConfigurationCenter();
        logger = LoggerFactory.getLogger(WSQueueManager.class);

    }

    public List<WSOrder> checkOrders() {
        List<WSOrder> list;
        DayTime currentTime = new DayTime(new SimpleDateFormat("HH:mm").format(new Date()));
        list = waitingStartQueue.getTimeReadyWSOrders(currentTime);
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

            /*
            *
            * Comment these lines for the fuck onetswitch30's eth0 driver in u-boot-meshsr.
            * By Samuel, Dec, 14
            *
            * */

            //for (Map.Entry<String, Integer> entry : userSW2domSW.entrySet()) {
            //    Thread t = new Thread(() -> {
            //        try {
            //            String userSW = entry.getKey();//s0
            //            String userSWConf = wsOrder.switchConf.get(userSW);
            //            Integer domSW = entry.getValue();//s5
            //            if (userSWConf == null || userSW == null) {
            //                logger.error("Error to map:" + "userSWConf=" + userSWConf + ", userSW=" + userSW);
            //                throw new Exception("Error to map");
            //            }
            //            switch (userSWConf) {
            //                case "OF1.0":
            //                    operationCenter.addSwitchConf("OF1.0", domSW, wsOrder.controllerIP, wsOrder.controllerPort);
            //                    break;
            //                case "OF1.3":
            //                    operationCenter.addSwitchConf("OF1.3", domSW, wsOrder.controllerIP, wsOrder.controllerPort);
            //                    break;
            //                default://custom
            //                    Map<String, String> customSwitchConf = wsOrder.customSwitchConf.get(userSW);
            //                    if (customSwitchConf == null) {
            //                        logger.error("There should be #" + userSW + " , But this number is not in the custom switch conf.");
            //                        throw new Exception("Incomplete Custom Switch Configuration");
            //                    }
            //
            //                    /*
            //                    *   customSwitchConf should be like this.
            //                    *
            //                    *  {
            //                    *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
            //                    *     "system-bit": "http://202.117.15.79/ons_bak/system.bit",
            //                    *     "uImage": "http://202.117.15.79/ons_bak/uImage",
            //                    *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
            //                    *  }
            //                    *
            //                    * */
            //
            //                    String rootFsUrl = customSwitchConf.get("root-fs");
            //                    String systemBitUrl = customSwitchConf.get("system-bit");
            //                    String uImageUrl = customSwitchConf.get("uImage");
            //                    String deviceTreeUrl = customSwitchConf.get("device-tree");
            //
            //                    //Url Validation.
            //                    String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            //                    if (!rootFsUrl.matches(urlRegex) ||
            //                            !systemBitUrl.matches(urlRegex) ||
            //                            !uImageUrl.matches(uImageUrl) ||
            //                            !deviceTreeUrl.matches(urlRegex)) {
            //                        logger.error("rootFsUrl = " + rootFsUrl);
            //                        logger.error("systemBitUrl = " + systemBitUrl);
            //                        logger.error("uImageUrl = " + uImageUrl);
            //                        logger.error("deviceTreeUrl = " + deviceTreeUrl);
            //                        throw new Exception("Wrong URL");
            //                    }
            //
            //                    //RootFS
            //                    String rootFsFileName = System.currentTimeMillis() + "-rootfs.tar.xz";
            //                    String rootFsFilePath = "/var/www/html/SwitchConfStore/" + rootFsFileName;
            //                    FileUtils.copyURLToFile(new URL(rootFsUrl), new File(rootFsFilePath));
            //                    String rootFsUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + rootFsFileName;
            //
            //                    //system-bit
            //                    String bootGenUtilWorkingDir = configurationCenter.getConf("BootgenUtilWorkingDir");
            //                    FileUtils.copyURLToFile(new URL(systemBitUrl), new File(bootGenUtilWorkingDir + "onetswitch_top.bit"));
            //                    //Generate actual boot-bin using bootgen utils.
            //                    Runtime.getRuntime().exec("rm boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                    Runtime.getRuntime().exec("bootgen -w -image output.bif -o boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                    if (!new File(bootGenUtilWorkingDir + "boot.bin").exists()) {
            //                        logger.error("bootgen build error");
            //                        Runtime.getRuntime().exec("rm boot.bin", null, new File(bootGenUtilWorkingDir)).waitFor();
            //                        throw new Exception("bootgen build error.");
            //                    }
            //                    String bootBinFileName = System.currentTimeMillis() + "-boot.bin";
            //                    String bootBinFilePath = "/var/www/html/SwitchConfStore/" + bootBinFileName;
            //                    FileUtils.copyFile(new File(bootGenUtilWorkingDir + "boot.bin"), new File(bootBinFilePath));
            //                    String bootBinUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + bootBinFileName;
            //
            //                    //uImage
            //                    String uImageFileName = System.currentTimeMillis() + "-uImage";
            //                    String uImageFilePath = "/var/www/html/SwitchConfStore/" + uImageFileName;
            //                    FileUtils.copyURLToFile(new URL(uImageUrl), new File(uImageFilePath));
            //                    String uImageUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + uImageFileName;
            //
            //                    //Device Tree
            //                    String deviceTreeFileName = System.currentTimeMillis() + "-devicetree.dtb";
            //                    String deviceTreeFilePath = "/var/www/html/SwitchConfStore/" + deviceTreeFileName;
            //                    FileUtils.copyURLToFile(new URL(deviceTreeUrl), new File(deviceTreeFilePath));
            //                    String deviceTreeUrlFilePath = "http://202.117.15.79/SwitchConfStore/" + deviceTreeFileName;
            //
            //                    Map<String, String> customSwitchConfGDM = new HashMap<>();
            //                    customSwitchConfGDM.put("root-fs", rootFsUrlFilePath);
            //                    customSwitchConfGDM.put("boot-bin", bootBinUrlFilePath);
            //                    customSwitchConfGDM.put("uImage", uImageUrlFilePath);
            //                    customSwitchConfGDM.put("device-tree", deviceTreeUrlFilePath);
            //
            //                    operationCenter.addSwitchConf(customSwitchConfGDM, domSW, wsOrder.controllerIP, wsOrder.controllerPort);
            //
            //                    break;
            //            }
            //        } catch (Throwable e) {
            //            e.printStackTrace();
            //        }
            //    });
            //    t.start();
            //    threadsAddConf.add(t);
            //}
            //
            //for (Thread thread : threadsAddConf) {
            //    thread.join();
            //}

            /*
            *
            * Comment these lines for the fuck box to fix the power system.
            * by Samuel, Dec, 14
            *
            * */

            //for (Integer switchID : switchesIDs) {
            //    operationCenter.powerOnSwitch(switchID);
            //}

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
            for (Thread t : threadsStartVM) {
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

            //FIXME if userId is null, and the whole program will be crashed.
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
