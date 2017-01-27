package com.lifehackig.realm.model;

import io.realm.RealmObject;

/**
 * Created by Sheena on 1/27/17.
 */

public class Photo extends RealmObject {
    private byte[] image;

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
