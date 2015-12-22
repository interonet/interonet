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

    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Exception {
        int vlanID = snmpManager.getNextFreeVlanID();
        snmpManager.addVlan(vlanID);
        snmpManager.addPortToVlan(switchPortPeer, vlanID);
        snmpManager.addPortToVlan(peerSwitchPortPeer, vlanID);
        snmpManager.upPort(switchPortPeer);
        snmpManager.upPort(peerSwitchPortPeer);
    }

    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        snmpManager.addPortToVlan(switchPortPeeronTT, vmID);
        snmpManager.upPort(switchPortPeeronTT);
    }

    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception {
        snmpManager.downPort(switchPortPeeronTT);
        snmpManager.downPort(athrSwitchPortPeeronTT);
        int vlanid = snmpManager.isPortPeerPort(switchPortPeeronTT, athrSwitchPortPeeronTT);
        snmpManager.deleteVlan(vlanid);
    }

    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        snmpManager.isPortPeerVlan(switchPortPeeronTT, vmID);
        snmpManager.deletePortFromVlan(switchPortPeeronTT);
        snmpManager.downPort(switchPortPeeronTT);
    }
}