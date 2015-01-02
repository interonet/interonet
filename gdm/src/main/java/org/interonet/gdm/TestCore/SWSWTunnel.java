package org.interonet.gdm.TestCore;

public class SWSWTunnel {
    public int SwitchID;
    public int SwitchIDPortNum;
    public int PeerSwitchID;
    public int PeerSwitchIDPortNum;

    public SWSWTunnel(int switchID, int switchIDPortNum, int peerSwitchID, int peerSwitchIDPortNum) {
        SwitchID = switchID;
        SwitchIDPortNum = switchIDPortNum;
        PeerSwitchID = peerSwitchID;
        PeerSwitchIDPortNum = peerSwitchIDPortNum;
    }
}
