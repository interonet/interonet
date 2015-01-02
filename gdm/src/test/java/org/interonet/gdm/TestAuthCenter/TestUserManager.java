package org.interonet.gdm.TestAuthCenter;


import org.interonet.gdm.AuthenticationCenter.IUserManager;
import org.interonet.gdm.AuthenticationCenter.UserManager;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestUserManager {

    @Test
    public void testAuthUser() throws Exception {
        IUserManager userManger = new UserManager();

        assertTrue(userManger.authUser("admin", "admin"));
        assertTrue(userManger.authUser("root", "root"));
        assertTrue(userManger.authUser("test1", "test1"));
        assertTrue(userManger.authUser("test2", "test2"));
        assertTrue(userManger.authUser("test3", "test3"));
        assertFalse(userManger.authUser("test3", "test1"));
    }
}
