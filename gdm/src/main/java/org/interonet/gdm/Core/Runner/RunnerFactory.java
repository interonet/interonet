package org.interonet.gdm.Core.Runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class RunnerFactory implements ThreadFactory {
    private Logger logger = LoggerFactory.getLogger(RunnerExceptionHandler.class);

    @Override
    public Thread newThread(Runnable target) {
        final Thread thread = new Thread(target);
        thread.setName(target.getClass().getCanonicalName());
        thread.setUncaughtExceptionHandler(new RunnerExceptionHandler());
        return thread;
    }

    public static class RunnerExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Logger logger = LoggerFactory.getLogger(RunnerExceptionHandler.class);

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("LDMTaskCallExceptionHandler->Thread:" + t.getName(), e);
        }
    }
}
