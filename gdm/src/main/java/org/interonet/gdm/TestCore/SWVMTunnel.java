package org.interonet.gdm.TestCore;

public class SWVMTunnel {
    public int SwitchID;
    public int SwitchPort;
    public int VMID;
    public int VMPort;

    public SWVMTunnel(int switchID, int switchPort, int VMID, int VMPort) {
        SwitchID = switchID;
        SwitchPort = switchPort;
        this.VMID = VMID;
        this.VMPort = VMPort;
    }
}
