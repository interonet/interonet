package org.interonet.gdm.AuthenticationCenter;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthTokenManager {
    Map<UserPassword, AuthToken> authTokenMap;

    public AuthTokenManager() {
        authTokenMap = new HashMap<UserPassword, AuthToken>();
    }

    public static String toPlainText(AuthToken authToken) {

        byte[] bytesEncoded;
        bytesEncoded = Base64.getEncoder().encode(authToken.toString().getBytes());
        return new String(bytesEncoded);
    }

    public AuthToken toAuthToken(String PlainText) {
        String authToken = new String(Base64.getDecoder().decode(PlainText.getBytes()));
        AuthToken authTk = new AuthToken(authToken);
        for (Map.Entry<UserPassword, AuthToken> entry : authTokenMap.entrySet()) {
            if (entry.getValue().equals(authTk))
                return entry.getValue();
        }
        return null;
    }

    public AuthToken generate(String username, String password) {
        UserPassword userPassword = new UserPassword(username, password);
        AuthToken authToken = oneTimePassword(username, password);
        authTokenMap.put(userPassword, authToken);
        return authToken;
    }

    private AuthToken oneTimePassword(String username, String password) {
        return new AuthToken(username + password);
    }


    public boolean auth(AuthToken authToken) {
        for (Map.Entry<UserPassword, AuthToken> entry : authTokenMap.entrySet()) {
            if (entry.getValue().equals(authToken))
                return true;
        }
        return false;
    }

    public String getUsernameByToken(AuthToken authToken) {
        for (Map.Entry<UserPassword, AuthToken> entry : authTokenMap.entrySet()) {
            if (entry.getValue().equals(authToken))
                return entry.getKey().username;
        }
        return null;
    }

    public class UserPassword {
        public String username;
        public String password;

        public UserPassword(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
