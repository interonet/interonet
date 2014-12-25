package org.interonet.ldm.VMM;

import com.jcraft.jsch.JSchException;

/**
 * Created by houlifei on 14-12-25.
 */
public interface initBV {
    String createBridge()throws JSchException;
    String createVLAN()throws JSchException;
    String addBridgeToVlan()throws JSchException;

}
