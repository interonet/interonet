package org.interonet.gdm.AuthenticationCenter;

import java.util.Collection;
import java.util.HashSet;

public class UserManager implements IUserManager {
    UserDBManager userDBManager;
    Collection<User> users;

    public UserManager() {
        users = new HashSet<User>();
        userDBManager = new UserDBManager();
        userDBManager.init(users);
    }

    @Override
    public Boolean authUser(String username, String password) {
        for (User user : users) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
