package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public class VMManager implements IVMManager {
    private ICreateVirtualMachine iCreateVirtualMachine;
    private IDeleteVirtualMachine iDeleteVirtualMachine;
    private IBridgeAndVlan iBridgeAndVlan;
    private Connect connect;

    public VMManager() {
        iCreateVirtualMachine = new CreateVirtualMachine();
        iDeleteVirtualMachine = new DeleteVirtualMachine();
        iBridgeAndVlan = new BridgeAndVlan();
        try {
            connect = new Connect("qemu+tcp://400@192.168.2.3/system", false);
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
        iBridgeAndVlan.bridgeAndvlan();
    }

    @Override
    public String powerOnVM(Integer vmID) throws LibvirtException, DocumentException {
        String powerOnVMResult = "failure";
        String vmCloneTest = iCreateVirtualMachine.cloneVM(vmID);
        String vmStartTest = iCreateVirtualMachine.startVM(connect, vmID);
        String vmtestOn = "Clone 'vmm" + vmID + "' created successfully.";
        if (vmCloneTest.equals(vmtestOn) && vmStartTest.equals("success"))
            powerOnVMResult = "success";
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
