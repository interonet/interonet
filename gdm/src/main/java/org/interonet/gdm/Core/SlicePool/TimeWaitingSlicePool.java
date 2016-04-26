package org.interonet.gdm.Core.SlicePool;

import org.interonet.gdm.Core.Slice;

public class TimeWaitingSlicePool extends SlicePool {
    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.TIME_WAITING && slicePool.add(slice);
    }
}
