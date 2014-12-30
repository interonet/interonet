package org.interonet.ldm.VMM;

import com.jcraft.jsch.JSchException;

public interface initBV {
    String createBridge() throws JSchException;

    String createVLAN() throws JSchException;

    String addBridgeToVlan() throws JSchException;

}
