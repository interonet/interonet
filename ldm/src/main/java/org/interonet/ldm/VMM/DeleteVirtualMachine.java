package org.interonet.ldm.VMM;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

public class DeleteVirtualMachine implements IDeleteVirtualMachine {
    private String user = "root";
    private String password = "xjtu420";
    private String ip = "192.168.2.3";

    @Override
    public String vmdestroy(Connect connect, int ID) {
        String vmDestroyResult = "failure";
        try {
            Domain domain = connect.domainLookupByName("vm" + ID);
            domain.destroy();
            vmDestroyResult = "success";
        } catch (LibvirtException e) {
            e.printStackTrace();
        }
        return vmDestroyResult;
    }

    @Override
    public String vmdelete(int ID) {
        String command = "virsh undefine vmm" + ID + ";rm -f /home/400/vmuser/vm" + ID + ".img";
        Channel channel = new Channel(user, password, ip, 22);
        String result = channel.setChannel(command, false);
        return result;
    }
}
