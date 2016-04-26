package org.interonet.gdm.Core.FuturePool;

import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

public class LDMTaskFuturePool {
    Logger logger = LoggerFactory.getLogger(LDMTaskFuturePool.class);
    private List<FutureTask<LDMTaskReturn>> futureTaskList = new ArrayList<>();

    synchronized public boolean submit(FutureTask<LDMTaskReturn> futureTask) {
        return futureTaskList.add(futureTask);
    }

    synchronized public List<FutureTask<LDMTaskReturn>> consumeAllFinishedTask() {
        List<FutureTask<LDMTaskReturn>> list = new ArrayList<>();
        for (FutureTask<LDMTaskReturn> futureTask : futureTaskList) {
            if (futureTask.isDone())
                list.add(futureTask);
        }
        futureTaskList.removeAll(list);
        return list;
    }

    synchronized public int getPoolSize() {
        return futureTaskList.size();
    }
}
