package org.interonet.ldm.service;

public class SwitchToVMTunnel {
    private int switchId;
    private int switchPort;
    private int vmId;
    private int vmPort;

    public SwitchToVMTunnel() {
    }

    public SwitchToVMTunnel(int switchId, int switchPort, int vmId, int vmPort) {
        this.switchId = switchId;
        this.switchPort = switchPort;
        this.vmId = vmId;
        this.vmPort = vmPort;
    }

    @Override
    public String toString() {
        return "SwitchToVMTunnel{" +
                "switchId=" + switchId +
                ", switchPort=" + switchPort +
                ", vmId=" + vmId +
                ", vmPort=" + vmPort +
                '}';
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public int getSwitchPort() {
        return switchPort;
    }

    public void setSwitchPort(int switchPort) {
        this.switchPort = switchPort;
    }

    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public int getVmPort() {
        return vmPort;
    }

    public void setVmPort(int vmPort) {
        this.vmPort = vmPort;
    }
}
