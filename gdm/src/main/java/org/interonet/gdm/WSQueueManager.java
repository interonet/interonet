package org.interonet.gdm;

import java.text.SimpleDateFormat;
import java.util.*;

public class WSQueueManager implements Runnable {
    WaitingStartQueue waitingStartQueue;
    WaitingTermQueue waitingTermQueue;
    OperationCenter operationCenter;
    ConfigurationCenter configurationCenter;

    public WSQueueManager(WaitingStartQueue wsQueue, WaitingTermQueue wtQueue, OperationCenter operationCenter) {
        this.waitingStartQueue = wsQueue;
        this.waitingTermQueue = wtQueue;
        this.operationCenter = operationCenter;
        configurationCenter = new ConfigurationCenter();
    }

    synchronized public List<WSOrder> checkOrders() {
        List<WSOrder> list = new ArrayList<WSOrder>();
        DayTime currentTime = new DayTime(new SimpleDateFormat("HH:mm").format(new Date()));

        for (WSOrder wsOrder : waitingStartQueue.getQueue()) {
            if (new DayTime(wsOrder.beginTime).earlyThan(currentTime))
                list.add(wsOrder);
        }
        return list;
    }

    synchronized public void startSlice(List<WSOrder> wsOrderList) throws Throwable {
        for (WSOrder wsOrder : wsOrderList) {
            List<Integer> switchesIDs = wsOrder.switchIDs;
            List<Integer> vmIDs = wsOrder.vmIDs;
            Map<String, Integer> userSW2domSW = new HashMap<String, Integer>();
            Map<String, Integer> userVM2domVM = new HashMap<String, Integer>();
            for (int i = 0; i < switchesIDs.size(); i++)
                userSW2domSW.put("s" + new Integer(i).toString(), switchesIDs.get(i));

            for (int i = 0; i < vmIDs.size(); i++)
                userVM2domVM.put("h" + new Integer(i).toString(), vmIDs.get(i));

            List<SWSWTunnel> swswTunnels = getswswTunnel(wsOrder.topology, userSW2domSW, userVM2domVM);
            List<SWVMTunnel> swvmTunnels = getswvmTunnel(wsOrder.topology, userSW2domSW, userVM2domVM);

            System.out.println("*************************************Starting a Slice***********************************");
            for (SWSWTunnel swswT : swswTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.SwitchID, swswT.SwitchIDPortNum);
                int athrSwitchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.PeerSwitchID, swswT.PeerSwitchIDPortNum);
                operationCenter.createTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
            }

            for (SWVMTunnel swvmTunnel : swvmTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.SwitchID, swvmTunnel.SwitchPort);
                int peerVMPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.VMID, swvmTunnel.VMPort);
                operationCenter.createTunnelSW2VM(switchPortPeeronTT, peerVMPortPeeronTT);
            }

            for (Integer switchID : switchesIDs) {
                operationCenter.addSWitchConf(switchID, wsOrder.controllerIP, wsOrder.controllerPort);
            }

            for (Integer switchID : switchesIDs) {
                operationCenter.powerOnSwitch(switchID);
            }

            for (Integer vmID : vmIDs) {
                operationCenter.powerOnVM(vmID);
            }
            System.out.println("*************************************Starting a Slice***********************************\n\n");

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
                    swvmTunnels);

            waitingStartQueue.deleteOrderByID(wsOrder.orderID);
            waitingTermQueue.newOrder(wtOrder);
        }
    }


    synchronized private List<SWSWTunnel> getswswTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSw, Map<String, Integer> userVM2domVM) {
        List<SWSWTunnel> swswTunnels = new ArrayList<SWSWTunnel>();

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

    synchronized private List<SWVMTunnel> getswvmTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSW, Map<String, Integer> userVM2domVM) {
        List<SWVMTunnel> swvmTunnels = new ArrayList<SWVMTunnel>();

        for (Map.Entry<String, String> entry : topology.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            String userID = key.split(":")[0]; //"s0", "h0"
            String userPeerID = value.split(":")[0]; //"s0", "h0"

            if (userID.substring(0, 1).equals("s") && userPeerID.substring(0, 1).equals("s"))
                continue;

            Integer domIDint = userID.substring(0, 1).equals("s") ? userSW2domSW.get(userID) : userVM2domVM.get(userID);
            Integer domPeerIDint = userPeerID.substring(0, 1).equals("s") ? userSW2domSW.get(userPeerID) : userVM2domVM.get(userPeerID);

            int userSwitchPort = Integer.parseInt(key.split(":")[1]); //0
            int userPeerSwitchPort = Integer.parseInt(value.split(":")[1]); //1


            SWVMTunnel tunnel = new SWVMTunnel(domIDint, userSwitchPort, domPeerIDint, userPeerSwitchPort);
            swvmTunnels.add(tunnel);
        }
        return swvmTunnels;
    }


    @Override
    public void run() {
        while (true) {
            try {
                List<WSOrder> list = checkOrders();
                if (list.size() != 0) {
                    startSlice(list);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

}
