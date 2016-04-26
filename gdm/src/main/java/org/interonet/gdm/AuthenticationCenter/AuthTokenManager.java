package org.interonet.gdm.AuthenticationCenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthTokenManager {
    private Map<User, String> userToken = new ConcurrentHashMap<>(100);
    private Logger logger = LoggerFactory.getLogger(AuthTokenManager.class);
    private ScheduledExecutorService tokenUpdater = Executors.newSingleThreadScheduledExecutor();

    public AuthTokenManager(String userDBPath) throws IOException {
        this(null, userDBPath);
    }

    public AuthTokenManager(Integer tokenUpdatePeriod, String userDBPath) throws IOException {
        try {
            List<Map<String, String>> userDBParser;
            logger.info("reading userDB from" + userDBPath);
            File file = new File(userDBPath);
            userDBParser = new ObjectMapper().readValue(file, List.class);
            /*
            * read from the db file, write to the userToken.
            * */
            for (Map<String, String> map : userDBParser) {
                User user = new User();
                String username = map.get("username");
                user.setUsername(username);
                String password = map.get("password");
                user.setPassword(password);
                user.setKeys(null);
                user.setAccountBalance(Integer.parseInt(map.get("accountBalance")));
                String authToken = oneTimePassword();
                userToken.put(user, authToken);
            }
            if (tokenUpdatePeriod == null)
                tokenUpdater.scheduleAtFixedRate(new TokenUpdater(userToken), 0, 5, TimeUnit.MINUTES);
            else
                tokenUpdater.scheduleAtFixedRate(new TokenUpdater(userToken), 0, tokenUpdatePeriod, TimeUnit.MINUTES);

        } catch (IOException e) {
            logger.error("userDBPath Error", e);
            throw e;
        }
    }


    public boolean isTokenExisted(String authToken) {
        return userToken.containsValue(authToken);
    }

    public User getUserByToken(String authToken) {
        for (Map.Entry<User, String> entry : userToken.entrySet()) {
            if (entry.getValue().equals(authToken))
                return entry.getKey();
        }
        return null;
    }

    public String getTokenByUserPassword(String user, String password) {
        for (Map.Entry<User, String> entry : userToken.entrySet()) {
            User userObj = entry.getKey();
            if (userObj.getUsername().equals(user) && userObj.getPassword().equals(password))
                return entry.getValue();
        }
        return null;
    }

    private String oneTimePassword() {
        UUID uuid = UUID.randomUUID();
        //abs(leastsignBits)+abs(mostsignBits)
        return String.valueOf(Math.abs(uuid.getLeastSignificantBits()))
                + String.valueOf(Math.abs(uuid.getMostSignificantBits()));
    }

    public static class User implements Serializable {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String phone;
        private String url;
        private Collection<PublicKey> keys;
        private long accountBalance;

        public User() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Collection<PublicKey> getKeys() {
            return keys;
        }

        public void setKeys(Collection<PublicKey> keys) {
            this.keys = keys;
        }

        public long getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(long accountBalance) {
            this.accountBalance = accountBalance;
        }

        public static class SSHPublicKey implements PublicKey {
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
    }

    private class TokenUpdater implements Runnable {
        Map<User, String> userToken;

        public TokenUpdater(Map<User, String> userToken) {
            this.userToken = userToken;
        }

        @Override
        public void run() {
            for (Map.Entry<User, String> entry : userToken.entrySet()) {
                User user = entry.getKey();
                String token =
                        userToken.put(user, oneTimePassword());
            }
        }
    }
}
