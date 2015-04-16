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

    public OperationCenter(GDMCore core) {
        this.core = core;
        try {
            client = new JsonRpcHttpClient(new URL(core.getConfigurationCenter().getConf("LDMConnectionURL")));
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
//        client.invoke("addSWitchConf", new Object[]{switchID, controllerIP, controllerPort}, String.class);
        operationCenterLogger.info("LDM --> addSWitchConf(switchID=" + switchID + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
    }

    @Override
    public void addSWitchConf(String type, Integer domSW, String controllerIP, int controllerPort) {
        operationCenterLogger.info("LDM --> addSWitchConf(type=" + type + ",domSW=" + domSW + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
    }

    @Override
    public void powerOnSwitch(Integer switchID) throws Throwable {
        client.invoke("powerOnSwitch", new Object[]{switchID + 1}, String.class);
        operationCenterLogger.info("LDM --> powerOnSwitch(switchID=" + switchID + ")");
    }

    @Override
    public void powerOnVM(Integer vmID) throws Throwable {
//        client.invoke("powerOnVM", new Object[]{vmID + 1}, String.class);
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
        client.invoke("powerOffSwitch", new Object[]{switchID + 1}, String.class);
        operationCenterLogger.info("LDM --> powerOffSwitch(switchID=" + switchID);

    }

    @Override
    public void powerOffVM(Integer vmID) throws Throwable {
//        client.invoke("powerOffVM", new Object[]{vmID + 1}, String.class);
        operationCenterLogger.info("LDM --> powerOffVM(vmID=" + vmID + ")");
    }
}
