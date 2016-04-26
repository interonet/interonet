package org.interonet.gdm.LDMConnector;

import org.interonet.gdm.LDMConnector.LDMTask.LDMStartTask;
import org.interonet.gdm.LDMConnector.LDMTask.LDMStopTask;
import org.interonet.gdm.LDMConnector.LDMTask.LDMTaskReturn;
import org.interonet.gdm.LDMConnector.LDMTaskCall.LDMTaskCallFactory;
import org.interonet.gdm.LDMConnector.LDMTaskCall.LDMTaskStartCall;
import org.interonet.gdm.LDMConnector.LDMTaskCall.LDMTaskStopCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class LDMConnector {
    Logger logger = LoggerFactory.getLogger(LDMConnector.class);
    private LDMCalls ldmCalls;
    private ExecutorService threadPool;

    public LDMConnector(URL url, int timeout, boolean debug) {
        threadPool = Executors.newCachedThreadPool(new LDMTaskCallFactory());
        ldmCalls = new LDMCalls(url, timeout, debug);
    }

    public FutureTask<LDMTaskReturn> submit(LDMStartTask ldmStartTask) {
        LDMTaskStartCall ldmTaskStartCall = new LDMTaskStartCall(ldmStartTask, ldmCalls);
        FutureTask<LDMTaskReturn> futureTask = new FutureTask<>(ldmTaskStartCall);
        threadPool.submit(futureTask);
        return futureTask;

    }

    public FutureTask<LDMTaskReturn> submit(LDMStopTask ldmStopTask) {
        LDMTaskStopCall ldmTaskStopCall = new LDMTaskStopCall(ldmStopTask, ldmCalls);
        FutureTask<LDMTaskReturn> futureTask = new FutureTask<>(ldmTaskStopCall);
        threadPool.submit(futureTask);
        return futureTask;
    }
}
