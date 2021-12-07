
package com.dev.objects;

public class UserObject {
    int id;
    String username;
    String password;
    String token;
    boolean blockUser = false;

    public UserObject(int id, String username, String password, String token, boolean blockUser) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        this.blockUser = blockUser;
    }

    public UserObject(int id, String username, String password, String token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        this.blockUser = false;
    }

    public UserObject(UserObject userObject) {
        this.id = userObject.id;
        this.username = userObject.username;
        this.password = userObject.password;
        this.token = userObject.token;
        this.blockUser = userObject.blockUser;
    }
    public UserObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBlockUser() {
        return blockUser;
    }

    public void setBlockUser(boolean blockUser) {
        this.blockUser = blockUser;
    }

}

