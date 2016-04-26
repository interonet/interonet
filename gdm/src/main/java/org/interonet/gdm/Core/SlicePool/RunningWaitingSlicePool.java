package org.interonet.gdm.Core.SlicePool;

import org.interonet.gdm.Core.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RunningWaitingSlicePool extends SlicePool {
    private Logger logger = LoggerFactory.getLogger(RunningWaitingSlicePool.class);

    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNING_WAITING && slicePool.add(slice);
    }
}
