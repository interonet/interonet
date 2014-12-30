package org.interonet.gdm;

public class AuthToken {
    public String authToken;

    public AuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        authToken = authToken;
    }

    public boolean equals(AuthToken auth) {
        return auth.authToken.equals(this.authToken) ? true : false;
    }

    public String toString() {
        return authToken;
    }

}
