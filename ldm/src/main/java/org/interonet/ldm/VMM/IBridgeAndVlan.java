package org.interonet.ldm.VMM;

import com.jcraft.jsch.JSchException;

public interface IBridgeAndVlan {
    String createBridge();

    String createVLAN();

    String addBridgeToVlan();

    void bridgeAndvlan();

}
