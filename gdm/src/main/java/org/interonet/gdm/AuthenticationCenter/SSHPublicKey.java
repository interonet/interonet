package org.interonet.gdm.AuthenticationCenter;

import java.security.PublicKey;

public class SSHPublicKey implements PublicKey{
    @Override
    public String getAlgorithm() {
        return null;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];
    }
}
