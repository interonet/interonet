package org.interonet.ldm.VMM;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.JSchException;

public interface deleteVM {
    void vmdestroy(Connect connect, int ID) throws LibvirtException;

    String vmdelete(int ID) throws JSchException;
}
