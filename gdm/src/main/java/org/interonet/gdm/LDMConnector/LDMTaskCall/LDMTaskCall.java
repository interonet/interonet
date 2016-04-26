package org.interonet.gdm.LDMConnector.LDMTaskCall;

import org.interonet.gdm.LDMConnector.LDMCalls;

public abstract class LDMTaskCall {
    public LDMCalls ldmCalls;

    public LDMTaskCall(LDMCalls ldmCalls) {
        this.ldmCalls = ldmCalls;
    }
}
