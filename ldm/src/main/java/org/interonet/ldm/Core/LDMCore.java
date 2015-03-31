package org.interonet.ldm.Core;

import org.interonet.ldm.ConfigurationCenter.ConfigurationCenter;
import org.interonet.ldm.ConfigurationCenter.IConfigurationCenter;
import org.interonet.ldm.SwitchManager.ISwitchManager;
import org.interonet.ldm.SwitchManager.SwitchManager;
import org.interonet.ldm.VMM.*;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import java.io.IOException;

public class LDMCore {
    @SuppressWarnings("FieldCanBeLocal")
    private LDMAgent ldmAgent;
    private Connect connect;
    private ICreateVirtualMachine iCreateVirtualMachine;
    private IDeleteVirtualMachine iDeleteVirtualMachine;
    @SuppressWarnings("FieldCanBeLocal")
    private IBridgeAndVlan iBridgeAndVlan;

    private ISwitchManager switchManager;

    private IConfigurationCenter configurationCenter;

    public void start() {
        ldmAgent = new LDMAgent(this);
        iCreateVirtualMachine = new CreateVirtualMachine();
        iDeleteVirtualMachine = new DeleteVirtualMachine();
        iBridgeAndVlan = new BridgeAndVlan();
        iBridgeAndVlan.bridgeAndvlan();  //创建网桥和Vlan

        // VMM initiation.
        try {
            connect = new Connect("qemu+tcp://400@202.117.15.94/system", false);
        } catch (LibvirtException e) {
            e.printStackTrace();
        }

        // ConfigurationCenter initiation.
        configurationCenter = new ConfigurationCenter(this);

        // SwitchManager initiation.
        switchManager = new SwitchManager(this);
    }

    public LDMAgent getAgent() {
        return ldmAgent;
    }

    public String powerOnVM(Integer vmID) {
        String powerOnVMResult = "failure";
        String vmCloneTest = iCreateVirtualMachine.vmclone(vmID);
        String vmStartTest = iCreateVirtualMachine.vmstart(connect, vmID);
        String vmtestOn = "Clone 'vmm"+vmID+"' created successfully.";
        if(vmCloneTest.equals(vmtestOn) && vmStartTest.equals("success"))

            powerOnVMResult="success";

        return powerOnVMResult;
    }

    public String powerOffVM(Integer vmID) {
        String powerOffVMResult = "failure";
        String vmDestroyTest = iDeleteVirtualMachine.vmdestroy(connect, vmID);
        String vmDeleteTest = iDeleteVirtualMachine.vmdelete(vmID);
        String vmtestOff="Domain vmm"+vmID+" has been undefined";
        if(vmDestroyTest.equals("success") && vmDeleteTest.equals(vmtestOff))
            powerOffVMResult = "success";
        return powerOffVMResult;

    }

    public void addSwitchConf(Integer switchID, String controllerIP, int controllerPort) throws IOException{
        switchManager.changeConnectionPropertyFromNFS(switchID, controllerIP, controllerPort);
    }

    public IConfigurationCenter getConfigurationCenter() {
        return configurationCenter;
    }
}
