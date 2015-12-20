package org.interonet.gdm.Core;

import org.interonet.gdm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.gdm.Core.Utils.DayTime;
import org.interonet.gdm.OperationCenter.IOperationCenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class WTQueueManager implements Runnable {
    WaitingStartQueue waitingStartQueue;
    WaitingTermQueue waitingTermQueue;
    IOperationCenter operationCenter;
    IConfigurationCenter configurationCenter;
    GDMCore core;

    public WTQueueManager(GDMCore core, WaitingStartQueue wsQueue, WaitingTermQueue wtQueue, IOperationCenter operationCenter) {
        this.core = core;
        this.waitingStartQueue = wsQueue;
        this.waitingTermQueue = wtQueue;
        this.operationCenter = operationCenter;
        configurationCenter = core.getConfigurationCenter();
    }

    private List<WTOrder> checkOrders() {
        DayTime currentTime = new DayTime(new SimpleDateFormat("HH:mm").format(new Date()));
        List<WTOrder> list = waitingTermQueue.getTimeReadyWSOrders(currentTime);
        return list;
    }

    private void stopSlice(WTOrder wtOrder) throws Throwable {

        List<Integer> switchesIDs = wtOrder.switchIDs;
        List<Integer> vmIDs = wtOrder.vmIDs;
        List<SWSWTunnel> swswTunnels = wtOrder.swswTunnel;
        List<SWVMTunnel> swvmTunnels = wtOrder.swvmTunnel;

        for (SWSWTunnel swswT : swswTunnels) {
            int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.SwitchID, swswT.SwitchIDPortNum);
            int athrSwitchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swswT.PeerSwitchID, swswT.PeerSwitchIDPortNum);
            operationCenter.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
        }

        for (SWVMTunnel swvmTunnel : swvmTunnels) {
            int switchPortPeeronTT = configurationCenter.getTopologyTransformerPortFromPeerPort(swvmTunnel.SwitchID, swvmTunnel.SwitchPort);
            int vmID = swvmTunnel.VMID;
            operationCenter.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
        }

            /*
            *
            * Comment these lines for the fuck onetswitch30's eth0 driver in u-boot-meshsr.
            * By Samuel, Dec, 14
            *
            * */

        //for (Integer switchID : switchesIDs) {
        //    operationCenter.deleteSWitchConf(switchID);
        //}

            /*
            *
            * Comment these lines for the fuck box to fix the power system.
            * by Samuel, Dec, 14
            *
            * */
        //for (Integer switchID : switchesIDs) {
        //    operationCenter.powerOffSwitch(switchID);
        //}

        for (Integer vmID : vmIDs) {
            operationCenter.powerOffVM(vmID);
        }
        waitingTermQueue.deleteOrderByID(wtOrder.sliceID);
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<WTOrder> wtOrderList = checkOrders();

                HashSet<Thread> threadsStopSlice = new HashSet<>();
                for (WTOrder wtOrder : wtOrderList) {
                    Thread thread = new Thread(() -> {
                        try {
                            stopSlice(wtOrder);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                    thread.start();
                    threadsStopSlice.add(thread);
                }
                for (Thread t : threadsStopSlice) {
                    t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }
}
