package org.interonet.gdm.AuthenticationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.interonet.gdm.Core.GDMCore;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserDBManager {
    GDMCore core;
    public UserDBManager(GDMCore core) {
        this.core = core;
    }

    public void init(Collection<User> users) {

        List<Map<String, String>> userDBParser;
        try {
            Logger.getLogger("UserDBManagerLogger").info("reading userDB from" + core.getConfigurationCenter().getConf("userDB"));
            File file = new File(core.getConfigurationCenter().getConf("userDB"));
            userDBParser = new ObjectMapper().readValue(file, List.class);

            for (Map<String, String> map : userDBParser) {
                User user = new User();
                user.setUsername(map.get("username"));
                user.setPassword(map.get("password"));
                //TODO SSH Public Key
                user.setKeys(null);
                user.setAccountBalance(Integer.parseInt(map.get("accountBalance")));
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
