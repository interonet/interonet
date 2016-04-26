package org.interonet.gdm.Core.Runner;


import org.interonet.gdm.Core.Slice;
import org.interonet.gdm.Core.SlicePool.RunnableSlicePool;
import org.interonet.gdm.Core.SlicePool.TimeWaitingSlicePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;

public class SliceBeginTimeChecker implements Runnable {
    private TimeWaitingSlicePool timeWaitingSlicePool;
    private RunnableSlicePool runnableSlicePool;

    private Logger logger = LoggerFactory.getLogger(SliceBeginTimeChecker.class);

    @Override
    public void run() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Slice> list = timeWaitingSlicePool.consumeSliceBeginTimeBeforeThan(now);
        for (Slice slice : list) {
            slice.setStatus(Slice.SliceStatus.RUNNABLE);
            logger.info("submit slice to RUNNABLE: sliceId=" + slice.getId() + " beginTime=" + slice.getBeginTime());
            runnableSlicePool.submit(slice);
        }
    }

    public void setTimeWaitingSlicePool(TimeWaitingSlicePool timeWaitingSlicePool) {
        this.timeWaitingSlicePool = timeWaitingSlicePool;
    }

    public void setRunnableSlicePool(RunnableSlicePool runnableSlicePool) {
        this.runnableSlicePool = runnableSlicePool;
    }
}
