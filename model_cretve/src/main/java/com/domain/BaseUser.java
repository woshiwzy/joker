package com.domain;

import java.io.Serializable;

/**
 * Created by wangzy on 16/8/8.
 */
public class BaseUser implements Serializable{


    private String email;
    private String id;
    private String nickName;
    private String avatar;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
