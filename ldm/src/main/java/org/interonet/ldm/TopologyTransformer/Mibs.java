package org.interonet.ldm.TopologyTransformer;

import org.snmp4j.smi.OID;

public final class Mibs {
    public final OID vtpVlanState = new OID("1.3.6.1.4.1.9.9.46.1.3.1.1.2");
    public final OID vtpVlanEditOperation = new OID("1.3.6.1.4.1.9.9.46.1.4.1.1.1");
    public final OID vtpVlanEditBufferOwner = new OID("1.3.6.1.4.1.9.9.46.1.4.1.1.3");
    public final OID vtpVlanEditState = new OID("1.3.6.1.4.1.9.9.46.1.4.2.1.2");
    public final OID vtpVlanEditRowStatus = new OID("1.3.6.1.4.1.9.9.46.1.4.2.1.11");
    public final OID vtpVlanEditType = new OID("1.3.6.1.4.1.9.9.46.1.4.2.1.3");
    public final OID vtpVlanEditName = new OID("1.3.6.1.4.1.9.9.46.1.4.2.1.4");
    public final OID vtpVlanEditDot10Said = new OID("1.3.6.1.4.1.9.9.46.1.4.2.1.6");
    public final OID vtpVlanApplyStatus = new OID("1.3.6.1.4.1.9.9.46.1.4.1.1.2");
    public final OID vmVlan = new OID("1.3.6.1.4.1.9.9.68.1.2.2.1.2");
    public final OID ifAdminStatus = new OID("1.3.6.1.2.1.2.2.1.7");
}