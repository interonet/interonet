package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.JSchException;

public interface ICreateVirtualMachine {
    String vmclone(int ID);

    void vmstart(Connect connect, int ID);
}
