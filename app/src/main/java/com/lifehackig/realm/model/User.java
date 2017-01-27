package com.lifehackig.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sheena on 1/26/17.
 */

public class User extends RealmObject{
    @PrimaryKey
    private String uid;
    private String username;
    private byte[] profileImage;
    private RealmList<Photo> photoStream;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public RealmList<Photo> getPhotoStream() {
        return photoStream;
    }

}
