package org.interonet.gdm.AuthenticationCenter;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Collection;

public class User implements Serializable{
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


}
