package org.interonet.gdm.Core.Runner;

import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.RunningSlicePool;
import org.interonet.gdm.Core.SlicePool.TerminatableSlicePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;


public class SliceEndTimeChecker implements Runnable {
    private RunningSlicePool runningSlicePool;
    private TerminatableSlicePool terminatableSlicePool;

    private Logger logger = LoggerFactory.getLogger(SliceEndTimeChecker.class);

    public void setRunningSlicePool(RunningSlicePool runningSlicePool) {
        this.runningSlicePool = runningSlicePool;
    }

    public void setTerminatableSlicePool(TerminatableSlicePool terminatableSlicePool) {
        this.terminatableSlicePool = terminatableSlicePool;
    }

    @Override
    public void run() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = runningSlicePool.consumeSliceEndTimeBefore(now);
        for (Slice slice : list) {
            logger.info("submit slice to TERMINATABLE: sliceId=" + slice.getId() + ", endTime=" + slice.getEndTime());
            slice.setStatus(Slice.SliceStatus.TERMINATABLE);
            terminatableSlicePool.submit(slice);
        }
    }
}
