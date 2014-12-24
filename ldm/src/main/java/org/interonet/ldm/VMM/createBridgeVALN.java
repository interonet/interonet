package org.interonet.ldm.VMM;

import com.jcraft.jsch.JSchException;

public interface createBridgeVALN {
	String createBridge() throws JSchException;
	String createvlan() throws JSchException;
	String addBridgeToVlan() throws JSchException;
}
