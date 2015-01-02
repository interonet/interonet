package org.interonet.ldm.VMM;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.JSchException;

public interface IDeleteVirtualMachine {
    void vmdestroy(Connect connect, int ID) ;

    String vmdelete(int ID) ;
}
