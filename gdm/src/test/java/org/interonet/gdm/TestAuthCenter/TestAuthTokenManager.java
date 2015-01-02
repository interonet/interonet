package org.interonet.gdm.TestAuthCenter;

import org.interonet.gdm.AuthenticationCenter.AuthTokenManager;
import org.interonet.gdm.AuthenticationCenter.IAuthTokenManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAuthTokenManager {
    private static IAuthTokenManager authTokenManager;

    @BeforeClass
    public static void initAuthToken() {
        authTokenManager = new AuthTokenManager();
    }

    @Test
    public void testToAuthToken() {

    }

    @Test
    public void testGenerate() {

    }

    @Test
    public void testAuth() {

    }

    @Test
    public void getUsernameByToken() {

    }
}
