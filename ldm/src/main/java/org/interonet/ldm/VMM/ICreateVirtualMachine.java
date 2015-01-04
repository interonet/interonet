package org.interonet.ldm.VMM;

import org.libvirt.Connect;

public interface ICreateVirtualMachine {
    String vmclone(int ID);

    String vmstart(Connect connect, int ID);
}
