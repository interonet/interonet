package org.interonet.gdm.Core.Runner;

import org.interonet.gdm.Core.FuturePool.LDMTaskRunningFuturePool;
import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.RunnableSlicePool;
import org.interonet.gdm.Core.SlicePool.RunningWaitingSlicePool;
import org.interonet.gdm.LDMConnector.LDMConnector;
import org.interonet.gdm.LDMConnector.LDMTask.LDMStartTask;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.interonet.gdm.LDMConnector.SwitchToSwitchTunnel;
import org.interonet.gdm.LDMConnector.SwitchToVMTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class SliceRunner implements Runnable {
    private Logger logger = LoggerFactory.getLogger(SliceRunner.class);
    private LDMConnector ldmConnector;
    private RunnableSlicePool runnableSlicePool;
    private RunningWaitingSlicePool runningWaitingSlicePool;
    private LDMTaskRunningFuturePool ldmTaskRunningFuturePool;

    public SliceRunner() {
    }

    public SliceRunner(LDMConnector ldmConnector) {
        this.ldmConnector = ldmConnector;
    }

    private List<SwitchToSwitchTunnel> getswswTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSw, Map<String, Integer> userVM2domVM) {
        List<SwitchToSwitchTunnel> switchToSwitchTunnels = new ArrayList<>();

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


            SwitchToSwitchTunnel tunnel = new SwitchToSwitchTunnel(domIDint, userSwitchPort, domPeerIDint, userPeerSwitchPort);
            switchToSwitchTunnels.add(tunnel);
        }
        return switchToSwitchTunnels;
    }

    private List<SwitchToVMTunnel> getswvmTunnel(Map<String, String> topology, Map<String, Integer> userSW2domSW, Map<String, Integer> userVM2domVM) {
        List<SwitchToVMTunnel> switchToVMTunnels = new ArrayList<>();

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
                SwitchToVMTunnel tunnel = new SwitchToVMTunnel(domSwitchId, domSwitchPort, domVmId, domVmPort);
                switchToVMTunnels.add(tunnel);

            } catch (NullPointerException e) {
                return switchToVMTunnels;
            }
        }
        return switchToVMTunnels;
    }

    @Override
    public void run() {
        try {
            List<Slice> list = runnableSlicePool.consumeAllSlice();
            for (Slice slice : list) {
                List<Integer> switchesIDs = slice.getSwitchIdList();
                List<Integer> vmIDs = slice.getVmIdList();
                Map<String, Integer> userSW2domSW = new HashMap<>();
                Map<String, Integer> userVM2domVM = new HashMap<>();
                for (int i = 0; i < switchesIDs.size(); i++)
                    userSW2domSW.put("s" + Integer.toString(i), switchesIDs.get(i));

                for (int i = 0; i < vmIDs.size(); i++)
                    userVM2domVM.put("h" + Integer.toString(i), vmIDs.get(i));

                List<SwitchToSwitchTunnel> switchToSwitchTunnels = getswswTunnel(slice.getTopology(), userSW2domSW, userVM2domVM);
                List<SwitchToVMTunnel> switchToVMTunnels = getswvmTunnel(slice.getTopology(), userSW2domSW, userVM2domVM);

                if (switchToSwitchTunnels.size() != 0 && switchToVMTunnels.size() == 0) {
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                    slice.setException(Slice.SliceException.WRONG_TOPOLOGY_FORMAT);
                    continue;
                }
                slice.setUserSW2domSW(userSW2domSW);
                slice.setUserVM2domVM(userVM2domVM);
                slice.setSwitchToSwitchTunnelList(switchToSwitchTunnels);
                slice.setSwitchToVMTunnelList(switchToVMTunnels);

                LDMStartTask ldmStartTask = new LDMStartTask(slice);
                FutureTask<LDMTaskReturn> futureTask = ldmConnector.submit(ldmStartTask);

                logger.info("submit slice starting task to ldmConnector: sliceId=" + slice.getId());
                ldmTaskRunningFuturePool.submit(futureTask);

                logger.info("submit slice to RUNNING_WAITING: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
                slice.setStatus(Slice.SliceStatus.RUNNING_WAITING);
                runningWaitingSlicePool.submit(slice);
            }
        } catch (Exception e) {
            logger.error("InterruptedException:", e);
            throw e;
        }
    }

    public void setRunnableSlicePool(RunnableSlicePool runnableSlicePool) {
        this.runnableSlicePool = runnableSlicePool;
    }

    public void setRunningWaitingSlicePool(RunningWaitingSlicePool runningWaitingSlicePool) {
        this.runningWaitingSlicePool = runningWaitingSlicePool;
    }

    public void setLdmTaskRunningFuturePool(LDMTaskRunningFuturePool ldmTaskRunningFuturePool) {
        this.ldmTaskRunningFuturePool = ldmTaskRunningFuturePool;
    }
}
