package org.interonet.gdm.Core.SlicePool;

import org.interonet.gdm.Core.Slice;

public class RunnableSlicePool extends SlicePool {
    @Override
    synchronized public boolean submit(Slice slice) {
        return slice.getStatus() == Slice.SliceStatus.RUNNABLE && slicePool.add(slice);
    }
}
