package org.interonet.gdm.LDMConnector;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.interonet.ldm.service.LDMService;
import org.interonet.ldm.service.SwitchToSwitchTunnel;
import org.interonet.ldm.service.SwitchToVMTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

public class LDMCalls {
    private LDMService ldmService;
    private boolean DEBUG = true;
    private Logger logger = LoggerFactory.getLogger(LDMCalls.class);

    // TODO: 4/27/16 Check Return Value of RPC.
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

    public String createTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        logger.debug("LDM --> createTunnelSW2SW(switchToSwitchTunnels=)" + switchToSwitchTunnels);
        if (!DEBUG) {
            ldmService.createSwitchToSwitchTunnel(switchToSwitchTunnels);
        }
        return "Success";
    }

    public String createTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.debug("LDM --> createTunnelSW2VM(switchToSwitchTunnels=)" + switchToVMTunnelList);
        if (!DEBUG) {
            ldmService.createSwitchToVMTunnel(switchToVMTunnelList);
        }
        return "Success";
    }

    public String powerOnVM(List<Integer> vmIDList) throws Throwable {
        logger.debug("LDM --> powerOnVM(vmIDList=" + vmIDList + ")");
        if (!DEBUG) {
            ldmService.powerOnVM(vmIDList);
        }
        return "Success";
    }

    public String deleteTunnelSW2SW(List<SwitchToSwitchTunnel> switchToSwitchTunnels) {
        logger.debug("LDM --> deleteTunnelSW2SW(switchToSwitchTunnels=" + switchToSwitchTunnels + ")");
        if (!DEBUG) {
            ldmService.deleteSwitchToSwitchTunnel(switchToSwitchTunnels);
        }
        return "Success";
    }

    public String deleteTunnelSW2VM(List<SwitchToVMTunnel> switchToVMTunnelList) {
        logger.debug("LDM --> deleteTunnelSW2VM(switchToVMTunnelList=" + switchToVMTunnelList + ")");
        if (!DEBUG) {
            ldmService.deleteSwitchToVMTunnel(switchToVMTunnelList);
        }
        return "Success";
    }

    public String powerOffVM(List<Integer> vmIdList) throws Throwable {
        logger.debug("LDM --> powerOffVM(vmIdList=" + vmIdList + ")");
        if (!DEBUG) {
            ldmService.powerOffVM(vmIdList);
        }
        return "Success";
    }
}
