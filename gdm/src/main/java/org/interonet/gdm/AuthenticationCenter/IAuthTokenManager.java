package org.interonet.gdm.AuthenticationCenter;

public interface IAuthTokenManager {
    AuthToken toAuthToken(String PlainText);

    AuthToken generate(String username, String password);

    boolean auth(AuthToken authToken);

    String getUsernameByToken(AuthToken authToken);
}
