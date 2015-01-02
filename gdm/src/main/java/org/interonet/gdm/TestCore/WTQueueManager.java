package org.interonet.gdm.TestCore;

import org.interonet.gdm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.gdm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.gdm.OperationCenter.IOperationCenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WTQueueManager implements Runnable {
    WaitingStartQueue waitingStartQueue;
    WaitingTermQueue waitingTermQueue;
    IOperationCenter operationCenter;
    IConfigurationCenter configurationCenter;

    public WTQueueManager(WaitingStartQueue wsQueue, WaitingTermQueue wtQueue, IOperationCenter operationCenter) {
        this.waitingStartQueue = wsQueue;
        this.waitingTermQueue = wtQueue;
        this.operationCenter = operationCenter;
        configurationCenter = new ConfigurationCenter();
    }

    synchronized private List<WTOrder> checkOrders() {
        List<WTOrder> list = new ArrayList<WTOrder>();
        DayTime currentTime = new DayTime(new SimpleDateFormat("HH:mm").format(new Date()));

        for (WTOrder wtOrder : waitingTermQueue.getQueue()) {
            if (new DayTime(wtOrder.endTime).earlyThan(currentTime))
                list.add(wtOrder);
        }
        return list;
    }

    synchronized private void stopSlice(List<WTOrder> wtOrderList) throws Throwable {
        for (WTOrder wtOrder : wtOrderList) {
            List<Integer> switchesIDs = wtOrder.switchIDs;
            List<Integer> vmIDs = wtOrder.vmIDs;
            List<SWSWTunnel> swswTunnels = wtOrder.swswTunnel;
            List<SWVMTunnel> swvmTunnels = wtOrder.swvmTunnel;

            System.out.println("*************************************Stopping a Slice***********************************");
            for (SWSWTunnel swswT : swswTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.SwitchID, swswT.SwitchIDPortNum);
                int athrSwitchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.PeerSwitchID, swswT.PeerSwitchIDPortNum);
                operationCenter.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
            }

            for (SWVMTunnel swvmTunnel : swvmTunnels) {
                int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.SwitchID, swvmTunnel.SwitchPort);
                int peerVMPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.VMID, swvmTunnel.VMPort);
                operationCenter.deleteTunnelSW2VM(switchPortPeeronTT, peerVMPortPeeronTT);
            }

            for (Integer switchID : switchesIDs) {
                operationCenter.deleteSWitchConf(switchID);
            }

            for (Integer switchID : switchesIDs) {
                operationCenter.powerOffSwitch(switchID);
            }

            for (Integer vmID : vmIDs) {
                operationCenter.powerOffVM(vmID);
            }

            waitingTermQueue.deleteOrderByID(wtOrder.sliceID);
            System.out.println("*************************************Stopping a Slice***********************************\n\n\n");
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                List<WTOrder> list = checkOrders();
                if (list.size() != 0) {
                    stopSlice(list);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }
}
