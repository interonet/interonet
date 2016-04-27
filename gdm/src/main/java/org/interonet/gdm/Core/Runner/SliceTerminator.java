package org.interonet.gdm.Core.Runner;

import org.interonet.gdm.Core.FuturePool.LDMTaskTerminatedFuturePool;
import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.TerminatableSlicePool;
import org.interonet.gdm.Core.SlicePool.TerminatedWaitingPool;
import org.interonet.gdm.LDMConnector.LDMConnector;
import org.interonet.gdm.LDMConnector.LDMTask.LDMStopTask;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.FutureTask;

public class SliceTerminator implements Runnable {
    private LDMConnector ldmConnector;

    private TerminatableSlicePool terminatableSlicePool;
    private TerminatedWaitingPool terminatedWaitingSlicePool;
    private LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool;

    private Logger logger = LoggerFactory.getLogger(SliceTerminator.class);

    public SliceTerminator(LDMConnector ldmConnector) {
        this.ldmConnector = ldmConnector;
    }

    @Override
    public void run() {
        List<Slice> list = terminatableSlicePool.consumeAllSlice();
        //logger.debug("terminatableSlicePool.consumeAllSlice()");
        for (Slice slice : list) {
            List<Integer> switchesIDs = slice.getSwitchIdList();
            List<Integer> vmIDs = slice.getVmIdList();
            List<SwitchToSwitchTunnel> switchToSwitchTunnels = slice.getSwitchToSwitchTunnelList();
            List<SwitchToVMTunnel> switchToVMTunnels = slice.getSwitchToVMTunnelList();

            LDMStopTask ldmStopTask = new LDMStopTask(slice);
            FutureTask<LDMTaskReturn> futureTask = ldmConnector.submit(ldmStopTask);
            logger.info("submit slice stopping task to ldmConnector: sliceId=" + slice.getId());
            ldmTaskTerminatedFuturePool.submit(futureTask);

            slice.setStatus(Slice.SliceStatus.TERMINATED_WAITING);
            logger.info("submit slice to TERMINATED_WAITING: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
            terminatedWaitingSlicePool.submit(slice);

        }

    }

    public void setTerminatableSlicePool(TerminatableSlicePool terminatableSlicePool) {
        this.terminatableSlicePool = terminatableSlicePool;
    }

    public void setTerminatedWaitingSlicePool(TerminatedWaitingPool terminatedWaitingSlicePool) {
        this.terminatedWaitingSlicePool = terminatedWaitingSlicePool;
    }

    public void setLdmTaskTerminatedFuturePool(LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool) {
        this.ldmTaskTerminatedFuturePool = ldmTaskTerminatedFuturePool;
    }
}