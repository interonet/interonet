package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;

public interface ICreateVirtualMachine {
    String cloneVM(int ID);

    String startVM(Connect connect, int ID) throws LibvirtException, DocumentException;
}
