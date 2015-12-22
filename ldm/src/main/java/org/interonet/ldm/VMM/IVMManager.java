package org.interonet.ldm.VMM;

import org.dom4j.DocumentException;
import org.libvirt.LibvirtException;

public interface IVMManager {
    String powerOnVM(Integer vmID) throws LibvirtException, DocumentException;
    String powerOffVM(Integer vmID);
}
