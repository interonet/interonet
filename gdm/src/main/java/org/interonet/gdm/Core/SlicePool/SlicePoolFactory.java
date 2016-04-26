package org.interonet.gdm.Core.SlicePool;

public class SlicePoolFactory {
    public SlicePool getSlicePool(String slicePoolType) {
        if (slicePoolType == null) {
            return null;
        }
        if (slicePoolType.equalsIgnoreCase("TimeWaitingSlicePool")) {
            return new TimeWaitingSlicePool();
        } else if (slicePoolType.equalsIgnoreCase("RunnableSlicePool")) {
            return new RunnableSlicePool();
        } else if (slicePoolType.equalsIgnoreCase("RunningWaitingSlicePool")) {
            return new RunningWaitingSlicePool();
        } else if (slicePoolType.equalsIgnoreCase("RunningSlicePool")) {
            return new RunningSlicePool();
        } else if (slicePoolType.equalsIgnoreCase("TerminatableSlicePool")) {
            return new TerminatableSlicePool();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedWaitingPool")) {
            return new TerminatedWaitingPool();
        } else if (slicePoolType.equalsIgnoreCase("TerminatedSlicePool")) {
            return new TerminatedSlicePool();
        }
        return null;
    }
}
