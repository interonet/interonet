package org.interonet.gdm.LDMConnector;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class LDMCalls implements LDMService {
    private LDMService ldmService;
    private boolean DEBUG = true;
    private Logger logger = LoggerFactory.getLogger(LDMCalls.class);

    public LDMCalls(URL LDMConnectionURL) {
        JsonRpcHttpClient client = new JsonRpcHttpClient(LDMConnectionURL);
        client.setReadTimeoutMillis(300000);
        ldmService = ProxyUtil.createClientProxy(ClassLoader.getSystemClassLoader(), LDMService.class, client);
    }

    public LDMCalls(URL LDMConnectionURL, int LDMConnectionReadTimeoutMillis, boolean gdmDebug) {
        JsonRpcHttpClient client = new JsonRpcHttpClient(LDMConnectionURL);
        client.setReadTimeoutMillis(LDMConnectionReadTimeoutMillis);
        ldmService = ProxyUtil.createClientProxy(ClassLoader.getSystemClassLoader(), LDMService.class, client);
        DEBUG = gdmDebug;
    }


    @Override
    public void createTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) throws Throwable {
        logger.debug("LDM --> createTunnelSW2SW(switchToSwitchTunnels=)" + switchToSwitchTunnels);
        if (!DEBUG) {
            ldmService.createTunnelSW2SW(switchToSwitchTunnels);
        }
    }

    @Override
    @Deprecated
    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Throwable {
        logger.debug("LDM --> createTunnelSW2SW(switchPortPeer=" + switchPortPeer + ",peerSwitchPortPeer=" + peerSwitchPortPeer + ")");
        if (!DEBUG) {
            ldmService.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
        }
    }

    @Override
    @Deprecated
    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable {
        logger.debug("LDM --> createTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",vmId=" + vmID + ")");
        if (!DEBUG) {
            ldmService.createTunnelSW2VM(switchPortPeeronTT, vmID);
        }
    }

    @Override
    public void createTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) throws Throwable {
        logger.debug("LDM --> createTunnelSW2VM(switchToSwitchTunnels=)" + switchToVMTunnelList);
        if (!DEBUG) {
            ldmService.createTunnelSW2VM(switchToVMTunnelList);
        }
    }

    @Override
    public void addSwitchConf(String type, Integer switchId, String controllerIP, int controllerPort) throws Throwable {
        logger.debug("LDM --> addSwitchConf(type=" + type + ",switchId=" + switchId + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
        if (!DEBUG) {
            ldmService.addSwitchConf(type, switchId, controllerIP, controllerPort);
        }
    }

    /*
     *  customSwitchConf should be like this.
     *  {
     *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
     *     "boot-bin": "http://202.117.15.79/ons_bak/system.bit",
     *     "uImage": "http://202.117.15.79/ons_bak/uImage",
     *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
     *  }
     * */
    @Override
    public void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchId, String controllerIP, int controllerPort) throws Throwable {
        logger.debug("LDM --> addSwitchConf(customSwitchConfGDM=" + customSwitchConfGDM + ",switchId=" + switchId + ",controllerIP=" + controllerIP + ",controllerPort=" + controllerPort + ")");
        if (!DEBUG) {
            ldmService.addSwitchConf(customSwitchConfGDM, switchId, controllerIP, controllerPort);
        }
    }

    @Override
    public void powerOnSwitch(Integer switchID) throws Throwable {
        logger.debug("LDM --> powerOnSwitch(switchId=" + switchID + ")");
        if (!DEBUG) {
            ldmService.powerOnSwitch(switchID);
        }
    }

    @Override
    @Deprecated
    public String powerOnVM(Integer vmID) throws Throwable {
        logger.debug("LDM --> powerOnVM(vmIdList=" + vmID + ")");
        if (!DEBUG) {
            ldmService.powerOnVM(vmID);
        }
        return "Failed";
    }

    @Override
    public String powerOnVM(List<Integer> vmIDList) throws Throwable {
        logger.debug("LDM --> powerOnVM(vmIDList=" + vmIDList + ")");
        if (!DEBUG) {
            ldmService.powerOnVM(vmIDList);
        }
        return "Failed";
    }

    @Override
    @Deprecated
    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Throwable {
        logger.debug("LDM --> deleteTunnelSW2SW(switchPortPeeronTT=" + switchPortPeeronTT + ",athrSwitchPortPeeronTT=" + athrSwitchPortPeeronTT + ")");
        if (!DEBUG) {
            ldmService.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
        }

    }

    @Override
    public void deleteTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) throws Throwable {
        logger.debug("LDM --> deleteTunnelSW2SW(switchToSwitchTunnels=" + switchToSwitchTunnels + ")");
        if (!DEBUG) {
            ldmService.deleteTunnelSW2SW(switchToSwitchTunnels);
        }
    }

    @Override
    @Deprecated
    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Throwable {
        logger.debug("LDM --> deleteTunnelSW2VM(switchPortPeeronTT=" + switchPortPeeronTT + ",vmIdList=" + vmID + ")");
        if (!DEBUG) {
            ldmService.deleteTunnelSW2VM(switchPortPeeronTT, vmID + 11);
        }
    }

    @Override
    public void deleteTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) throws Throwable {
        logger.debug("LDM --> deleteTunnelSW2VM(switchToVMTunnelList=" + switchToVMTunnelList + ")");
        if (!DEBUG) {
            // TODO: 4/24/16 plus 11;
            ldmService.deleteTunnelSW2VM(switchToVMTunnelList);
        }

    }

    @Override
    public void deleteSWitchConf(Integer switchID) throws Throwable {
        logger.debug("LDM --> deleteSWitchConf(switchId=" + switchID + ")");
        if (!DEBUG) {
            ldmService.deleteSWitchConf(switchID);
        }
    }

    @Override
    @Deprecated
    public void powerOffSwitch(Integer switchID) throws Throwable {
        logger.debug("LDM --> powerOffSwitch(switchId=" + switchID);
        if (!DEBUG) {
            ldmService.powerOffSwitch(switchID);
        }
    }

    @Override
    @Deprecated
    public String powerOffVM(Integer vmID) throws Throwable {
        logger.debug("LDM --> powerOffVM(vmIdList=" + vmID + ")");
        if (!DEBUG) {
            ldmService.powerOffVM(vmID);
        }
        return "Success";
    }

    @Override
    public String powerOffVM(List<Integer> vmIdList) throws Throwable {
        logger.debug("LDM --> powerOffVM(vmIdList=" + vmIdList + ")");
        if (!DEBUG) {
            ldmService.powerOffVM(vmIdList);
        }
        return "Success";
    }
}
