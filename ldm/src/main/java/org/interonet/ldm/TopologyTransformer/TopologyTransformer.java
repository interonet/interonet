package org.interonet.ldm.TopologyTransformer;


public class TopologyTransformer {
    private SnmpManager snmpManager;

    public TopologyTransformer() {
        try {
            snmpManager = new SnmpManager();
        } catch (Exception e) {
            e.printStackTrace();
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