package org.interonet.ldm.Core;

import com.jcraft.jsch.JSchException;
import org.dom4j.DocumentException;
import org.interonet.ldm.VMM.createVM;
import org.interonet.ldm.VMM.createVirtualMachine;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public class LDMCore {
    private LDMAgent ldmAgent;
    private createVM CreateVM;

    public void start() {
        ldmAgent = new LDMAgent(this);
        CreateVM = new createVirtualMachine();
    }

    public LDMAgent getAgent() {
        return null;
    }

    public void powerOnVM(Integer vmID) {
        try {
            Connect conn = new Connect("qemu+tcp://400@202.117.15.94/system",false);
            try {
                CreateVM.vmclone(vmID);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            try {
                CreateVM.vmstart(conn, vmID);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        } catch (LibvirtException e) {
            e.printStackTrace();
        }
    }
}
