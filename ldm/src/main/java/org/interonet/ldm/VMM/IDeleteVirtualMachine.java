package org.interonet.ldm.VMM;

import org.libvirt.Connect;

public interface IDeleteVirtualMachine {
    String vmdestroy(Connect connect, int ID) ;

    String vmdelete(int ID) ;
}
