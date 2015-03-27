package org.interonet.gdm.OperationCenter;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OperationCenter implements IOperationCenter {
    Map<String, String> localDomains;
    JsonRpcHttpClient client;
    Logger operationCenterLogger = Logger.getLogger("operationCenterLogger");

    //TODO test after ldm agent be completed.
    public OperationCenter() {
        localDomains = new HashMap<String, String>();
        localDomains.put("XJTU", "192.168.1.2");
        try {
            client = new JsonRpcHttpClient(new URL("http://127.0.0.1:8081/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable {
//        client.invoke("createTunnelSW2SW", new Object[]{switchPortPeer, peerSwitchPortPeer}, String.class);
        operationCenterLogger.info("LDM --> createTunnelSW2SW(switchPortPeer=" + switchPortPeer + ",peerSwitchPortPeer=" + peerSwitchPortPeer + ")");
    }

    @Override
    public void createTunnelSW2VM(int switchPortPeeronTT, int VMID) throws Throwable {
//        client.invoke("createTunnelSW2VM", new Object[]{switchPortPeeronTT, peerVMPortPeeronTT}, String.class);
        operationCenterLogger.info("LDM --> createTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",VMID=" + VMID + ")");
    }

    @Override
    public void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws Throwable {
        client.invoke("addSWitchConf", new Object[]{switchID, controllerIP, controllerPort}, String.class);
        operationCenterLogger.info("LDM --> addSWitchConf(switchID=" + switchID + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
    }

    @Override
    public void powerOnSwitch(Integer switchID) throws Throwable {
//        client.invoke("powerOnSwitch", new Object[]{switchID}, String.class);
        operationCenterLogger.info("LDM --> powerOnSwitch(switchID=" + switchID + ")");
    }

    @Override
    public void powerOnVM(Integer vmID) throws Throwable {
//        client.invoke("powerOnVM", new Object[]{vmID}, String.class);
        operationCenterLogger.info("LDM --> powerOnVM(vmID=" + vmID + ")");
    }

    @Override
    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable {
//        client.invoke("deleteTunnelSW2SW", new Object[]{switchPortPeeronTT, athrSwitchPortPeeronTT}, String.class);
        operationCenterLogger.info("LDM --> deleteTunnelSW2SW(switchPortPeeronTT=" + switchPortPeeronTT + ",athrSwitchPortPeeronTT=" + athrSwitchPortPeeronTT + ")");
    }

    @Override
    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable {
//        client.invoke("deleteTunnelSW2VM", new Object[]{switchPortPeeronTT, peerVMPortPeeronTT}, String.class);
        operationCenterLogger.info("LDM --> deleteTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",vmID=" + vmID + ")");
    }

    @Override
    public void deleteSWitchConf(Integer switchID) throws Throwable {
//        client.invoke("deleteSWitchConf", new Object[]{switchID}, String.class);
        operationCenterLogger.info("LDM --> deleteSWitchConf(switchID=" + switchID + ")");
    }

    @Override
    public void powerOffSwitch(Integer switchID) throws Throwable {
//        client.invoke("powerOffSwitch", new Object[]{switchID}, String.class);
        operationCenterLogger.info("LDM --> powerOffSwitch(switchID=" + switchID);

    }

    @Override
    public void powerOffVM(Integer vmID) throws Throwable {
//        client.invoke("powerOffVM", new Object[]{vmID}, String.class);
        operationCenterLogger.info("LDM --> powerOffVM(vmID=" + vmID + ")");
    }
}
