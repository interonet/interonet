package org.interonet.gdm.AuthenticationCenter;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    Map<String, String> userDB;

    public UserManager() {
        userDB = new HashMap<String, String>();
        userDB.put("admin", "admin");
        userDB.put("root", "root");
        userDB.put("test1", "test1");
        userDB.put("test2", "test2");
        userDB.put("test3", "test3");
    }

    public Boolean authUser(String username, String password) {
        if (!userDB.get(username).equals(password))
            return false;
        return true;
    }
}
