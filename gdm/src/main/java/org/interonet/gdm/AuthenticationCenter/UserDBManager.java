package org.interonet.gdm.AuthenticationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserDBManager {
    public void init(Collection<User> users) {

        List<Map<String, String>> userDBParser;
        String conf = "";
        try {
            String INTERONET_HOME = System.getenv().get("INTERONET_HOME");
            File file = new File(INTERONET_HOME + "/db/userDB.json");
            userDBParser = new ObjectMapper().readValue(file, List.class);

            for (Map<String, String> map : userDBParser){
                User user = new User();
                user.setUsername(map.get("username"));
                user.setPassword(map.get("password"));
                //TODO SSH Public Key
                user.setKeys(null);
                user.setAccountBalance(Integer.parseInt(map.get("accountBalance")));
                users.add(user);
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
