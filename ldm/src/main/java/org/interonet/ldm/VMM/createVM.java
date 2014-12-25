package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

import com.jcraft.jsch.JSchException;

public interface createVM {
    String vmclone(int ID) throws JSchException;

    void vmstart(Connect connect, int ID) throws DocumentException, LibvirtException;
}
