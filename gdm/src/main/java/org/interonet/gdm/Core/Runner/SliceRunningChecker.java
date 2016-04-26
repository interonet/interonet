package org.interonet.gdm.Core.Runner;

import org.interonet.gdm.Core.FuturePool.LDMTaskRunningFuturePool;
import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.RunningSlicePool;
import org.interonet.gdm.Core.SlicePool.RunningWaitingSlicePool;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SliceRunningChecker implements Runnable {
    private LDMTaskRunningFuturePool ldmTaskRunningFuturePool;
    private RunningWaitingSlicePool runningWaitingSlicePool;
    private RunningSlicePool runningSlicePool;

    private Logger logger = LoggerFactory.getLogger(SliceRunningChecker.class);

    @Override
    public void run() {
        try {
            List<FutureTask<LDMTaskReturn>> list = ldmTaskRunningFuturePool.consumeAllFinishedTask();
            for (FutureTask<LDMTaskReturn> future : list) {
                LDMTaskReturn ldmTaskReturn = future.get();
                if (!ldmTaskReturn.getSuccess()) {
                    Slice slice = runningSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                    slice.setException(Slice.SliceException.LDM_TASK_START_CALL_TIMEOUT);
                    slice.setStatus(Slice.SliceStatus.TERMINATED);
                }
                Slice slice = runningWaitingSlicePool.consumeBySliceId(ldmTaskReturn.getSliceId());
                slice.setStatus(Slice.SliceStatus.RUNNING);
                logger.info("submit slice to RUNNING: sliceId=" + slice.getId());
                runningSlicePool.submit(slice);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Thread Exception", e);
        }
    }

    public void setLDMTaskRunningFuturePool(LDMTaskRunningFuturePool ldmTaskRunningFuturePool) {
        this.ldmTaskRunningFuturePool = ldmTaskRunningFuturePool;
    }

    public void setRunningWaitingSlicePool(RunningWaitingSlicePool runningWaitingSlicePool) {
        this.runningWaitingSlicePool = runningWaitingSlicePool;
    }

    public void setRunningSlicePool(RunningSlicePool runningSlicePool) {
        this.runningSlicePool = runningSlicePool;
    }
}
