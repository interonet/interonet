package org.interonet.gdm.Core.Runner;

import org.interonet.gdm.Core.FuturePool.LDMTaskTerminatedFuturePool;
import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.TerminatedSlicePool;
import org.interonet.gdm.Core.SlicePool.TerminatedWaitingPool;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SliceTerminatedChecker implements Runnable {
    private LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool;
    private TerminatedWaitingPool terminatedWaitingSlicePool;
    private TerminatedSlicePool terminatedSlicePool;

    private Logger logger = LoggerFactory.getLogger(SliceTerminatedChecker.class);

    @Override
    public void run() {
        try {
            List<FutureTask<LDMTaskReturn>> list = ldmTaskTerminatedFuturePool.consumeAllFinishedTask();
            for (FutureTask<LDMTaskReturn> future : list) {
                if (!future.isDone()) continue;
                LDMTaskReturn ldmTaskReturn = future.get();
                if (!ldmTaskReturn.getSuccess()) {
                    Slice slice = terminatedWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    slice.setException(Slice.SliceException.LDM_TASK_STOP_CALL_TIMEOUT);
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                }
                Slice slice = terminatedWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                logger.info("submit slice to TERMINATED: sliceId=" + slice.getId());
                slice.setStatus(Slice.SliceStatus.TERMINATED);
                terminatedSlicePool.submit(slice);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Thread Exception", e);
        }
    }

    public void setLDMTaskTerminatedFuturePool(LDMTaskTerminatedFuturePool ldmTaskTerminatedFuturePool) {
        this.ldmTaskTerminatedFuturePool = ldmTaskTerminatedFuturePool;
    }

    public void setTerminatedWaitingSlicePool(TerminatedWaitingPool terminatedWaitingSlicePool) {
        this.terminatedWaitingSlicePool = terminatedWaitingSlicePool;
    }

    public void setTerminatedSlicePool(TerminatedSlicePool terminatedSlicePool) {
        this.terminatedSlicePool = terminatedSlicePool;
    }
}
