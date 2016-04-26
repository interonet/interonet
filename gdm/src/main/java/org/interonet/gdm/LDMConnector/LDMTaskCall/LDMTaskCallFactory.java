package org.interonet.gdm.LDMConnector.LDMTaskCall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

public class LDMTaskCallFactory implements ThreadFactory {
    private Logger logger = LoggerFactory.getLogger(LDMTaskCallFactory.class);

    @Override
    public Thread newThread(Runnable target) {
        final Thread thread = new Thread(target);
        thread.setName(target.getClass().getCanonicalName());
        thread.setUncaughtExceptionHandler(new LDMTaskCallExceptionHandler());
        return thread;
    }

    public static class LDMTaskCallExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Logger logger = LoggerFactory.getLogger(LDMTaskCallExceptionHandler.class);

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("LDMTaskCallExceptionHandler->Thread:" + t.getName(), e);
        }
    }
}
