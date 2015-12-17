package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.interonet.ldm.Core.LDMCore;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import java.util.logging.Logger;

public class VMManager implements IVMManager {
    private LDMCore core;
    private ICreateVirtualMachine iCreateVirtualMachine;
    private IDeleteVirtualMachine iDeleteVirtualMachine;
    private IBridgeAndVlan iBridgeAndVlan;
    private Connect connect;
    private Logger logger = Logger.getLogger(VMManager.class.getCanonicalName());

    public VMManager(LDMCore core) {
        try {
            this.core = core;
            iCreateVirtualMachine = new CreateVirtualMachine();
            iDeleteVirtualMachine = new DeleteVirtualMachine();
            iBridgeAndVlan = new BridgeAndVlan();
            String libvirtConnectURL = core.getConfigurationCenter().getLibvirtConnectURL();
            connect = new Connect(libvirtConnectURL, false);
            iBridgeAndVlan.bridgeAndvlan();
            logger.info(VMManager.class.getCanonicalName() + "has been initiate successfully");
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String powerOnVM(Integer vmID) throws LibvirtException, DocumentException {
        String powerOnVMResult = "failure";
        String vmCloneTest = iCreateVirtualMachine.cloneVM(vmID);
        String vmStartTest = iCreateVirtualMachine.startVM(connect, vmID);
        String vmtestOn = "Clone 'vmm" + vmID + "' created successfully.";
        if (vmCloneTest.equals(vmtestOn) && vmStartTest.equals("success"))
            powerOnVMResult = "success";

        logger.info(vmCloneTest);
        logger.info(vmStartTest);
        return powerOnVMResult;
    }

    @Override
    public String powerOffVM(Integer vmID) {
        String powerOffVMResult = "failure";
        String vmDestroyTest = iDeleteVirtualMachine.vmdestroy(connect, vmID);
        String vmDeleteTest = iDeleteVirtualMachine.vmdelete(vmID);
        String vmtestOff = "Domain vmm" + vmID + " has been undefined";
        if (vmDestroyTest.equals("success") && vmDeleteTest.equals(vmtestOff))
            powerOffVMResult = "success";
        return powerOffVMResult;
    }
}
