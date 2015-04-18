package org.interonet.ldm.VMM;

/**
 * Created by houlifei on 15-4-18.
 */
public interface IVMManager {


    String powerOnVM(Integer vmID);
    String powerOffVM(Integer vmID);
}
