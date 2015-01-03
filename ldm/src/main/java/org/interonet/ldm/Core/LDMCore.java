package org.interonet.ldm.Core;

import org.interonet.ldm.VMM.*;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public class LDMCore {
    @SuppressWarnings("FieldCanBeLocal")
    private LDMAgent ldmAgent;
    private Connect connect;
    private ICreateVirtualMachine iCreateVirtualMachine;
    private IDeleteVirtualMachine iDeleteVirtualMachine;
    @SuppressWarnings("FieldCanBeLocal")
    private IBridgeAndVlan iBridgeAndVlan;


    public void start() {
        ldmAgent = new LDMAgent(this);
        iCreateVirtualMachine = new CreateVirtualMachine();
        iDeleteVirtualMachine = new DeleteVirtualMachine();
        iBridgeAndVlan = new BridgeAndVlan();
        iBridgeAndVlan.bridgeAndvlan();  //创建网桥和Vlan
        try {
            connect = new Connect("qemu+tcp://400@202.117.15.94/system", false);
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }

    public LDMAgent getAgent() {
        return null;
    }

    public void powerOnVM(Integer vmID) {
        iCreateVirtualMachine.vmclone(vmID);
        iCreateVirtualMachine.vmstart(connect, vmID);
    }

    public void powerOffVM(Integer vmID) {
        iDeleteVirtualMachine.vmdestroy(connect, vmID);
        iDeleteVirtualMachine.vmdelete(vmID);
    }
}
