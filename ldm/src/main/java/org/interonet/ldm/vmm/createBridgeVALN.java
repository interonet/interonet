package com.init.bridge;

import com.jcraft.jsch.JSchException;

public interface createBridgeVALN {
	String createbr() throws JSchException;
	String createvlan() throws JSchException;
	String addBridgeToVlan() throws JSchException;
}
