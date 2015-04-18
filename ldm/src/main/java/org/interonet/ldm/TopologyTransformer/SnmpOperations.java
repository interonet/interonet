package org.interonet.ldm.TopologyTransformer;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.util.Map;

public class SnmpOperations {
    private final String rocommunity = "public";
    private final String rwcommunity = "private";
    private final String address = "192.168.2.2/161";
    private final int retries = 1;
    private final int timeout = 1000;
    private final int snmpVersion = SnmpConstants.version2c;
    private Address targetAddress;
    private CommunityTarget target;
    private TransportMapping transport;
    private Snmp snmp;

    public SnmpOperations() throws Exception {
        targetInit();
        snmpInit();
    }

    private void targetInit() {
        targetAddress = new UdpAddress(address);
        target = new CommunityTarget();
        target.setAddress(targetAddress);
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setVersion(snmpVersion);
    }

    private void snmpInit() throws Exception {
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

//    public void snmpGet(OID targetOid, Map<OID, Variable> status) throws Exception {
//        status.clear();
//        target.setCommunity(new OctetString(rocommunity));
//        PDU request = new PDU();
//        request.setType(PDU.GET);
//        request.add(new VariableBinding(targetOid));
//        ResponseEvent responseEvent = snmp.send(request, target);
//        if (responseEvent != null && responseEvent.getResponse() != null) {
//            PDU response = responseEvent.getResponse();
//            if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
//                status.put(targetOid, response.getVariableBindings().firstElement().getVariable());
//            } else {
//                throw new Exception("Error in response when snmpget: " + targetOid.toString());
//            }
//        } else {
//            throw new Exception("Error in connection to " + address + " when snmpget");
//        }
//    }

    public void snmpWalk(OID targetOid, Map<OID, Variable> status) throws Exception {
        status.clear();
        OID currentOid = new OID(targetOid);
        String rootOid = currentOid.toString();
        target.setCommunity(new OctetString(rocommunity));
        do {
            PDU request = new PDU();
            request.setType(PDU.GETNEXT);
            request.add(new VariableBinding(currentOid));
            ResponseEvent responseEvent = snmp.send(request, target);
            if (responseEvent != null && responseEvent.getResponse() != null) {
                PDU response = responseEvent.getResponse();
                if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
                    VariableBinding variableBinding = (VariableBinding) response.getVariableBindings().firstElement();
                    currentOid = variableBinding.getOid();
                    if (currentOid.toString().contains(rootOid)) {
                        status.put(currentOid, variableBinding.getVariable());
                    } else break;
                } else {
                    throw new Exception("Error in response when snmpwalk: " + targetOid.toString());
                }
            } else {
                throw new Exception("Error in connection to " + address + " when snmpwalk");
            }
        } while (currentOid.toString().contains(rootOid));
    }

    public void snmpSet(OID targetOid, Variable value) throws Exception {
        target.setCommunity(new OctetString(rwcommunity));
        PDU request = new PDU();
        request.add(new VariableBinding(targetOid, value));
        ResponseEvent responseEvent = snmp.set(request, target);
        if (responseEvent != null && responseEvent.getResponse() != null) {
            PDU response = responseEvent.getResponse();
            if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
                VariableBinding variableBinding = (VariableBinding) response.getVariableBindings().firstElement();
                Variable newValue = variableBinding.getVariable();
                if (newValue.equals(value)) {
                    return;
                } else {
                    throw new Exception("snmpset " + targetOid.toString() + " failure.");
                }
            } else {
                throw new Exception("Error in response when snmpset: " + targetOid.toString());
            }
        } else {
            throw new Exception("Error in connection to " + address + " when snmpset");
        }
    }
}
