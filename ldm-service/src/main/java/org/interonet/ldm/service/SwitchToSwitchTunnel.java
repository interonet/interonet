package org.interonet.ldm.service;

public class SwitchToSwitchTunnel {
    private int switchId;
    private int switchIdPortNum;
    private int peerSwitchId;
    private int peerSwitchIdPortNum;

    public SwitchToSwitchTunnel() {
    }

    public SwitchToSwitchTunnel(int switchId, int switchIdPortNum, int peerSwitchId, int peerSwitchIdPortNum) {
        this.switchId = switchId;
        this.switchIdPortNum = switchIdPortNum;
        this.peerSwitchId = peerSwitchId;
        this.peerSwitchIdPortNum = peerSwitchIdPortNum;
    }

    @Override
    public String toString() {
        return "SwitchToSwitchTunnel{" +
                "switchId=" + switchId +
                ", switchIdPortNum=" + switchIdPortNum +
                ", peerSwitchId=" + peerSwitchId +
                ", peerSwitchIdPortNum=" + peerSwitchIdPortNum +
                '}';
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
    }

    public int getSwitchIdPortNum() {
        return switchIdPortNum;
    }

    public void setSwitchIdPortNum(int switchIdPortNum) {
        this.switchIdPortNum = switchIdPortNum;
    }

    public int getPeerSwitchId() {
        return peerSwitchId;
    }

    public void setPeerSwitchId(int peerSwitchId) {
        this.peerSwitchId = peerSwitchId;
    }

    public int getPeerSwitchIdPortNum() {
        return peerSwitchIdPortNum;
    }

    public void setPeerSwitchIdPortNum(int peerSwitchIdPortNum) {
        this.peerSwitchIdPortNum = peerSwitchIdPortNum;
    }
}
