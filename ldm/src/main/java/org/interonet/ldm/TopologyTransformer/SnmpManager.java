package org.interonet.ldm.TopologyTransformer;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SnmpManager {
    private SnmpOperations snmpOperations;
    private Mibs mibs;

    public SnmpManager() throws Exception {
        snmpOperations = new SnmpOperations();
        mibs = new Mibs();
    }

    public void addVlan(int tag) throws Exception {
        Map<OID, Variable> status = new LinkedHashMap<OID, Variable>();
        int isEditing = 0;
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState), status);
        for (OID key : status.keySet()) {
            if (key.last() == tag) {
                throw new Exception("VLAN" + tag + " exists.");
            }
        }
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanEditState), status);
        isEditing = status.size();

        if (isEditing != 0) {
            snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
        }
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(2));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditBufferOwner).append(1), new OctetString("TTop"));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditRowStatus).append(1).append(tag), new Integer32(4));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditType).append(1).append(tag), new Integer32(1));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(3));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState).append(1), status);
        if (status.containsKey(new OID(mibs.vtpVlanState).append(1).append(tag)) == false) {
            throw new Exception("Fail to add VLAN" + tag);
        }
//System.out.println("VLAN"+tag+" created successfully");
    }

    public void deleteVlan(int tag) throws Exception {
//snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
        Map<OID, Variable> status = new LinkedHashMap<OID, Variable>();
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState), status);
        boolean isExist = false;
        for (OID key : status.keySet()) {
            if (key.last() == tag) isExist = true;
        }
        if (isExist == false) {
            throw new Exception("VLAN" + tag + " doed not exists");
        }
        int isEditing = 0;
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState), status);
        if (isEditing != 0) {
            snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
        }
        snmpOperations.snmpWalk(new OID(mibs.vmVlan), status);
        for (OID key : status.keySet()) {
            if (status.get(key).toInt() == tag) {
                int portnumber = key.last() - 10100;
                deletePortFromVlan(portnumber);
            }
        }
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(2));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditBufferOwner).append(1), new OctetString("TTop"));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditRowStatus).append(1).append(tag), new Integer32(6));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(3));
        snmpOperations.snmpSet(new OID(mibs.vtpVlanEditOperation).append(1), new Integer32(4));
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState), status);
        boolean isDelete = true;
        for (OID key : status.keySet()) {
            if (key.last() == tag) isDelete = false;
        }
        if (isDelete == false) {
            throw new Exception("Fail to delete Vlan" + tag);
        }
//System.out.println("VLAN"+tag+" delete successfully");
    }

    public void changeVlan(int originalTag, int tag) throws Exception {
        deleteVlan(originalTag);
        addVlan(tag);
    }

    public void addPortToVlan(int port, int tag) throws Exception {
        snmpOperations.snmpSet(new OID(mibs.vmVlan).append(10100 + port), new Integer32(tag));
    }

    public void deletePortFromVlan(int port) throws Exception {
        snmpOperations.snmpSet(new OID(mibs.vmVlan).append(10100 + port), new Integer32(1));
    }

    public void changePortFromVlantoVlan(int port, int tag) throws Exception {
        snmpOperations.snmpSet(new OID(mibs.vmVlan).append(10100 + port), new Integer32(tag));
    }

    public void upPort(int port) throws Exception {
        snmpOperations.snmpSet(new OID(mibs.ifAdminStatus).append(10100 + port), new Integer32(1));
    }

    public void downPort(int port) throws Exception {
        snmpOperations.snmpSet(new OID(mibs.ifAdminStatus).append(10100 + port), new Integer32(2));
    }

    public int getNextFreeVlanID() throws Exception {
        Map<OID, Variable> status = new HashMap<OID, Variable>();
        snmpOperations.snmpWalk(new OID(mibs.vtpVlanState), status);
        for (int vlanID = 101; vlanID < 1000; vlanID++) {
            if (status.containsKey(new OID(mibs.vtpVlanState).append(1).append(vlanID)) == false) {
                return vlanID;
            }
        }
        throw new Exception("No vlanid available");
    }

    public int isPortPeerPort(int port1, int port2) throws Exception {
        Map<OID, Variable> status = new LinkedHashMap<OID, Variable>();
        snmpOperations.snmpWalk(new OID(mibs.vmVlan), status);
        int vlanID = status.get(new OID(mibs.vmVlan).append(10100 + port1)).toInt();
        if (vlanID == status.get(new OID(mibs.vmVlan).append(10100 + port2)).toInt()) {
            return vlanID;
        } else {
            throw new Exception("Port" + port1 + " and port" + port2 + " are not in the same Vlan");
        }
    }

    public void isPortPeerVlan(int port, int vlanID) throws Exception {
        Map<OID, Variable> status = new HashMap<OID, Variable>();
        snmpOperations.snmpWalk(new OID(mibs.vmVlan), status);
        OID targetVmVlan = new OID(mibs.vmVlan).append(10100 + port);
        if (status.containsKey(targetVmVlan) == false || status.get(targetVmVlan).toInt() != vlanID) {
            throw new Exception("Port" + port + " does not peer vlan" + vlanID);
        }
    }
}