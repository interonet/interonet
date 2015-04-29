package org.interonet.gdm.OperationCenter;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.interonet.gdm.Core.GDMCore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class OperationCenter implements IOperationCenter {
    JsonRpcHttpClient client;
    Logger operationCenterLogger = Logger.getLogger("operationCenterLogger");
    GDMCore core;
    boolean DEBUG = true;

    public OperationCenter(GDMCore core) {
        this.core = core;
        try {
            client = new JsonRpcHttpClient(new URL(core.getConfigurationCenter().getConf("LDMConnectionURL")));
            client.setReadTimeoutMillis(60 * 1000 * 5);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable {
        operationCenterLogger.info("LDM --> createTunnelSW2SW(switchPortPeer=" + switchPortPeer + ",peerSwitchPortPeer=" + peerSwitchPortPeer + ")");
        if (!DEBUG) {
            client.invoke("createTunnelSW2SW", new Object[]{switchPortPeer, peerSwitchPortPeer}, String.class);
        }
    }

    @Override
    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable {
        operationCenterLogger.info("LDM --> createTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",VMID=" + vmID + ")");
        if (!DEBUG) {
            client.invoke("createTunnelSW2VM", new Object[]{switchPortPeeronTT, vmID + 11}, String.class);
        }
    }

    @Override
    public void addSWitchConf(Integer switchID, String controllerIP, int controllerPort) throws Throwable {
        operationCenterLogger.info("LDM --> addSWitchConf(switchID=" + switchID + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
        if (!DEBUG) {
            client.invoke("addSWitchConf", new Object[]{switchID, controllerIP, controllerPort}, String.class);
        }
    }

    @Override
    public void addSWitchConf(String type, Integer domSW, String controllerIP, int controllerPort) throws Throwable {
        operationCenterLogger.info("LDM --> addSWitchConf(type=" + type + ",domSW=" + domSW + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
        if (!DEBUG) {
            client.invoke("addSWitchConf", new Object[]{type, domSW, controllerIP, controllerPort}, String.class);
        }
    }

    @Override
    public void powerOnSwitch(Integer switchID) throws Throwable {
        operationCenterLogger.info("LDM --> powerOnSwitch(switchID=" + switchID + ")");
        if (!DEBUG) {
            client.invoke("powerOnSwitch", new Object[]{switchID + 1}, String.class);
        }
    }

    @Override
    public void powerOnVM(Integer vmID) throws Throwable {
        operationCenterLogger.info("LDM --> powerOnVM(vmID=" + vmID + ")");
        if (!DEBUG) {
            client.invoke("powerOnVM", new Object[]{vmID + 1}, String.class);
        }
    }

    @Override
    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable {
        operationCenterLogger.info("LDM --> deleteTunnelSW2SW(switchPortPeeronTT=" + switchPortPeeronTT + ",athrSwitchPortPeeronTT=" + athrSwitchPortPeeronTT + ")");
        if (!DEBUG) {
            client.invoke("deleteTunnelSW2SW", new Object[]{switchPortPeeronTT, athrSwitchPortPeeronTT}, String.class);
        }

    }

    @Override
    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable {
        operationCenterLogger.info("LDM --> deleteTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",vmID=" + vmID + ")");
        if (!DEBUG) {
            client.invoke("deleteTunnelSW2VM", new Object[]{switchPortPeeronTT, vmID + 11}, String.class);
        }
    }

    @Override
    public void deleteSWitchConf(Integer switchID) throws Throwable {
        operationCenterLogger.info("LDM --> deleteSWitchConf(switchID=" + switchID + ")");
        if (!DEBUG) {
            client.invoke("deleteSWitchConf", new Object[]{switchID}, String.class);
        }
    }

    @Override
    public void powerOffSwitch(Integer switchID) throws Throwable {
        operationCenterLogger.info("LDM --> powerOffSwitch(switchID=" + switchID);
        if (!DEBUG) {
            client.invoke("powerOffSwitch", new Object[]{switchID + 1}, String.class);
        }
    }

    @Override
    public void powerOffVM(Integer vmID) throws Throwable {
        operationCenterLogger.info("LDM --> powerOffVM(vmID=" + vmID + ")");
        if (!DEBUG) {
            client.invoke("powerOffVM", new Object[]{vmID + 1}, String.class);
        }
    }
}
