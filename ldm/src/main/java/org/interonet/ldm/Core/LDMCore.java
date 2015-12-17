package org.interonet.ldm.Core;

import org.dom4j.DocumentException;
import org.interonet.ldm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.PowerManager.PowerManager;
import org.interonet.ldm.SwitchManager.ISwitchManager;
import org.interonet.ldm.SwitchManager.SwitchManager;
import org.interonet.ldm.TopologyTransformer.TopologyTransformer;
import org.interonet.ldm.VMM.IVMManager;
import org.interonet.ldm.VMM.VMManager;
import org.libvirt.LibvirtException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class LDMCore {
    @SuppressWarnings("FieldCanBeLocal")
    private LDMAgent ldmAgent;
    @SuppressWarnings("FieldCanBeLocal")
    private PowerManager powerManager;
    private ISwitchManager switchManager;
    private IConfigurationCenter configurationCenter;
    private IVMManager vMManager;

    private TopologyTransformer topologyTransformer;

    private Logger logger = Logger.getLogger(LDMCore.class.getCanonicalName());

    public void start() {
        ldmAgent = new LDMAgent(this);

        // PowerManager
        powerManager = new PowerManager();

        // ConfigurationCenter initiation.
        configurationCenter = new ConfigurationCenter(this);

        // SwitchManager initiation.
        switchManager = new SwitchManager(this);

        // VMManager initiation
        vMManager = new VMManager(this);

        // TT initiation.
        topologyTransformer = new TopologyTransformer();

    }

    public LDMAgent getAgent() {
        return ldmAgent;
    }

    public IVMManager getVMManager() {
        return vMManager;
    }

    public ISwitchManager getSwitchManager() {
        return switchManager;
    }

    public TopologyTransformer getTopologyTransformer() {
        return topologyTransformer;
    }

    public String powerOnVM(Integer vmID) throws LibvirtException, DocumentException {
        return vMManager.powerOnVM(vmID);
    }

    public String powerOffVM(Integer vmID) {
        return vMManager.powerOffVM(vmID);

    }


    public void powerOnSwitch(Integer switchID) throws Exception {
        powerManager.powerOnSwitchById(switchID);
    }


    public void powerOffSwitch(Integer switchID) throws Exception {
        powerManager.powerOffSwitchById(switchID);
    }

    public void addSwitchConf(String type, Integer switchID, String controllerIP, int controllerPort) throws IOException, InterruptedException {
        switchManager.changeSwitchConf(type, switchID, controllerIP, controllerPort);
    }

    /*
    *   customSwitchConf should be like this.
    *
    *  {
    *     "root-fs": "http://202.117.15.79/ons_bak/backup.tar.xz",
    *     "boot-bin": "http://202.117.15.79/ons_bak/system.bit",
    *     "uImage": "http://202.117.15.79/ons_bak/uImage",
    *     "device-tree": "http://202.117.15.79/ons_bak/devicetree.dtb"
    *  }
    *
    * */
    public void addSwitchConf(Map<String, String> customSwitchConfGDM, Integer switchID, String controllerIP, int controllerPort) throws Exception {
        switchManager.changeSwitchConf(customSwitchConfGDM, switchID, controllerIP, controllerPort);
    }

    public IConfigurationCenter getConfigurationCenter() {
        return configurationCenter;
    }

    public void resetSwitchConf(Integer switchID) throws IOException, InterruptedException {
        switchManager.resetSwitchConf(switchID);
    }

    public void createTunnelSW2SW(int switchPortPeer, int peerSwitchPortPeer) throws Exception {
        topologyTransformer.createTunnelSW2SW(switchPortPeer, peerSwitchPortPeer);
    }

    public void createTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        topologyTransformer.createTunnelSW2VM(switchPortPeeronTT, vmID);
    }

    public void deleteTunnelSW2SW(int switchPortPeeronTT, int athrSwitchPortPeeronTT) throws Exception {
        topologyTransformer.deleteTunnelSW2SW(switchPortPeeronTT, athrSwitchPortPeeronTT);
    }

    public void deleteTunnelSW2VM(int switchPortPeeronTT, int vmID) throws Exception {
        topologyTransformer.deleteTunnelSW2VM(switchPortPeeronTT, vmID);
    }
}
