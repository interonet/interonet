package org.interonet.gdm.Core.SlicePool;

import org.interonet.gdm.Core.Slice;

public class RunningSlicePool extends SlicePool {
    @Override
    public synchronized boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNING && slicePool.add(slice);
    }
}
