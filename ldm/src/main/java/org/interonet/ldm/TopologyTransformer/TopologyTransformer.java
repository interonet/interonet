package org.interonet.ldm.TopologyTransformer;


import java.util.logging.Logger;

public class TopologyTransformer {
    private final int MAX_TT_PORT_NUM = 40;

    private SnmpManager snmpManager;
    private Logger logger = Logger.getLogger(TopologyTransformer.class.getName());

    public TopologyTransformer() {
        try {
            snmpManager = new SnmpManager();
            initVlanTag();
            initPortStatus();
            logger.info(TopologyTransformer.class.getCanonicalName() + "has been initiate successfully");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    public void initPortStatus() {
        try {
            for (int i = 1; i <= MAX_TT_PORT_NUM; i++) {
                snmpManager.downPort(i);
            }
            logger.info("Initiate all the 40 ports to disable status");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    public void initVlanTag() {
        try {
            for (int i = 1; i <= MAX_TT_PORT_NUM; i++) {
                snmpManager.changePortFromVlantoVlan(i, 1);
            }
            logger.info("Initiate all the 40 ports to VLAN 1");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    public void createTunnelSW2SW(int portOnTT, int peerPortOnTT) throws Exception {
        logger.info("portOnTT = [" + portOnTT + "], peerPortOnTT = [" + peerPortOnTT + "]");
        int vlanID = snmpManager.getNextFreeVlanID();
        snmpManager.addVlan(vlanID);
        snmpManager.addPortToVlan(portOnTT, vlanID);
        snmpManager.addPortToVlan(peerPortOnTT, vlanID);
        snmpManager.upPort(portOnTT);
        snmpManager.upPort(peerPortOnTT);
    }

    public void createTunnelSW2VM(int portOnTT, int vlanId) throws Exception {
        logger.info("portOnTT = [" + portOnTT + "], vlanId = [" + vlanId + "]");
        snmpManager.addPortToVlan(portOnTT, vlanId);
        snmpManager.upPort(portOnTT);
    }

    public void deleteTunnelSW2SW(int portOnTT, int peerPortOnTT) throws Exception {
        logger.info("portOnTT = [" + portOnTT + "], peerPortOnTT = [" + peerPortOnTT + "]");
        snmpManager.downPort(portOnTT);
        snmpManager.downPort(peerPortOnTT);
        int vlanid = snmpManager.isPortPeerPort(portOnTT, peerPortOnTT);
        snmpManager.deleteVlan(vlanid);
    }

    public void deleteTunnelSW2VM(int portOnTT, int vlanId) throws Exception {
        logger.info("portOnTT = [" + portOnTT + "], vlanId = [" + vlanId + "]");
        snmpManager.isPortPeerVlan(portOnTT, vlanId);
        snmpManager.deletePortFromVlan(portOnTT);
        snmpManager.downPort(portOnTT);
    }
}