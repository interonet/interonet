package org.interonet.ldm.VMM;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;

public class DeleteVirtualMachine implements IDeleteVirtualMachine {
    @Override
    public String  vmdestroy(Connect connect, int ID)  {
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
    public String vmdelete(int ID)  {
        String command = "virsh undefine vmm" + ID + ";rm -f /home/400/vmuser/vm" + ID + ".img";
        Channel channel = new Channel("root","xjtu420","202.117.15.94", 22);
        String result = channel.setChannel(command,false);
        return result;
    }


}
